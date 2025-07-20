package com.pattymoda.repository;

import com.pattymoda.entity.DetalleVenta;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetalleVentaRepository extends BaseRepository<DetalleVenta, Long> {
    
    List<DetalleVenta> findByVentaId(Long ventaId);
    
    @Query("SELECT dv FROM DetalleVenta dv WHERE dv.venta.id = :ventaId ORDER BY dv.fechaCreacion")
    List<DetalleVenta> findDetallesByVentaId(@Param("ventaId") Long ventaId);
    
    @Query("SELECT dv FROM DetalleVenta dv WHERE dv.producto.id = :productoId ORDER BY dv.fechaCreacion DESC")
    List<DetalleVenta> findByProductoId(@Param("productoId") Long productoId);
    
    @Query("SELECT COUNT(dv) FROM DetalleVenta dv WHERE dv.producto.id = :productoId")
    long countByProductoId(@Param("productoId") Long productoId);
    
    @Query("SELECT SUM(dv.cantidad) FROM DetalleVenta dv WHERE dv.producto.id = :productoId")
    Integer sumCantidadByProductoId(@Param("productoId") Long productoId);
    
    @Query("SELECT SUM(dv.subtotal) FROM DetalleVenta dv WHERE dv.venta.id = :ventaId")
    java.math.BigDecimal sumSubtotalByVentaId(@Param("ventaId") Long ventaId);
} 