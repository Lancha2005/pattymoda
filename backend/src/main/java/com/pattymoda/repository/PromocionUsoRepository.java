package com.pattymoda.repository;

import com.pattymoda.entity.PromocionUso;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PromocionUsoRepository extends BaseRepository<PromocionUso, Long> {
    List<PromocionUso> findByPromocionId(Long promocionId);
    List<PromocionUso> findByClienteId(Long clienteId);
    List<PromocionUso> findByVentaId(Long ventaId);
} 