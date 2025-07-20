package com.pattymoda.service;

import com.pattymoda.entity.PromocionUso;
import com.pattymoda.repository.PromocionUsoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PromocionUsoService extends BaseService<PromocionUso, Long> {
    @Autowired
    public PromocionUsoService(PromocionUsoRepository promocionUsoRepository) {
        super(promocionUsoRepository);
        this.promocionUsoRepository = promocionUsoRepository;
    }

    private final PromocionUsoRepository promocionUsoRepository;

    public List<PromocionUso> findByPromocionId(Long promocionId) {
        return promocionUsoRepository.findByPromocionId(promocionId);
    }

    public List<PromocionUso> findByClienteId(Long clienteId) {
        return promocionUsoRepository.findByClienteId(clienteId);
    }

    public List<PromocionUso> findByVentaId(Long ventaId) {
        return promocionUsoRepository.findByVentaId(ventaId);
    }
} 