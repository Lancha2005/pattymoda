package com.pattymoda.service;

import com.pattymoda.entity.VentaDescuento;
import com.pattymoda.repository.VentaDescuentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class VentaDescuentoService extends BaseService<VentaDescuento, Long> {
    @Autowired
    public VentaDescuentoService(VentaDescuentoRepository ventaDescuentoRepository) {
        super(ventaDescuentoRepository);
        this.ventaDescuentoRepository = ventaDescuentoRepository;
    }

    private final VentaDescuentoRepository ventaDescuentoRepository;

    public List<VentaDescuento> findByVentaId(Long ventaId) {
        return ventaDescuentoRepository.findByVentaId(ventaId);
    }
} 