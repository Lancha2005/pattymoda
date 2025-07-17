package com.tienda.repository;

import com.tienda.entity.DetalleVenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DetalleVentaRepository extends JpaRepository<DetalleVenta, Long> {
    List<DetalleVenta> findByVentaId(Long ventaId);
    
    @Query("SELECT dv FROM DetalleVenta dv WHERE dv.producto.id = :productoId AND dv.venta.fecha BETWEEN :fechaInicio AND :fechaFin")
    List<DetalleVenta> findByProductoAndFecha(@Param("productoId") Long productoId, @Param("fechaInicio") LocalDateTime fechaInicio, @Param("fechaFin") LocalDateTime fechaFin);
    
    @Query("SELECT p.nombre, SUM(dv.cantidad) as total FROM DetalleVenta dv JOIN dv.producto p GROUP BY p.id, p.nombre ORDER BY total DESC")
    List<Object[]> findProductosMasVendidos();
}