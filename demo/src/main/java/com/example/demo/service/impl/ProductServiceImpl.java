package com.example.demo.service.impl;

import com.example.demo.dto.ProductoRequestDTO;
import com.example.demo.dto.ProductoResponseDTO;
import com.example.demo.exeption.RecursoNoEncontradoException;
import com.example.demo.model.Producto;
import com.example.demo.repository.IProductoRepository;
import com.example.demo.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private static final String CACHE_PRODUCTOS = "productos";

    private final IProductoRepository productoRepository;

    @Override
    @Cacheable(
            value = CACHE_PRODUCTOS,
            key = "#pageable.pageNumber + '-' + #pageable.pageSize + '-' + #pageable.sort.toString()"
    )
    @Transactional(readOnly = true)
    public Page<ProductoResponseDTO> listarProductos(Pageable pageable) {
        log.debug("Cache MISS -> consultando productos activos en base de datos (pagina={}, tamano={})",
                pageable.getPageNumber(), pageable.getPageSize());

        Page<Producto> pagina = productoRepository.findByActivoTrue(pageable);
        return pagina.map(ProductoResponseDTO::fromEntity);
    }

    @Override
    @CacheEvict(value = CACHE_PRODUCTOS, allEntries = true)
    @Transactional
    public ProductoResponseDTO crearProducto(ProductoRequestDTO request) {
        Producto producto = Producto.builder()
                .nombre(request.getNombre())
                .categoria(request.getCategoria())
                .stock(request.getStock())
                .precio(request.getPrecio())
                .activo(true)
                .build();

        Producto guardado = productoRepository.save(producto);
        log.info("Producto creado id={} nombre='{}' -> cache 'productos' invalidado", guardado.getId(), guardado.getNombre());

        return ProductoResponseDTO.fromEntity(guardado);
    }

    @Override
    @CacheEvict(value = CACHE_PRODUCTOS, allEntries = true)
    @Transactional
    public void eliminarProducto(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontro un producto con id " + id));

        if (Boolean.FALSE.equals(producto.getActivo())) {
            log.debug("Producto id={} ya se encontraba inactivo", id);
            return;
        }

        producto.setActivo(false);
        productoRepository.save(producto);
        log.info("Soft delete aplicado a producto id={} -> cache 'productos' invalidado", id);
    }
}
