package com.pattymoda.repository;

import com.pattymoda.entity.ReservaProducto;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservaProductoRepository extends BaseRepository<ReservaProducto, Long> {
    
    Optional<ReservaProducto> findByNumeroReserva(String numeroReserva);
    
    List<ReservaProducto> findByClienteIdAndEstado(Long clienteId, ReservaProducto.EstadoReserva estado);
    
    @Query("SELECT r FROM ReservaProducto r WHERE r.estado = 'ACTIVA' AND r.fechaExpiracion <= :fecha")
    List<ReservaProducto> findReservasExpiradas(@Param("fecha") LocalDateTime fecha);
    
    @Query("SELECT r FROM ReservaProducto r WHERE r.producto.id = :productoId AND r.estado = 'ACTIVA'")
    List<ReservaProducto> findReservasActivasByProducto(@Param("productoId") Long productoId);
    
    @Query("SELECT SUM(r.cantidadReservada) FROM ReservaProducto r WHERE r.producto.id = :productoId AND r.talla.id = :tallaId AND r.color.id = :colorId AND r.estado = 'ACTIVA'")
    Integer sumCantidadReservada(@Param("productoId") Long productoId, @Param("tallaId") Long tallaId, @Param("colorId") Long colorId);
    
    List<ReservaProducto> findByVendedorIdAndEstado(Long vendedorId, ReservaProducto.EstadoReserva estado);
    
    boolean existsByNumeroReserva(String numeroReserva);
}