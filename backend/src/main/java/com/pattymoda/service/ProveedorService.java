package com.pattymoda.service;

import com.pattymoda.entity.Proveedor;
import com.pattymoda.repository.ProveedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class ProveedorService extends BaseService<Proveedor, Long> {
    @Autowired
    public ProveedorService(ProveedorRepository proveedorRepository) {
        super(proveedorRepository);
        this.proveedorRepository = proveedorRepository;
    }

    private final ProveedorRepository proveedorRepository;

    public Optional<Proveedor> findByCodigoProveedor(String codigoProveedor) {
        return proveedorRepository.findByCodigoProveedor(codigoProveedor);
    }

    public Optional<Proveedor> findByNumeroDocumento(String numeroDocumento) {
        return proveedorRepository.findByNumeroDocumento(numeroDocumento);
    }

    public boolean existsByCodigoProveedor(String codigoProveedor) {
        return proveedorRepository.existsByCodigoProveedor(codigoProveedor);
    }

    public boolean existsByNumeroDocumento(String numeroDocumento) {
        return proveedorRepository.existsByNumeroDocumento(numeroDocumento);
    }

    @Override
    public Proveedor save(Proveedor proveedor) {
        if (proveedor.getCodigoProveedor() == null || proveedor.getCodigoProveedor().trim().isEmpty()) {
            throw new IllegalArgumentException("El código de proveedor es obligatorio");
        }
        if (proveedor.getRazonSocial() == null || proveedor.getRazonSocial().trim().isEmpty()) {
            throw new IllegalArgumentException("La razón social es obligatoria");
        }
        if (proveedor.getNumeroDocumento() == null || proveedor.getNumeroDocumento().trim().isEmpty()) {
            throw new IllegalArgumentException("El número de documento es obligatorio");
        }
        if (proveedor.getId() == null && existsByCodigoProveedor(proveedor.getCodigoProveedor())) {
            throw new IllegalArgumentException("Ya existe un proveedor con ese código");
        }
        if (proveedor.getId() == null && existsByNumeroDocumento(proveedor.getNumeroDocumento())) {
            throw new IllegalArgumentException("Ya existe un proveedor con ese número de documento");
        }
        return super.save(proveedor);
    }
} 