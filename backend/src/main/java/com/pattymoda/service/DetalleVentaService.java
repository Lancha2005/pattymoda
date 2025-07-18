package com.pattymoda.service;

import com.pattymoda.entity.DetalleVenta;
import com.pattymoda.repository.DetalleVentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class DetalleVentaService extends BaseService<DetalleVenta, Long> {

    private final DetalleVentaRepository detalleVentaRepository;

    @Autowired
    public DetalleVentaService(DetalleVentaRepository detalleVentaRepository) {
        super(detalleVentaRepository);
        this.detalleVentaRepository = detalleVentaRepository;
    }

    public List<DetalleVenta> findByVentaId(Long ventaId) {
        return detalleVentaRepository.findByVentaId(ventaId);
    }

    public List<DetalleVenta> findDetallesByVentaId(Long ventaId) {
        return detalleVentaRepository.findDetallesByVentaId(ventaId);
    }

    public List<DetalleVenta> findByProductoId(Long productoId) {
        return detalleVentaRepository.findByProductoId(productoId);
    }

    public long countByProductoId(Long productoId) {
        return detalleVentaRepository.countByProductoId(productoId);
    }

    public Integer sumCantidadByProductoId(Long productoId) {
        return detalleVentaRepository.sumCantidadByProductoId(productoId);
    }

    public BigDecimal sumSubtotalByVentaId(Long ventaId) {
        return detalleVentaRepository.sumSubtotalByVentaId(ventaId);
    }

    @Override
    public DetalleVenta save(DetalleVenta detalle) {
        // Validaciones específicas para detalles de venta
        if (detalle.getVenta() == null) {
            throw new IllegalArgumentException("La venta es obligatoria");
        }

        if (detalle.getProducto() == null) {
            throw new IllegalArgumentException("El producto es obligatorio");
        }

        if (detalle.getCantidad() == null || detalle.getCantidad() <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
        }

        if (detalle.getPrecioUnitario() == null || detalle.getPrecioUnitario().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El precio unitario debe ser mayor a 0");
        }

        // Calcular subtotal automáticamente
        BigDecimal cantidad = new BigDecimal(detalle.getCantidad());
        BigDecimal subtotal = detalle.getPrecioUnitario().multiply(cantidad);
        
        // Aplicar descuentos si existen
        if (detalle.getDescuentoMonto() != null && detalle.getDescuentoMonto().compareTo(BigDecimal.ZERO) > 0) {
            subtotal = subtotal.subtract(detalle.getDescuentoMonto());
        } else if (detalle.getDescuentoPorcentaje() != null && detalle.getDescuentoPorcentaje().compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal descuento = subtotal.multiply(detalle.getDescuentoPorcentaje()).divide(new BigDecimal("100"));
            subtotal = subtotal.subtract(descuento);
            detalle.setDescuentoMonto(descuento);
        }

        detalle.setSubtotal(subtotal);

        return super.save(detalle);
    }
}