package com.pattymoda.repository;

import com.pattymoda.entity.Compra;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompraRepository extends BaseRepository<Compra, Long> {
    Optional<Compra> findByNumeroCompra(String numeroCompra);
    boolean existsByNumeroCompra(String numeroCompra);
} 