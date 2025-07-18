package com.pattymoda.repository;

import com.pattymoda.entity.RolPermiso;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RolPermisoRepository extends BaseRepository<RolPermiso, Long> {
    List<RolPermiso> findByRolId(Long rolId);
    List<RolPermiso> findByPermisoId(Long permisoId);
} 