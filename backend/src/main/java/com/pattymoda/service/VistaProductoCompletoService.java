package com.pattymoda.service;

import com.pattymoda.dto.VistaProductoCompletoDTO;
import com.pattymoda.repository.VistaProductoCompletoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class VistaProductoCompletoService {
    @Autowired
    private VistaProductoCompletoRepository repository;

    public Page<VistaProductoCompletoDTO> findAllByFilters(String nombre, Long categoriaId, Long marcaId, Pageable pageable) {
        return repository.findAllByFilters(nombre, categoriaId, marcaId, pageable);
    }
} 