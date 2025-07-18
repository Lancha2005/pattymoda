package com.pattymoda.repository;

import com.pattymoda.entity.VentaEntrega;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VentaEntregaRepository extends BaseRepository<VentaEntrega, Long> {
    Optional<VentaEntrega> findByVentaId(Long ventaId);
} 