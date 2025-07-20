package com.pattymoda.repository;

import com.pattymoda.entity.TransferenciaInventario;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface TransferenciaInventarioRepository extends BaseRepository<TransferenciaInventario, Long> {
    Optional<TransferenciaInventario> findByNumeroTransferencia(String numeroTransferencia);
    List<TransferenciaInventario> findByEstado(TransferenciaInventario.EstadoTransferencia estado);
    boolean existsByNumeroTransferencia(String numeroTransferencia);
} 