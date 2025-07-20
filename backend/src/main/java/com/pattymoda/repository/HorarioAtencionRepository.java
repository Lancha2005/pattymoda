package com.pattymoda.repository;

import com.pattymoda.entity.HorarioAtencion;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface HorarioAtencionRepository extends BaseRepository<HorarioAtencion, Long> {
    Optional<HorarioAtencion> findByDiaSemana(HorarioAtencion.DiaSemana diaSemana);
    List<HorarioAtencion> findByActivoTrue();
} 