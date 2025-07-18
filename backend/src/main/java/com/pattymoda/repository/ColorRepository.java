package com.pattymoda.repository;

import com.pattymoda.entity.Color;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ColorRepository extends BaseRepository<Color, Long> {
    
    Optional<Color> findByCodigo(String codigo);
    
    List<Color> findByActivoTrueOrderByOrdenVisualizacionAsc();
    
    List<Color> findByFamiliaColorAndActivoTrueOrderByOrdenVisualizacionAsc(String familiaColor);
    
    @Query("SELECT c FROM Color c WHERE c.activo = true AND c.familiaColor IN :familias ORDER BY c.ordenVisualizacion")
    List<Color> findByFamiliasAndActivo(@Param("familias") List<String> familias);
    
    Optional<Color> findByCodigoHex(String codigoHex);
    
    boolean existsByCodigo(String codigo);
    
    @Query("SELECT COUNT(c) FROM Color c WHERE c.activo = true")
    long countActivos();
} 