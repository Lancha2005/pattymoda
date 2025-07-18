package com.pattymoda.repository;

import com.pattymoda.entity.PromocionProducto;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PromocionProductoRepository extends BaseRepository<PromocionProducto, Long> {
    List<PromocionProducto> findByPromocionId(Long promocionId);
    List<PromocionProducto> findByProductoId(Long productoId);
} 