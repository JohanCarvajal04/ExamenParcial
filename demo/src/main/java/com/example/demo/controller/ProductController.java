package com.example.demo.controller;


import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.PageMetaDTO;
import com.example.demo.dto.ProductoRequestDTO;
import com.example.demo.dto.ProductoResponseDTO;
import com.example.demo.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/productos")
@RequiredArgsConstructor
@Tag(name = "Productos", description = "Gestion de inventario del Mercado Municipal de Quevedo")
@SecurityRequirement(name = "bearerAuth")
public class ProductController {
    private final ProductService productoService;

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Lista productos activos de forma paginada (cache-aside con Redis)")
    public ResponseEntity<ApiResponse<List<ProductoResponseDTO>>> listarProductos(
            @PageableDefault(page = 0, size = 10, sort = "id") Pageable pageable) {

        Page<ProductoResponseDTO> pagina = productoService.listarProductos(pageable);

        PageMetaDTO meta = PageMetaDTO.fromPage(pagina, pageable.getSort().toString());

        return ResponseEntity.ok(
                ApiResponse.success(pagina.getContent(), "Productos obtenidos correctamente", meta)
        );
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Crea un nuevo producto e invalida el cache del listado")
    public ResponseEntity<ApiResponse<ProductoResponseDTO>> crearProducto(
            @Valid @RequestBody ProductoRequestDTO request) {

        ProductoResponseDTO creado = productoService.crearProducto(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.success(creado, "Producto creado correctamente")
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Elimina (soft delete) un producto por id e invalida el cache del listado")
    public ResponseEntity<ApiResponse<Object>> eliminarProducto(@PathVariable Long id) {
        productoService.eliminarProducto(id);
        return ResponseEntity.ok(
                ApiResponse.success(null, "Producto eliminado correctamente (soft delete)")
        );
    }
}
