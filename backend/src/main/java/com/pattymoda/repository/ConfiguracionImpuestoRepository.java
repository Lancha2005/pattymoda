package com.pattymoda.repository;

import com.pattymoda.entity.ConfiguracionImpuesto;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface ConfiguracionImpuestoRepository extends BaseRepository<ConfiguracionImpuesto, Long> {
    Optional<ConfiguracionImpuesto> findByCodigo(String codigo);
    List<ConfiguracionImpuesto> findByActivoTrue();
    boolean existsByCodigo(String codigo);
} 