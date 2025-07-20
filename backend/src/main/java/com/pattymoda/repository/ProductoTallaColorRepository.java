package com.pattymoda.repository;

import com.pattymoda.entity.ProductoTallaColor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoTallaColorRepository extends BaseRepository<ProductoTallaColor, Long> {
    
    List<ProductoTallaColor> findByProductoIdAndActivoTrue(Long productoId);
    
    Optional<ProductoTallaColor> findByProductoIdAndTallaIdAndColorId(Long productoId, Long tallaId, Long colorId);
    
    @Query("SELECT ptc FROM ProductoTallaColor ptc WHERE ptc.producto.id = :productoId AND ptc.stockDisponible > 0 AND ptc.activo = true")
    List<ProductoTallaColor> findDisponiblesByProducto(@Param("productoId") Long productoId);
    
    @Query("SELECT ptc FROM ProductoTallaColor ptc WHERE ptc.stockActual <= ptc.stockMinimo AND ptc.activo = true")
    List<ProductoTallaColor> findStockBajo();
    
    @Query("SELECT ptc FROM ProductoTallaColor ptc WHERE ptc.stockActual = 0 AND ptc.activo = true")
    List<ProductoTallaColor> findSinStock();
    
    @Query("SELECT SUM(ptc.stockActual) FROM ProductoTallaColor ptc WHERE ptc.producto.id = :productoId AND ptc.activo = true")
    Integer sumStockByProducto(@Param("productoId") Long productoId);
    
    @Query("SELECT COUNT(ptc) FROM ProductoTallaColor ptc WHERE ptc.producto.id = :productoId AND ptc.stockDisponible > 0 AND ptc.activo = true")
    long countCombinacionesDisponibles(@Param("productoId") Long productoId);
    
    Optional<ProductoTallaColor> findBySkuCombinacion(String skuCombinacion);
    
    Optional<ProductoTallaColor> findByCodigoBarrasCombinacion(String codigoBarrasCombinacion);
    
    boolean existsBySkuCombinacion(String skuCombinacion);
    
    boolean existsByCodigoBarrasCombinacion(String codigoBarrasCombinacion);
}