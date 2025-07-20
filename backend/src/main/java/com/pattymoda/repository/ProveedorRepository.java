package com.pattymoda.repository;

import com.pattymoda.entity.Proveedor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProveedorRepository extends BaseRepository<Proveedor, Long> {
    Optional<Proveedor> findByCodigoProveedor(String codigoProveedor);
    Optional<Proveedor> findByNumeroDocumento(String numeroDocumento);
    boolean existsByCodigoProveedor(String codigoProveedor);
    boolean existsByNumeroDocumento(String numeroDocumento);
} 