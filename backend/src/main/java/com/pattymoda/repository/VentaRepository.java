package com.pattymoda.repository;

import com.pattymoda.entity.Venta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface VentaRepository extends BaseRepository<Venta, Long> {

    Optional<Venta> findByNumeroVenta(String numeroVenta);
    
    boolean existsByNumeroVenta(String numeroVenta);
    
    Page<Venta> findByClienteId(Long clienteId, Pageable pageable);
    
    Page<Venta> findByEstado(Venta.EstadoVenta estado, Pageable pageable);
    
    Page<Venta> findByVendedorId(Long vendedorId, Pageable pageable);
    
    Page<Venta> findByCajeroId(Long cajeroId, Pageable pageable);
    
    Page<Venta> findByCanalVentaId(Long canalVentaId, Pageable pageable);
    
    @Query("SELECT v FROM Venta v WHERE v.fecha BETWEEN :fechaInicio AND :fechaFin")
    Page<Venta> findByFechaBetween(@Param("fechaInicio") LocalDateTime fechaInicio, @Param("fechaFin") LocalDateTime fechaFin, Pageable pageable);
    
    @Query("SELECT v FROM Venta v WHERE v.cliente.id = :clienteId AND v.estado = :estado")
    List<Venta> findByClienteIdAndEstado(@Param("clienteId") Long clienteId, @Param("estado") Venta.EstadoVenta estado);
    
    @Query("SELECT SUM(v.total) FROM Venta v WHERE v.estado = 'PAGADA' AND v.fecha BETWEEN :fechaInicio AND :fechaFin")
    BigDecimal sumTotalVentasPagadas(@Param("fechaInicio") LocalDateTime fechaInicio, @Param("fechaFin") LocalDateTime fechaFin);
    
    @Query("SELECT COUNT(v) FROM Venta v WHERE v.estado = 'PAGADA' AND v.fecha BETWEEN :fechaInicio AND :fechaFin")
    long countVentasPagadas(@Param("fechaInicio") LocalDateTime fechaInicio, @Param("fechaFin") LocalDateTime fechaFin);
    
    @Query("SELECT v FROM Venta v WHERE v.estado = 'PENDIENTE' AND v.fechaVencimiento <= :fecha")
    List<Venta> findVentasVencidas(@Param("fecha") LocalDate fecha);
    
    @Query("SELECT COUNT(v) FROM Venta v WHERE v.activo = true")
    long countVentasActivas();
} 