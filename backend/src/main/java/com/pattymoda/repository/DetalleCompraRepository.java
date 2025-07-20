package com.pattymoda.repository;

import com.pattymoda.entity.DetalleCompra;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetalleCompraRepository extends BaseRepository<DetalleCompra, Long> {
    List<DetalleCompra> findByCompraId(Long compraId);
    List<DetalleCompra> findByProductoId(Long productoId);
} 