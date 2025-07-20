package com.pattymoda.service;

import com.pattymoda.entity.PromocionCategoria;
import com.pattymoda.repository.PromocionCategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PromocionCategoriaService extends BaseService<PromocionCategoria, Long> {
    @Autowired
    public PromocionCategoriaService(PromocionCategoriaRepository promocionCategoriaRepository) {
        super(promocionCategoriaRepository);
        this.promocionCategoriaRepository = promocionCategoriaRepository;
    }

    private final PromocionCategoriaRepository promocionCategoriaRepository;

    public List<PromocionCategoria> findByPromocionId(Long promocionId) {
        return promocionCategoriaRepository.findByPromocionId(promocionId);
    }

    public List<PromocionCategoria> findByCategoriaId(Long categoriaId) {
        return promocionCategoriaRepository.findByCategoriaId(categoriaId);
    }
} 