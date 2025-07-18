package com.pattymoda.service;

import com.pattymoda.entity.ClientePreferencia;
import com.pattymoda.repository.ClientePreferenciaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ClientePreferenciaService extends BaseService<ClientePreferencia, Long> {
    private final ClientePreferenciaRepository clientePreferenciaRepository;

    public ClientePreferenciaService(ClientePreferenciaRepository clientePreferenciaRepository) {
        super(clientePreferenciaRepository);
        this.clientePreferenciaRepository = clientePreferenciaRepository;
    }

    public List<ClientePreferencia> findByClienteId(Long clienteId) {
        return clientePreferenciaRepository.findByClienteId(clienteId);
    }
} 