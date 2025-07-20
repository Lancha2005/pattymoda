package com.pattymoda.service;

import com.pattymoda.entity.DetalleCotizacion;
import com.pattymoda.repository.DetalleCotizacionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DetalleCotizacionService extends BaseService<DetalleCotizacion, Long> {
    private final DetalleCotizacionRepository detalleCotizacionRepository;

    public DetalleCotizacionService(DetalleCotizacionRepository detalleCotizacionRepository) {
        super(detalleCotizacionRepository);
        this.detalleCotizacionRepository = detalleCotizacionRepository;
    }

    public List<DetalleCotizacion> findByCotizacionId(Long cotizacionId) {
        return detalleCotizacionRepository.findByCotizacionId(cotizacionId);
    }

    public List<DetalleCotizacion> findByProductoId(Long productoId) {
        return detalleCotizacionRepository.findByProductoId(productoId);
    }
} 