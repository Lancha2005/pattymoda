package com.pattymoda.service;

import com.pattymoda.entity.Compra;
import com.pattymoda.repository.CompraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class CompraService extends BaseService<Compra, Long> {
    @Autowired
    public CompraService(CompraRepository compraRepository) {
        super(compraRepository);
        this.compraRepository = compraRepository;
    }

    private final CompraRepository compraRepository;

    public Optional<Compra> findByNumeroCompra(String numeroCompra) {
        return compraRepository.findByNumeroCompra(numeroCompra);
    }

    public boolean existsByNumeroCompra(String numeroCompra) {
        return compraRepository.existsByNumeroCompra(numeroCompra);
    }

    @Override
    public Compra save(Compra compra) {
        if (compra.getNumeroCompra() == null || compra.getNumeroCompra().trim().isEmpty()) {
            throw new IllegalArgumentException("El número de compra es obligatorio");
        }
        if (compra.getProveedor() == null) {
            throw new IllegalArgumentException("El proveedor es obligatorio");
        }
        if (compra.getFechaCompra() == null) {
            throw new IllegalArgumentException("La fecha de compra es obligatoria");
        }
        if (compra.getId() == null && existsByNumeroCompra(compra.getNumeroCompra())) {
            throw new IllegalArgumentException("Ya existe una compra con ese número");
        }
        return super.save(compra);
    }
} 