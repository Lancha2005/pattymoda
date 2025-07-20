package com.pattymoda.repository;

import com.pattymoda.entity.MetodoPagoVenta;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MetodoPagoVentaRepository extends BaseRepository<MetodoPagoVenta, Long> {
    List<MetodoPagoVenta> findByVentaId(Long ventaId);
} 