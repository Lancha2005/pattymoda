package com.tienda.repository;

import com.tienda.entity.ProductoTalla;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductoTallaRepository extends JpaRepository<ProductoTalla, Long> {
    List<ProductoTalla> findByProductoId(Long productoId);
    void deleteByProductoId(Long productoId);
}