package com.tienda.repository;

import com.tienda.entity.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.math.BigDecimal;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {
    List<Venta> findByFechaBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    List<Venta> findByClienteId(Long clienteId);
    
    @Query("SELECT SUM(v.precioTotal) FROM Venta v WHERE v.fecha BETWEEN :fechaInicio AND :fechaFin")
    BigDecimal sumVentasByFecha(@Param("fechaInicio") LocalDateTime fechaInicio, @Param("fechaFin") LocalDateTime fechaFin);
    
    @Query("SELECT COUNT(v) FROM Venta v WHERE v.fecha BETWEEN :fechaInicio AND :fechaFin")
    Long countVentasByFecha(@Param("fechaInicio") LocalDateTime fechaInicio, @Param("fechaFin") LocalDateTime fechaFin);
}