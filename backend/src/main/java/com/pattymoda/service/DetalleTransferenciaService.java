package com.pattymoda.service;

import com.pattymoda.entity.DetalleTransferencia;
import com.pattymoda.repository.DetalleTransferenciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DetalleTransferenciaService extends BaseService<DetalleTransferencia, Long> {
    @Autowired
    public DetalleTransferenciaService(DetalleTransferenciaRepository detalleTransferenciaRepository) {
        super(detalleTransferenciaRepository);
        this.detalleTransferenciaRepository = detalleTransferenciaRepository;
    }

    private final DetalleTransferenciaRepository detalleTransferenciaRepository;

    public List<DetalleTransferencia> findByTransferenciaId(Long transferenciaId) {
        return detalleTransferenciaRepository.findByTransferenciaId(transferenciaId);
    }

    public List<DetalleTransferencia> findByProductoId(Long productoId) {
        return detalleTransferenciaRepository.findByProductoId(productoId);
    }
} 