package com.example.demo.dto;

import com.example.demo.model.Producto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductoResponseDTO implements Serializable {

    private Long id;
    private String nombre;
    private String categoria;
    private Integer stock;
    private BigDecimal precio;
    private Boolean activo;
    private OffsetDateTime creadoEn;

    public static ProductoResponseDTO fromEntity(Producto producto) {
        return ProductoResponseDTO.builder()
                .id(producto.getId())
                .nombre(producto.getNombre())
                .categoria(producto.getCategoria())
                .stock(producto.getStock())
                .precio(producto.getPrecio())
                .activo(producto.getActivo())
                .creadoEn(producto.getCreadoEn())
                .build();
    }
}
