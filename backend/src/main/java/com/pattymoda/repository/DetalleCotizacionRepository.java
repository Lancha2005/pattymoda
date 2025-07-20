package com.pattymoda.repository;

import com.pattymoda.entity.DetalleCotizacion;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DetalleCotizacionRepository extends BaseRepository<DetalleCotizacion, Long> {
    List<DetalleCotizacion> findByCotizacionId(Long cotizacionId);
    List<DetalleCotizacion> findByProductoId(Long productoId);
} 