package com.pattymoda.repository;

import com.pattymoda.entity.Devolucion;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface DevolucionRepository extends BaseRepository<Devolucion, Long> {
    Optional<Devolucion> findByNumeroDevolucion(String numeroDevolucion);
    List<Devolucion> findByClienteId(Long clienteId);
    List<Devolucion> findByVentaId(Long ventaId);
    boolean existsByNumeroDevolucion(String numeroDevolucion);
} 