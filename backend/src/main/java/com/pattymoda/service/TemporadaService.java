package com.pattymoda.service;

import com.pattymoda.entity.Temporada;
import com.pattymoda.repository.TemporadaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TemporadaService extends BaseService<Temporada, Long> {

    private final TemporadaRepository temporadaRepository;

    @Autowired
    public TemporadaService(TemporadaRepository temporadaRepository) {
        super(temporadaRepository);
        this.temporadaRepository = temporadaRepository;
    }

    public Optional<Temporada> findByCodigo(String codigo) {
        return temporadaRepository.findByCodigo(codigo);
    }

    @Cacheable("temporadas")
    public Optional<Temporada> getTemporadaActual() {
        return temporadaRepository.findTemporadaActual();
    }

    public Optional<Temporada> getTemporadaPorFecha(LocalDate fecha) {
        return temporadaRepository.findByFecha(fecha);
    }

    public List<Temporada> getTemporadasActivas() {
        return temporadaRepository.findByActivoTrueOrderByAñoDescFechaInicioDesc();
    }

    public List<Temporada> getTemporadasEnLiquidacion() {
        return temporadaRepository.findTemporadasEnLiquidacion(LocalDate.now());
    }

    public List<Temporada> getTemporadasPorTipo(Temporada.TipoTemporada tipo) {
        return temporadaRepository.findByTipoTemporadaAndActivoTrue(tipo);
    }

    public void establecerTemporadaActual(Long temporadaId) {
        // Desactivar temporada actual
        temporadaRepository.findTemporadaActual().ifPresent(temporada -> {
            temporada.setEsTemporadaActual(false);
            save(temporada);
        });

        // Activar nueva temporada
        Temporada nuevaTemporada = findById(temporadaId)
                .orElseThrow(() -> new RuntimeException("Temporada no encontrada"));
        nuevaTemporada.setEsTemporadaActual(true);
        save(nuevaTemporada);
    }

    @Override
    public Temporada save(Temporada temporada) {
        if (temporada.getCodigo() == null || temporada.getCodigo().trim().isEmpty()) {
            throw new IllegalArgumentException("El código de temporada es obligatorio");
        }
        if (temporada.getNombre() == null || temporada.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de temporada es obligatorio");
        }
        if (temporada.getFechaInicio() == null || temporada.getFechaFin() == null) {
            throw new IllegalArgumentException("Las fechas de inicio y fin son obligatorias");
        }
        if (temporada.getFechaInicio().isAfter(temporada.getFechaFin())) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser posterior a la fecha de fin");
        }
        if (temporada.getId() == null && temporadaRepository.existsByCodigo(temporada.getCodigo())) {
            throw new IllegalArgumentException("Ya existe una temporada con ese código");
        }

        return super.save(temporada);
    }
}