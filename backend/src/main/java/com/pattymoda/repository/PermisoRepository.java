package com.pattymoda.repository;

import com.pattymoda.entity.Permiso;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface PermisoRepository extends BaseRepository<Permiso, Long> {
    Optional<Permiso> findByCodigo(String codigo);
    List<Permiso> findByActivoTrue();
    boolean existsByCodigo(String codigo);
} 