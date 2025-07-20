package com.pattymoda.repository;

import com.pattymoda.entity.Producto;
import com.pattymoda.entity.ProductoInventario;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoInventarioRepository extends BaseRepository<ProductoInventario, Long> {
    
    Optional<ProductoInventario> findByProductoId(Long productoId);
    
    @Query("SELECT pi.producto FROM ProductoInventario pi WHERE pi.stockActual = 0")
    List<Producto> findProductosSinStock();
    
    @Query("SELECT pi.producto FROM ProductoInventario pi WHERE pi.stockActual <= pi.stockMinimo AND pi.stockActual > 0")
    List<Producto> findProductosStockBajo();
    
    @Query("SELECT SUM(pi.stockActual * pp.costo) FROM ProductoInventario pi " +
           "JOIN ProductoPrecio pp ON pi.producto.id = pp.producto.id " +
           "WHERE pp.activo = true")
    BigDecimal calcularValorTotalInventario();
    
    @Query("SELECT COUNT(pi) FROM ProductoInventario pi WHERE pi.stockActual > 0")
    long countProductosConStock();
    
    @Query("SELECT SUM(pi.stockActual) FROM ProductoInventario pi")
    Long sumTotalStock();
}