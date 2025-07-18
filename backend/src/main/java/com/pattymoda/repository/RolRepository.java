package com.pattymoda.repository;

import com.pattymoda.entity.Rol;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RolRepository extends BaseRepository<Rol, Long> {

    Optional<Rol> findByCodigo(String codigo);
    
    boolean existsByCodigo(String codigo);
    
    Page<Rol> findByActivo(Boolean activo, Pageable pageable);
    
    List<Rol> findByActivoTrueOrderByNombre();
    
    @Query("SELECT COUNT(r) FROM Rol r WHERE r.activo = true")
    long countRolesActivos();
}