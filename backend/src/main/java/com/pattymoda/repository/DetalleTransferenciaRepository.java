package com.pattymoda.repository;

import com.pattymoda.entity.DetalleTransferencia;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetalleTransferenciaRepository extends BaseRepository<DetalleTransferencia, Long> {
    List<DetalleTransferencia> findByTransferenciaId(Long transferenciaId);
    List<DetalleTransferencia> findByProductoId(Long productoId);
} 