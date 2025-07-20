package com.pattymoda.repository;

import com.pattymoda.entity.Temporada;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TemporadaRepository extends BaseRepository<Temporada, Long> {
    
    Optional<Temporada> findByCodigo(String codigo);
    
    @Query("SELECT t FROM Temporada t WHERE t.activo = true AND t.esTemporadaActual = true")
    Optional<Temporada> findTemporadaActual();
    
    @Query("SELECT t FROM Temporada t WHERE t.activo = true AND :fecha BETWEEN t.fechaInicio AND t.fechaFin")
    Optional<Temporada> findByFecha(@Param("fecha") LocalDate fecha);
    
    List<Temporada> findByActivoTrueOrderByAñoDescFechaInicioDesc();
    
    @Query("SELECT t FROM Temporada t WHERE t.activo = true AND t.fechaInicioLiquidacion <= :fecha AND t.fechaFin >= :fecha")
    List<Temporada> findTemporadasEnLiquidacion(@Param("fecha") LocalDate fecha);
    
    List<Temporada> findByTipoTemporadaAndActivoTrue(Temporada.TipoTemporada tipoTemporada);
    
    boolean existsByCodigo(String codigo);
}