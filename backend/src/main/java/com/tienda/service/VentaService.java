package com.tienda.service;

import com.tienda.entity.*;
import com.tienda.dto.VentaRequest;
import com.tienda.repository.*;
import com.tienda.service.ConfiguracionImpuestoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class VentaService {

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private DetalleVentaRepository detalleVentaRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private ConfiguracionImpuestoService configuracionImpuestoService;

    public List<Venta> obtenerTodasLasVentas() {
        return ventaRepository.findAll();
    }

    public Optional<Venta> obtenerVentaPorId(Long id) {
        return ventaRepository.findById(id);
    }

    public List<Venta> obtenerVentasPorFecha(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return ventaRepository.findByFechaBetween(fechaInicio, fechaFin);
    }

    public List<Venta> obtenerVentasPorCliente(Long clienteId) {
        return ventaRepository.findByClienteId(clienteId);
    }

    @Transactional
    public Venta crearVenta(VentaRequest ventaRequest) {
        // Obtener el cliente
        Cliente cliente = clienteRepository.findById(ventaRequest.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        // Crear la venta
        Venta venta = new Venta();
        venta.setCliente(cliente);
        venta.setFecha(LocalDateTime.now());

        BigDecimal total = BigDecimal.ZERO;
        BigDecimal subtotal = BigDecimal.ZERO;

        // Procesar cada producto
        for (VentaRequest.DetalleVentaRequest detalle : ventaRequest.getProductos()) {
            Producto producto = productoRepository.findById(detalle.getProductoId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

            // Verificar stock disponible
            if (producto.getStock() < detalle.getCantidad()) {
                throw new RuntimeException("Stock insuficiente para el producto: " + producto.getNombre());
            }

            // Calcular subtotal
            subtotal = subtotal.add(producto.getPrecio().multiply(BigDecimal.valueOf(detalle.getCantidad())));

            // Actualizar stock
            producto.setStock(producto.getStock() - detalle.getCantidad());
            productoRepository.save(producto);
        }

        // Calcular impuestos
        BigDecimal impuesto = configuracionImpuestoService.calcularImpuesto(subtotal);
        total = subtotal.add(impuesto);

        venta.setSubtotal(subtotal);
        venta.setImpuesto(impuesto);
        venta.setPrecioTotal(total);
        venta = ventaRepository.save(venta);

        // Crear detalles de venta
        for (VentaRequest.DetalleVentaRequest detalle : ventaRequest.getProductos()) {
            Producto producto = productoRepository.findById(detalle.getProductoId()).get();

            DetalleVenta detalleVenta = new DetalleVenta();
            detalleVenta.setVenta(venta);
            detalleVenta.setProducto(producto);
            detalleVenta.setCantidad(detalle.getCantidad());
            detalleVenta.setPrecioUnitario(producto.getPrecio());

            detalleVentaRepository.save(detalleVenta);
        }

        return venta;
    }

    public BigDecimal obtenerTotalVentasPorFecha(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        BigDecimal total = ventaRepository.sumVentasByFecha(fechaInicio, fechaFin);
        return total != null ? total : BigDecimal.ZERO;
    }

    public Long obtenerCantidadVentasPorFecha(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return ventaRepository.countVentasByFecha(fechaInicio, fechaFin);
    }
}