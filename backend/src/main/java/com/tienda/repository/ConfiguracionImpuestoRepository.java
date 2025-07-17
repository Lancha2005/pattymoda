package com.tienda.repository;

import com.tienda.entity.ConfiguracionImpuesto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ConfiguracionImpuestoRepository extends JpaRepository<ConfiguracionImpuesto, Long> {
    
    List<ConfiguracionImpuesto> findByActivoTrue();
    
    @Query("SELECT c FROM ConfiguracionImpuesto c WHERE c.activo = true AND c.aplicarPorDefecto = true")
    Optional<ConfiguracionImpuesto> findImpuestoPorDefecto();
    
    Optional<ConfiguracionImpuesto> findByNombreAndActivoTrue(String nombre);
    
    @Query("SELECT c FROM ConfiguracionImpuesto c WHERE c.nombre = 'IGV' AND c.activo = true")
    Optional<ConfiguracionImpuesto> findIGVActivo();
}