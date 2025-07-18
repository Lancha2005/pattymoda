package com.pattymoda.repository;

import com.pattymoda.entity.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends BaseRepository<Producto, Long> {

    Optional<Producto> findByCodigoProducto(String codigoProducto);
    
    Optional<Producto> findBySku(String sku);
    
    Optional<Producto> findByCodigoBarras(String codigoBarras);
    
    boolean existsByCodigoProducto(String codigoProducto);
    
    boolean existsBySku(String sku);
    
    boolean existsByCodigoBarras(String codigoBarras);
    
    Page<Producto> findByActivo(Boolean activo, Pageable pageable);
    
    Page<Producto> findByCategoriaId(Long categoriaId, Pageable pageable);
    
    Page<Producto> findByMarcaId(Long marcaId, Pageable pageable);
    
    Page<Producto> findByDestacado(Boolean destacado, Pageable pageable);
    
    Page<Producto> findByNuevo(Boolean nuevo, Pageable pageable);
    
    @Query("SELECT p FROM Producto p WHERE p.activo = true AND (p.nombre LIKE %:busqueda% OR p.descripcion LIKE %:busqueda% OR p.sku LIKE %:busqueda%)")
    Page<Producto> buscarProductos(@Param("busqueda") String busqueda, Pageable pageable);
    
    @Query("SELECT p FROM Producto p WHERE p.activo = true AND p.categoria.id = :categoriaId AND p.destacado = true")
    List<Producto> findDestacadosByCategoria(@Param("categoriaId") Long categoriaId);
    
    @Query("SELECT p FROM Producto p WHERE p.activo = true AND p.stockActual <= p.stockMinimo")
    List<Producto> findProductosStockBajo();
    
    @Query("SELECT COUNT(p) FROM Producto p WHERE p.activo = true")
    long countProductosActivos();
    
    @Query("SELECT COUNT(p) FROM Producto p WHERE p.activo = true AND p.destacado = true")
    long countProductosDestacados();
} 