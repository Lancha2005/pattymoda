package com.pattymoda.service;

import com.pattymoda.entity.HorarioAtencion;
import com.pattymoda.repository.HorarioAtencionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.List;

@Service
@Transactional
public class HorarioAtencionService extends BaseService<HorarioAtencion, Long> {
    @Autowired
    public HorarioAtencionService(HorarioAtencionRepository horarioAtencionRepository) {
        super(horarioAtencionRepository);
        this.horarioAtencionRepository = horarioAtencionRepository;
    }

    private final HorarioAtencionRepository horarioAtencionRepository;

    public Optional<HorarioAtencion> findByDiaSemana(HorarioAtencion.DiaSemana diaSemana) {
        return horarioAtencionRepository.findByDiaSemana(diaSemana);
    }

    public List<HorarioAtencion> findActivos() {
        return horarioAtencionRepository.findByActivoTrue();
    }
} 