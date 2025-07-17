package com.tienda.repository;

import com.tienda.entity.ProductoColor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductoColorRepository extends JpaRepository<ProductoColor, Long> {
    List<ProductoColor> findByProductoId(Long productoId);
    void deleteByProductoId(Long productoId);
}