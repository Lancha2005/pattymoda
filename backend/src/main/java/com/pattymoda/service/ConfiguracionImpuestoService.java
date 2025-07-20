package com.pattymoda.service;

import com.pattymoda.entity.ConfiguracionImpuesto;
import com.pattymoda.repository.ConfiguracionImpuestoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ConfiguracionImpuestoService extends BaseService<ConfiguracionImpuesto, Long> {
    @Autowired
    public ConfiguracionImpuestoService(ConfiguracionImpuestoRepository configuracionImpuestoRepository) {
        super(configuracionImpuestoRepository);
        this.configuracionImpuestoRepository = configuracionImpuestoRepository;
    }

    private final ConfiguracionImpuestoRepository configuracionImpuestoRepository;

    public Optional<ConfiguracionImpuesto> findByCodigo(String codigo) {
        return configuracionImpuestoRepository.findByCodigo(codigo);
    }

    public List<ConfiguracionImpuesto> findActivos() {
        return configuracionImpuestoRepository.findByActivoTrue();
    }

    public boolean existsByCodigo(String codigo) {
        return configuracionImpuestoRepository.existsByCodigo(codigo);
    }
} 