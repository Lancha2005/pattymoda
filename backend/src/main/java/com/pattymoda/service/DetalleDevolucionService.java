package com.pattymoda.service;

import com.pattymoda.entity.DetalleDevolucion;
import com.pattymoda.repository.DetalleDevolucionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DetalleDevolucionService extends BaseService<DetalleDevolucion, Long> {
    @Autowired
    public DetalleDevolucionService(DetalleDevolucionRepository detalleDevolucionRepository) {
        super(detalleDevolucionRepository);
        this.detalleDevolucionRepository = detalleDevolucionRepository;
    }

    private final DetalleDevolucionRepository detalleDevolucionRepository;

    public List<DetalleDevolucion> findByDevolucionId(Long devolucionId) {
        return detalleDevolucionRepository.findByDevolucionId(devolucionId);
    }

    public List<DetalleDevolucion> findByDetalleVentaId(Long detalleVentaId) {
        return detalleDevolucionRepository.findByDetalleVentaId(detalleVentaId);
    }
} 