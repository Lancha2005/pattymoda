package com.pattymoda.service;

import com.pattymoda.entity.TareaSeguimiento;
import com.pattymoda.repository.TareaSeguimientoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TareaSeguimientoService extends BaseService<TareaSeguimiento, Long> {
    @Autowired
    public TareaSeguimientoService(TareaSeguimientoRepository tareaSeguimientoRepository) {
        super(tareaSeguimientoRepository);
        this.tareaSeguimientoRepository = tareaSeguimientoRepository;
    }

    private final TareaSeguimientoRepository tareaSeguimientoRepository;

    public List<TareaSeguimiento> findByUsuarioAsignadoId(Long usuarioId) {
        return tareaSeguimientoRepository.findByUsuarioAsignadoId(usuarioId);
    }

    public List<TareaSeguimiento> findByEstado(TareaSeguimiento.EstadoTarea estado) {
        return tareaSeguimientoRepository.findByEstado(estado);
    }

    public List<TareaSeguimiento> findByPrioridad(TareaSeguimiento.PrioridadTarea prioridad) {
        return tareaSeguimientoRepository.findByPrioridad(prioridad);
    }

    public List<TareaSeguimiento> findByClienteId(Long clienteId) {
        return tareaSeguimientoRepository.findByClienteId(clienteId);
    }

    public List<TareaSeguimiento> findByVentaId(Long ventaId) {
        return tareaSeguimientoRepository.findByVentaId(ventaId);
    }

    public List<TareaSeguimiento> findByCotizacionId(Long cotizacionId) {
        return tareaSeguimientoRepository.findByCotizacionId(cotizacionId);
    }
} 