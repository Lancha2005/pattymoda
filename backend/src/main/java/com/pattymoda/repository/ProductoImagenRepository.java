package com.pattymoda.repository;

import com.pattymoda.entity.ProductoImagen;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductoImagenRepository extends BaseRepository<ProductoImagen, Long> {
    List<ProductoImagen> findByProductoId(Long productoId);
    List<ProductoImagen> findByColorId(Long colorId);
    List<ProductoImagen> findByEsPrincipalTrue();
    List<ProductoImagen> findByActivoTrue();
} 