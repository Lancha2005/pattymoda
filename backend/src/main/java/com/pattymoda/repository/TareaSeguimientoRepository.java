package com.pattymoda.repository;

import com.pattymoda.entity.TareaSeguimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TareaSeguimientoRepository extends BaseRepository<TareaSeguimiento, Long> {
    List<TareaSeguimiento> findByUsuarioAsignadoId(Long usuarioId);
    List<TareaSeguimiento> findByEstado(TareaSeguimiento.EstadoTarea estado);
    List<TareaSeguimiento> findByPrioridad(TareaSeguimiento.PrioridadTarea prioridad);
    List<TareaSeguimiento> findByClienteId(Long clienteId);
    List<TareaSeguimiento> findByVentaId(Long ventaId);
    List<TareaSeguimiento> findByCotizacionId(Long cotizacionId);
} 