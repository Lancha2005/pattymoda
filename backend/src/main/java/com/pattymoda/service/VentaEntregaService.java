package com.pattymoda.service;

import com.pattymoda.entity.VentaEntrega;
import com.pattymoda.repository.VentaEntregaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class VentaEntregaService extends BaseService<VentaEntrega, Long> {
    @Autowired
    public VentaEntregaService(VentaEntregaRepository ventaEntregaRepository) {
        super(ventaEntregaRepository);
        this.ventaEntregaRepository = ventaEntregaRepository;
    }

    private final VentaEntregaRepository ventaEntregaRepository;

    public Optional<VentaEntrega> findByVentaId(Long ventaId) {
        return ventaEntregaRepository.findByVentaId(ventaId);
    }

    @Override
    public VentaEntrega save(VentaEntrega entrega) {
        if (entrega.getVenta() == null) {
            throw new IllegalArgumentException("La venta es obligatoria");
        }
        if (entrega.getTipoEntrega() == null) {
            throw new IllegalArgumentException("El tipo de entrega es obligatorio");
        }
        return super.save(entrega);
    }
} 