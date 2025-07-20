package com.pattymoda.service;

import com.pattymoda.entity.Permiso;
import com.pattymoda.repository.PermisoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PermisoService extends BaseService<Permiso, Long> {
    @Autowired
    public PermisoService(PermisoRepository permisoRepository) {
        super(permisoRepository);
        this.permisoRepository = permisoRepository;
    }

    private final PermisoRepository permisoRepository;

    public Optional<Permiso> findByCodigo(String codigo) {
        return permisoRepository.findByCodigo(codigo);
    }

    public List<Permiso> findActivos() {
        return permisoRepository.findByActivoTrue();
    }

    public boolean existsByCodigo(String codigo) {
        return permisoRepository.existsByCodigo(codigo);
    }
} 