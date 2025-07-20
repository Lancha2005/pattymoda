package com.pattymoda.repository;

import com.pattymoda.entity.Categoria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoriaRepository extends BaseRepository<Categoria, Long> {

    Optional<Categoria> findByCodigo(String codigo);
    
    boolean existsByCodigo(String codigo);
    
    Page<Categoria> findByActivo(Boolean activo, Pageable pageable);
    
    @Query("SELECT c FROM Categoria c WHERE c.activo = true AND c.categoriaPadre IS NULL ORDER BY c.orden")
    List<Categoria> findCategoriasRaiz();
    
    @Query("SELECT c FROM Categoria c WHERE c.activo = true AND c.categoriaPadre.id = :padreId ORDER BY c.orden")
    List<Categoria> findSubcategorias(@Param("padreId") Long padreId);
    
    @Query("SELECT c FROM Categoria c WHERE c.activo = true AND (c.nombre LIKE %:busqueda% OR c.codigo LIKE %:busqueda%)")
    Page<Categoria> buscarCategorias(@Param("busqueda") String busqueda, Pageable pageable);
    
    @Query("SELECT COUNT(c) FROM Categoria c WHERE c.activo = true")
    long countCategoriasActivas();
}