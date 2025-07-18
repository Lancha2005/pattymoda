package com.pattymoda.controller;

import com.pattymoda.entity.DetalleVenta;
import com.pattymoda.service.DetalleVentaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/detalle-venta")
@Tag(name = "DetalleVenta", description = "API para gestión de detalles de venta")
@CrossOrigin(origins = "*")
public class DetalleVentaController extends BaseController<DetalleVenta, Long> {

    private final DetalleVentaService detalleVentaService;

    @Autowired
    public DetalleVentaController(DetalleVentaService detalleVentaService) {
        super(detalleVentaService);
        this.detalleVentaService = detalleVentaService;
    }

    @GetMapping("/venta/{ventaId}")
    @Operation(summary = "Obtener detalles por venta", description = "Obtiene todos los detalles de una venta")
    public ResponseEntity<List<DetalleVenta>> getDetallesByVenta(@PathVariable Long ventaId) {
        List<DetalleVenta> detalles = detalleVentaService.findDetallesByVentaId(ventaId);
        return ResponseEntity.ok(detalles);
    }

    @GetMapping("/producto/{productoId}")
    @Operation(summary = "Obtener detalles por producto", description = "Obtiene todos los detalles de venta de un producto")
    public ResponseEntity<List<DetalleVenta>> getDetallesByProducto(@PathVariable Long productoId) {
        List<DetalleVenta> detalles = detalleVentaService.findByProductoId(productoId);
        return ResponseEntity.ok(detalles);
    }

    @GetMapping("/producto/{productoId}/count")
    @Operation(summary = "Contar ventas por producto", description = "Cuenta cuántas veces se ha vendido un producto")
    public ResponseEntity<Long> countVentasByProducto(@PathVariable Long productoId) {
        long count = detalleVentaService.countByProductoId(productoId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/producto/{productoId}/cantidad")
    @Operation(summary = "Sumar cantidad vendida por producto", description = "Suma la cantidad total vendida de un producto")
    public ResponseEntity<Integer> sumCantidadByProducto(@PathVariable Long productoId) {
        Integer cantidad = detalleVentaService.sumCantidadByProductoId(productoId);
        return ResponseEntity.ok(cantidad != null ? cantidad : 0);
    }

    @GetMapping("/venta/{ventaId}/subtotal")
    @Operation(summary = "Calcular subtotal de venta", description = "Calcula el subtotal de todos los detalles de una venta")
    public ResponseEntity<BigDecimal> calcularSubtotalVenta(@PathVariable Long ventaId) {
        BigDecimal subtotal = detalleVentaService.sumSubtotalByVentaId(ventaId);
        return ResponseEntity.ok(subtotal != null ? subtotal : BigDecimal.ZERO);
    }

    @PostMapping
    @Operation(summary = "Crear detalle de venta", description = "Crea un nuevo detalle de venta")
    public ResponseEntity<DetalleVenta> createDetalle(@RequestBody DetalleVenta detalle) {
        try {
            DetalleVenta savedDetalle = detalleVentaService.save(detalle);
            return ResponseEntity.status(201).body(savedDetalle);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar detalle de venta", description = "Actualiza un detalle de venta existente")
    public ResponseEntity<DetalleVenta> updateDetalle(@PathVariable Long id, @RequestBody DetalleVenta detalle) {
        if (!detalleVentaService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        detalle.setId(id);
        try {
            DetalleVenta updatedDetalle = detalleVentaService.save(detalle);
            return ResponseEntity.ok(updatedDetalle);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar detalle de venta", description = "Elimina un detalle de venta")
    public ResponseEntity<Void> deleteDetalle(@PathVariable Long id) {
        if (!detalleVentaService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        detalleVentaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}