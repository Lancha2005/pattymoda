package com.pattymoda.repository;

import com.pattymoda.entity.Coleccion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ColeccionRepository extends BaseRepository<Coleccion, Long> {
    
    Optional<Coleccion> findByCodigo(String codigo);
    
    Optional<Coleccion> findBySlug(String slug);
    
    Page<Coleccion> findByActivoTrueOrderByOrdenVisualizacionAsc(Pageable pageable);
    
    List<Coleccion> findByDestacadoTrueAndActivoTrueOrderByOrdenVisualizacionAsc();
    
    @Query("SELECT c FROM Coleccion c WHERE c.activo = true AND c.temporada.id = :temporadaId ORDER BY c.ordenVisualizacion")
    List<Coleccion> findByTemporadaActiva(@Param("temporadaId") Long temporadaId);
    
    @Query("SELECT c FROM Coleccion c WHERE c.activo = true AND :fecha BETWEEN c.fechaLanzamiento AND COALESCE(c.fechaFinVenta, :fecha)")
    List<Coleccion> findColeccionesDisponibles(@Param("fecha") LocalDate fecha);
    
    List<Coleccion> findByTipoColeccionAndActivoTrue(Coleccion.TipoColeccion tipoColeccion);
    
    @Query("SELECT c FROM Coleccion c WHERE c.activo = true AND (c.nombre LIKE %:busqueda% OR c.descripcion LIKE %:busqueda%)")
    Page<Coleccion> buscarColecciones(@Param("busqueda") String busqueda, Pageable pageable);
    
    boolean existsByCodigo(String codigo);
    
    boolean existsBySlug(String slug);
}