package com.pattymoda.service;

import com.pattymoda.entity.TransaccionPunto;
import com.pattymoda.repository.TransaccionPuntoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TransaccionPuntoService extends BaseService<TransaccionPunto, Long> {
    @Autowired
    public TransaccionPuntoService(TransaccionPuntoRepository transaccionPuntoRepository) {
        super(transaccionPuntoRepository);
        this.transaccionPuntoRepository = transaccionPuntoRepository;
    }

    private final TransaccionPuntoRepository transaccionPuntoRepository;

    public List<TransaccionPunto> findByClienteId(Long clienteId) {
        return transaccionPuntoRepository.findByClienteId(clienteId);
    }

    public List<TransaccionPunto> findByVentaId(Long ventaId) {
        return transaccionPuntoRepository.findByVentaId(ventaId);
    }
} 