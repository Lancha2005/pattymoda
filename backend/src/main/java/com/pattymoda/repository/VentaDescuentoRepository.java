package com.pattymoda.repository;

import com.pattymoda.entity.VentaDescuento;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VentaDescuentoRepository extends BaseRepository<VentaDescuento, Long> {
    List<VentaDescuento> findByVentaId(Long ventaId);
} 