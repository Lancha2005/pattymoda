package com.pattymoda.service;

import com.pattymoda.entity.MetodoPagoVenta;
import com.pattymoda.repository.MetodoPagoVentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MetodoPagoVentaService extends BaseService<MetodoPagoVenta, Long> {
    @Autowired
    public MetodoPagoVentaService(MetodoPagoVentaRepository metodoPagoVentaRepository) {
        super(metodoPagoVentaRepository);
        this.metodoPagoVentaRepository = metodoPagoVentaRepository;
    }

    private final MetodoPagoVentaRepository metodoPagoVentaRepository;

    public List<MetodoPagoVenta> findByVentaId(Long ventaId) {
        return metodoPagoVentaRepository.findByVentaId(ventaId);
    }
} 