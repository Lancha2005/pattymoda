package com.pattymoda.repository;

import com.pattymoda.entity.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends BaseRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);
    
    Optional<Usuario> findByCodigoEmpleado(String codigoEmpleado);
    
    boolean existsByEmail(String email);
    
    boolean existsByCodigoEmpleado(String codigoEmpleado);
    
    Page<Usuario> findByActivo(Boolean activo, Pageable pageable);
    
    Page<Usuario> findByRolId(Long rolId, Pageable pageable);
    
    @Query("SELECT u FROM Usuario u WHERE u.activo = true AND (u.nombre LIKE %:busqueda% OR u.apellido LIKE %:busqueda% OR u.email LIKE %:busqueda%)")
    Page<Usuario> buscarUsuariosActivos(@Param("busqueda") String busqueda, Pageable pageable);
    
    @Query("SELECT u FROM Usuario u WHERE u.activo = true AND u.rol.codigo = :rolCodigo")
    List<Usuario> findByRolCodigo(@Param("rolCodigo") String rolCodigo);
    
    @Query("SELECT COUNT(u) FROM Usuario u WHERE u.activo = true")
    long countUsuariosActivos();
} 