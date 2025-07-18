package com.pattymoda.service;

import com.pattymoda.entity.DetalleCompra;
import com.pattymoda.repository.DetalleCompraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DetalleCompraService extends BaseService<DetalleCompra, Long> {
    @Autowired
    public DetalleCompraService(DetalleCompraRepository detalleCompraRepository) {
        super(detalleCompraRepository);
        this.detalleCompraRepository = detalleCompraRepository;
    }

    private final DetalleCompraRepository detalleCompraRepository;

    public List<DetalleCompra> findByCompraId(Long compraId) {
        return detalleCompraRepository.findByCompraId(compraId);
    }

    public List<DetalleCompra> findByProductoId(Long productoId) {
        return detalleCompraRepository.findByProductoId(productoId);
    }
} 