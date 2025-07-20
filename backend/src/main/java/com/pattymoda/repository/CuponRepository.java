package com.pattymoda.repository;

import com.pattymoda.entity.Cupon;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CuponRepository extends BaseRepository<Cupon, Long> {
    
    Optional<Cupon> findByCodigo(String codigo);
    
    @Query("SELECT c FROM Cupon c WHERE c.activo = true AND c.visiblePublico = true AND :fecha BETWEEN c.fechaInicio AND c.fechaFin")
    List<Cupon> findCuponesDisponibles(@Param("fecha") LocalDate fecha);
    
    @Query("SELECT c FROM Cupon c WHERE c.activo = true AND c.codigo = :codigo AND :fecha BETWEEN c.fechaInicio AND c.fechaFin")
    Optional<Cupon> findCuponValido(@Param("codigo") String codigo, @Param("fecha") LocalDate fecha);
    
    @Query("SELECT c FROM Cupon c WHERE c.activo = true AND (c.cantidadMaximaUsos IS NULL OR c.usosActuales < c.cantidadMaximaUsos)")
    List<Cupon> findCuponesConUsosDisponibles();
    
    List<Cupon> findByActivoTrueOrderByFechaFinAsc();
    
    boolean existsByCodigo(String codigo);
}