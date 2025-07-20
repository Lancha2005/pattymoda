package com.pattymoda.repository;

import com.pattymoda.entity.CuponUso;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CuponUsoRepository extends BaseRepository<CuponUso, Long> {
    
    List<CuponUso> findByCuponId(Long cuponId);
    
    List<CuponUso> findByClienteId(Long clienteId);
    
    List<CuponUso> findByVentaId(Long ventaId);
    
    @Query("SELECT COUNT(cu) FROM CuponUso cu WHERE cu.cliente.id = :clienteId AND cu.cupon.id = :cuponId")
    long countByClienteIdAndCuponId(@Param("clienteId") Long clienteId, @Param("cuponId") Long cuponId);
    
    @Query("SELECT SUM(cu.montoDescuento) FROM CuponUso cu WHERE cu.cupon.id = :cuponId")
    java.math.BigDecimal sumDescuentosByCupon(@Param("cuponId") Long cuponId);
    
    @Query("SELECT cu FROM CuponUso cu WHERE cu.fechaUso BETWEEN :inicio AND :fin ORDER BY cu.fechaUso DESC")
    List<CuponUso> findByFechaBetween(@Param("inicio") LocalDateTime inicio, @Param("fin") LocalDateTime fin);
}