package com.pattymoda.repository;

import com.pattymoda.entity.ListaDeseos;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ListaDeseosRepository extends BaseRepository<ListaDeseos, Long> {
    
    List<ListaDeseos> findByClienteIdAndActivoTrueOrderByPrioridadDescFechaAgregadoDesc(Long clienteId);
    
    Page<ListaDeseos> findByClienteIdAndActivoTrue(Long clienteId, Pageable pageable);
    
    Optional<ListaDeseos> findByClienteIdAndProductoId(Long clienteId, Long productoId);
    
    @Query("SELECT COUNT(ld) FROM ListaDeseos ld WHERE ld.producto.id = :productoId AND ld.activo = true")
    long countByProducto(@Param("productoId") Long productoId);
    
    @Query("SELECT ld.producto.id, COUNT(ld) FROM ListaDeseos ld WHERE ld.activo = true GROUP BY ld.producto.id ORDER BY COUNT(ld) DESC")
    List<Object[]> findProductosMasDeseados(Pageable pageable);
    
    @Query("SELECT ld FROM ListaDeseos ld WHERE ld.notificarDisponibilidad = true AND ld.activo = true AND ld.producto.id = :productoId")
    List<ListaDeseos> findParaNotificarDisponibilidad(@Param("productoId") Long productoId);
    
    boolean existsByClienteIdAndProductoId(Long clienteId, Long productoId);
}