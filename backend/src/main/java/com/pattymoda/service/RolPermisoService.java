package com.pattymoda.service;

import com.pattymoda.entity.RolPermiso;
import com.pattymoda.repository.RolPermisoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RolPermisoService extends BaseService<RolPermiso, Long> {
    @Autowired
    public RolPermisoService(RolPermisoRepository rolPermisoRepository) {
        super(rolPermisoRepository);
        this.rolPermisoRepository = rolPermisoRepository;
    }

    private final RolPermisoRepository rolPermisoRepository;

    public List<RolPermiso> findByRolId(Long rolId) {
        return rolPermisoRepository.findByRolId(rolId);
    }

    public List<RolPermiso> findByPermisoId(Long permisoId) {
        return rolPermisoRepository.findByPermisoId(permisoId);
    }
} 