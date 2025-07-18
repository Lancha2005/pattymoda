package com.pattymoda.repository;

import com.pattymoda.entity.Marca;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MarcaRepository extends BaseRepository<Marca, Long> {

    Optional<Marca> findByNombre(String nombre);
    
    boolean existsByNombre(String nombre);
    
    Page<Marca> findByActivo(Boolean activo, Pageable pageable);
    
    @Query("SELECT m FROM Marca m WHERE m.activo = true AND m.nombre LIKE %:busqueda%")
    Page<Marca> buscarMarcas(@Param("busqueda") String busqueda, Pageable pageable);
    
    @Query("SELECT COUNT(m) FROM Marca m WHERE m.activo = true")
    long countMarcasActivas();
    
    List<Marca> findByActivoTrueOrderByNombre();
}