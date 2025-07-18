package com.pattymoda.service;

import com.pattymoda.entity.VentaFacturacion;
import com.pattymoda.repository.VentaFacturacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class VentaFacturacionService extends BaseService<VentaFacturacion, Long> {
    @Autowired
    public VentaFacturacionService(VentaFacturacionRepository ventaFacturacionRepository) {
        super(ventaFacturacionRepository);
        this.ventaFacturacionRepository = ventaFacturacionRepository;
    }

    private final VentaFacturacionRepository ventaFacturacionRepository;

    public Optional<VentaFacturacion> findByVentaId(Long ventaId) {
        return ventaFacturacionRepository.findByVentaId(ventaId);
    }

    public Optional<VentaFacturacion> findByNumeroComprobante(String numeroComprobante) {
        return ventaFacturacionRepository.findByNumeroComprobante(numeroComprobante);
    }

    public boolean existsByNumeroComprobante(String numeroComprobante) {
        return ventaFacturacionRepository.existsByNumeroComprobante(numeroComprobante);
    }

    @Override
    public VentaFacturacion save(VentaFacturacion facturacion) {
        if (facturacion.getVenta() == null) {
            throw new IllegalArgumentException("La venta es obligatoria");
        }
        if (facturacion.getTipoComprobante() == null) {
            throw new IllegalArgumentException("El tipo de comprobante es obligatorio");
        }
        if (facturacion.getNumeroComprobante() == null || facturacion.getNumeroComprobante().trim().isEmpty()) {
            throw new IllegalArgumentException("El número de comprobante es obligatorio");
        }
        if (facturacion.getId() == null && existsByNumeroComprobante(facturacion.getNumeroComprobante())) {
            throw new IllegalArgumentException("Ya existe un comprobante con ese número");
        }
        return super.save(facturacion);
    }
} 