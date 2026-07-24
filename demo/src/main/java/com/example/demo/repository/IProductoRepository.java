package com.example.demo.repository;

import com.example.demo.model.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IProductoRepository extends JpaRepository <Producto, Long> {
    Page<Producto> findByActivoTrue(Pageable pageable);
    Optional<Producto> findByIdAndActivoTrue(Long id);
}
