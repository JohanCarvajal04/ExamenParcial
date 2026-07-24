package com.example.demo.service;

import com.example.demo.dto.ProductoRequestDTO;
import com.example.demo.dto.ProductoResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {
    Page<ProductoResponseDTO> listarProductos(Pageable pageable);

    ProductoResponseDTO crearProducto(ProductoRequestDTO request);

    void eliminarProducto(Long id);
}
