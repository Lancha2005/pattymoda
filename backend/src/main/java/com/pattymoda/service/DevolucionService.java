package com.pattymoda.service;

import com.pattymoda.entity.Devolucion;
import com.pattymoda.repository.DevolucionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DevolucionService extends BaseService<Devolucion, Long> {
    @Autowired
    public DevolucionService(DevolucionRepository devolucionRepository) {
        super(devolucionRepository);
        this.devolucionRepository = devolucionRepository;
    }

    private final DevolucionRepository devolucionRepository;

    public Optional<Devolucion> findByNumeroDevolucion(String numeroDevolucion) {
        return devolucionRepository.findByNumeroDevolucion(numeroDevolucion);
    }

    public List<Devolucion> findByClienteId(Long clienteId) {
        return devolucionRepository.findByClienteId(clienteId);
    }

    public List<Devolucion> findByVentaId(Long ventaId) {
        return devolucionRepository.findByVentaId(ventaId);
    }

    public boolean existsByNumeroDevolucion(String numeroDevolucion) {
        return devolucionRepository.existsByNumeroDevolucion(numeroDevolucion);
    }

    @Override
    public Devolucion save(Devolucion devolucion) {
        if (devolucion.getNumeroDevolucion() == null || devolucion.getNumeroDevolucion().trim().isEmpty()) {
            throw new IllegalArgumentException("El número de devolución es obligatorio");
        }
        if (devolucion.getVenta() == null) {
            throw new IllegalArgumentException("La venta es obligatoria");
        }
        if (devolucion.getCliente() == null) {
            throw new IllegalArgumentException("El cliente es obligatorio");
        }
        if (devolucion.getFechaDevolucion() == null) {
            throw new IllegalArgumentException("La fecha de devolución es obligatoria");
        }
        if (devolucion.getId() == null && existsByNumeroDevolucion(devolucion.getNumeroDevolucion())) {
            throw new IllegalArgumentException("Ya existe una devolución con ese número");
        }
        return super.save(devolucion);
    }
} 