package com.pattymoda.service;

import com.pattymoda.dto.VistaClienteEstadisticaDTO;
import com.pattymoda.repository.VistaClienteEstadisticaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class VistaClienteEstadisticaService {
    @Autowired
    private VistaClienteEstadisticaRepository repository;

    public Page<VistaClienteEstadisticaDTO> findAllByFilters(String nombre, String documento, String tipoCliente, Pageable pageable) {
        return repository.findAllByFilters(nombre, documento, tipoCliente, pageable);
    }
} 