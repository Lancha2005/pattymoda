package com.pattymoda.repository;

import com.pattymoda.entity.VentaFacturacion;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VentaFacturacionRepository extends BaseRepository<VentaFacturacion, Long> {
    Optional<VentaFacturacion> findByVentaId(Long ventaId);
    Optional<VentaFacturacion> findByNumeroComprobante(String numeroComprobante);
    boolean existsByNumeroComprobante(String numeroComprobante);
} 