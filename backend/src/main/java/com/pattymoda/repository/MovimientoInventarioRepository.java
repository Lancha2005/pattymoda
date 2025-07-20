package com.pattymoda.repository;

import com.pattymoda.entity.MovimientoInventario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MovimientoInventarioRepository extends BaseRepository<MovimientoInventario, Long> {
    
    List<MovimientoInventario> findByProductoIdOrderByFechaMovimientoDesc(Long productoId);
    
    Page<MovimientoInventario> findByTipoMovimiento(MovimientoInventario.TipoMovimiento tipo, Pageable pageable);
    
    Page<MovimientoInventario> findByMotivo(MovimientoInventario.MotivoMovimiento motivo, Pageable pageable);
    
    @Query("SELECT m FROM MovimientoInventario m WHERE m.fechaMovimiento BETWEEN :fechaInicio AND :fechaFin ORDER BY m.fechaMovimiento DESC")
    Page<MovimientoInventario> findByFechaBetween(@Param("fechaInicio") LocalDateTime fechaInicio, 
                                                  @Param("fechaFin") LocalDateTime fechaFin, 
                                                  Pageable pageable);
    
    @Query("SELECT m FROM MovimientoInventario m WHERE m.usuario.id = :usuarioId ORDER BY m.fechaMovimiento DESC")
    Page<MovimientoInventario> findByUsuarioId(@Param("usuarioId") Long usuarioId, Pageable pageable);
    
    @Query("SELECT COUNT(m) FROM MovimientoInventario m WHERE m.tipoMovimiento = :tipo AND m.fechaMovimiento >= :fecha")
    long countMovimientosByTipoSince(@Param("tipo") MovimientoInventario.TipoMovimiento tipo, 
                                     @Param("fecha") LocalDateTime fecha);
}