package com.pattymoda.repository;

import com.pattymoda.entity.ProductoSeo;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoSeoRepository extends BaseRepository<ProductoSeo, Long> {
    ProductoSeo findByProductoId(Long productoId);
    ProductoSeo findBySlug(String slug);
} 