package com.pattymoda.repository;

import com.pattymoda.entity.TransaccionPunto;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransaccionPuntoRepository extends BaseRepository<TransaccionPunto, Long> {
    List<TransaccionPunto> findByClienteId(Long clienteId);
    List<TransaccionPunto> findByVentaId(Long ventaId);
} 