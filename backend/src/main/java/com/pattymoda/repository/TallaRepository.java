package com.pattymoda.repository;

import com.pattymoda.entity.Talla;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TallaRepository extends BaseRepository<Talla, Long> {
    
    Optional<Talla> findByCodigo(String codigo);
    
    List<Talla> findByActivoTrueOrderByOrdenVisualizacionAsc();
    
    List<Talla> findByCategoriaTallaAndActivoTrueOrderByOrdenVisualizacionAsc(Talla.CategoriaTalla categoriaTalla);
    
    @Query("SELECT t FROM Talla t WHERE t.activo = true AND t.categoriaTalla IN :categorias ORDER BY t.ordenVisualizacion")
    List<Talla> findByCategoriasAndActivo(@Param("categorias") List<Talla.CategoriaTalla> categorias);
    
    boolean existsByCodigo(String codigo);
    
    @Query("SELECT COUNT(t) FROM Talla t WHERE t.activo = true")
    long countActivas();
} 