package com.pattymoda.service;

import com.pattymoda.entity.MovimientoInventario;
import com.pattymoda.entity.Producto;
import com.pattymoda.entity.ProductoInventario;
import com.pattymoda.repository.MovimientoInventarioRepository;
import com.pattymoda.repository.ProductoInventarioRepository;
import com.pattymoda.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class InventarioService {

    @Autowired
    private ProductoInventarioRepository inventarioRepository;
    
    @Autowired
    private MovimientoInventarioRepository movimientoRepository;
    
    @Autowired
    private ProductoRepository productoRepository;

    @Cacheable(value = "inventario", key = "#productoId")
    public Optional<ProductoInventario> getInventarioByProducto(Long productoId) {
        return inventarioRepository.findByProductoId(productoId);
    }

    @CacheEvict(value = "inventario", key = "#productoId")
    public void actualizarStock(Long productoId, Integer cantidad, 
                               MovimientoInventario.TipoMovimiento tipo,
                               MovimientoInventario.MotivoMovimiento motivo,
                               String referencia) {
        
        ProductoInventario inventario = inventarioRepository.findByProductoId(productoId)
            .orElseThrow(() -> new RuntimeException("Inventario no encontrado para producto: " + productoId));

        Integer stockAnterior = inventario.getStockActual();
        Integer nuevoStock;

        if (tipo == MovimientoInventario.TipoMovimiento.ENTRADA) {
            nuevoStock = stockAnterior + cantidad;
        } else if (tipo == MovimientoInventario.TipoMovimiento.SALIDA) {
            if (stockAnterior < cantidad) {
                throw new RuntimeException("Stock insuficiente. Stock actual: " + stockAnterior + ", Solicitado: " + cantidad);
            }
            nuevoStock = stockAnterior - cantidad;
        } else {
            nuevoStock = cantidad; // Para ajustes
        }

        // Actualizar inventario
        inventario.setStockActual(nuevoStock);
        inventario.setStockDisponible(nuevoStock);
        inventario.setFechaActualizacion(LocalDateTime.now());
        inventarioRepository.save(inventario);

        // Registrar movimiento
        MovimientoInventario movimiento = new MovimientoInventario();
        movimiento.setProducto(inventario.getProducto());
        movimiento.setTipoMovimiento(tipo);
        movimiento.setMotivo(motivo);
        movimiento.setCantidadAnterior(stockAnterior);
        movimiento.setCantidadMovimiento(cantidad);
        movimiento.setCantidadActual(nuevoStock);
        movimiento.setReferenciaDocumento(referencia);
        movimiento.setFechaMovimiento(LocalDateTime.now());
        
        movimientoRepository.save(movimiento);
    }

    public List<Producto> getProductosStockBajo() {
        return productoRepository.findProductosStockBajo();
    }

    public List<Producto> getProductosSinStock() {
        return inventarioRepository.findProductosSinStock();
    }

    public void verificarStockMinimo(Long productoId) {
        ProductoInventario inventario = inventarioRepository.findByProductoId(productoId)
            .orElseThrow(() -> new RuntimeException("Inventario no encontrado"));

        if (inventario.getStockActual() <= inventario.getStockMinimo()) {
            // Aquí podrías enviar notificaciones, crear alertas, etc.
            // notificationService.enviarAlertaStockBajo(inventario);
        }
    }

    public BigDecimal calcularValorInventario() {
        return inventarioRepository.calcularValorTotalInventario();
    }
}