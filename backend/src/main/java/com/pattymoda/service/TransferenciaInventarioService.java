package com.pattymoda.service;

import com.pattymoda.entity.TransferenciaInventario;
import com.pattymoda.repository.TransferenciaInventarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TransferenciaInventarioService extends BaseService<TransferenciaInventario, Long> {
    @Autowired
    public TransferenciaInventarioService(TransferenciaInventarioRepository transferenciaInventarioRepository) {
        super(transferenciaInventarioRepository);
        this.transferenciaInventarioRepository = transferenciaInventarioRepository;
    }

    private final TransferenciaInventarioRepository transferenciaInventarioRepository;

    public Optional<TransferenciaInventario> findByNumeroTransferencia(String numeroTransferencia) {
        return transferenciaInventarioRepository.findByNumeroTransferencia(numeroTransferencia);
    }

    public List<TransferenciaInventario> findByEstado(TransferenciaInventario.EstadoTransferencia estado) {
        return transferenciaInventarioRepository.findByEstado(estado);
    }

    public boolean existsByNumeroTransferencia(String numeroTransferencia) {
        return transferenciaInventarioRepository.existsByNumeroTransferencia(numeroTransferencia);
    }

    @Override
    public TransferenciaInventario save(TransferenciaInventario transferencia) {
        if (transferencia.getNumeroTransferencia() == null || transferencia.getNumeroTransferencia().trim().isEmpty()) {
            throw new IllegalArgumentException("El número de transferencia es obligatorio");
        }
        if (transferencia.getUbicacionOrigen() == null || transferencia.getUbicacionOrigen().trim().isEmpty()) {
            throw new IllegalArgumentException("La ubicación de origen es obligatoria");
        }
        if (transferencia.getUbicacionDestino() == null || transferencia.getUbicacionDestino().trim().isEmpty()) {
            throw new IllegalArgumentException("La ubicación de destino es obligatoria");
        }
        if (transferencia.getFechaTransferencia() == null) {
            throw new IllegalArgumentException("La fecha de transferencia es obligatoria");
        }
        if (transferencia.getId() == null && existsByNumeroTransferencia(transferencia.getNumeroTransferencia())) {
            throw new IllegalArgumentException("Ya existe una transferencia con ese número");
        }
        return super.save(transferencia);
    }
} 