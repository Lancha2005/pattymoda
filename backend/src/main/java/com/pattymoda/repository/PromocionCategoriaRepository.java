package com.pattymoda.repository;

import com.pattymoda.entity.PromocionCategoria;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PromocionCategoriaRepository extends BaseRepository<PromocionCategoria, Long> {
    List<PromocionCategoria> findByPromocionId(Long promocionId);
    List<PromocionCategoria> findByCategoriaId(Long categoriaId);
} 