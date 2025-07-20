package com.pattymoda.service;

import com.pattymoda.entity.ProductoSeo;
import com.pattymoda.repository.ProductoSeoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProductoSeoService extends BaseService<ProductoSeo, Long> {
    private final ProductoSeoRepository productoSeoRepository;

    public ProductoSeoService(ProductoSeoRepository productoSeoRepository) {
        super(productoSeoRepository);
        this.productoSeoRepository = productoSeoRepository;
    }

    public ProductoSeo findByProductoId(Long productoId) {
        return productoSeoRepository.findByProductoId(productoId);
    }

    public ProductoSeo findBySlug(String slug) {
        return productoSeoRepository.findBySlug(slug);
    }
} 