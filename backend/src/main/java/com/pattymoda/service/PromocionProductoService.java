package com.pattymoda.service;

import com.pattymoda.entity.PromocionProducto;
import com.pattymoda.repository.PromocionProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PromocionProductoService extends BaseService<PromocionProducto, Long> {
    @Autowired
    public PromocionProductoService(PromocionProductoRepository promocionProductoRepository) {
        super(promocionProductoRepository);
        this.promocionProductoRepository = promocionProductoRepository;
    }

    private final PromocionProductoRepository promocionProductoRepository;

    public List<PromocionProducto> findByPromocionId(Long promocionId) {
        return promocionProductoRepository.findByPromocionId(promocionId);
    }

    public List<PromocionProducto> findByProductoId(Long productoId) {
        return promocionProductoRepository.findByProductoId(productoId);
    }
} 