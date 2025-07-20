package com.pattymoda.service;

import com.pattymoda.dto.VistaVentaCompletaDTO;
import com.pattymoda.repository.VistaVentaCompletaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class VistaVentaCompletaService {
    @Autowired
    private VistaVentaCompletaRepository repository;

    public Page<VistaVentaCompletaDTO> findAllByFilters(String numeroVenta, String clienteNombre, String estado, Pageable pageable) {
        return repository.findAllByFilters(numeroVenta, clienteNombre, estado, pageable);
    }
} 