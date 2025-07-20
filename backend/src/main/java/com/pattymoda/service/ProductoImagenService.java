package com.pattymoda.service;

import com.pattymoda.entity.ProductoImagen;
import com.pattymoda.repository.ProductoImagenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProductoImagenService extends BaseService<ProductoImagen, Long> {
    private final ProductoImagenRepository productoImagenRepository;

    public ProductoImagenService(ProductoImagenRepository productoImagenRepository) {
        super(productoImagenRepository);
        this.productoImagenRepository = productoImagenRepository;
    }

    public List<ProductoImagen> findByProductoId(Long productoId) {
        return productoImagenRepository.findByProductoId(productoId);
    }

    public List<ProductoImagen> findByColorId(Long colorId) {
        return productoImagenRepository.findByColorId(colorId);
    }

    public List<ProductoImagen> findPrincipales() {
        return productoImagenRepository.findByEsPrincipalTrue();
    }

    public List<ProductoImagen> findActivas() {
        return productoImagenRepository.findByActivoTrue();
    }
} 