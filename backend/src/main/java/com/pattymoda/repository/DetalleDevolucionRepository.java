package com.pattymoda.repository;

import com.pattymoda.entity.DetalleDevolucion;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetalleDevolucionRepository extends BaseRepository<DetalleDevolucion, Long> {
    List<DetalleDevolucion> findByDevolucionId(Long devolucionId);
    List<DetalleDevolucion> findByDetalleVentaId(Long detalleVentaId);
} 