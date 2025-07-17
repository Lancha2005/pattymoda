package com.tienda.controller;

import com.tienda.entity.Venta;
import com.tienda.dto.VentaRequest;
import com.tienda.service.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/ventas")
@Tag(name = "Ventas", description = "Gestión de ventas")
public class VentaController {
    
    @Autowired
    private VentaService ventaService;
    
    @GetMapping
    @Operation(summary = "Obtener todas las ventas", description = "Devuelve una lista de todas las ventas")
    public ResponseEntity<List<Venta>> obtenerTodasLasVentas() {
        List<Venta> ventas = ventaService.obtenerTodasLasVentas();
        return ResponseEntity.ok(ventas);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Obtener venta por ID", description = "Devuelve una venta específica por su ID")
    public ResponseEntity<Venta> obtenerVentaPorId(@PathVariable Long id) {
        return ventaService.obtenerVentaPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/cliente/{clienteId}")
    @Operation(summary = "Obtener ventas por cliente", description = "Devuelve las ventas de un cliente específico")
    public ResponseEntity<List<Venta>> obtenerVentasPorCliente(@PathVariable Long clienteId) {
        List<Venta> ventas = ventaService.obtenerVentasPorCliente(clienteId);
        return ResponseEntity.ok(ventas);
    }
    
    @GetMapping("/fecha")
    @Operation(summary = "Obtener ventas por fecha", description = "Devuelve las ventas en un rango de fechas")
    public ResponseEntity<List<Venta>> obtenerVentasPorFecha(
            @RequestParam String fechaInicio,
            @RequestParam String fechaFin) {
        try {
            LocalDateTime inicio = LocalDateTime.parse(fechaInicio);
            LocalDateTime fin = LocalDateTime.parse(fechaFin);
            List<Venta> ventas = ventaService.obtenerVentasPorFecha(inicio, fin);
            return ResponseEntity.ok(ventas);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PostMapping
    @Operation(summary = "Crear venta", description = "Registra una nueva venta")
    public ResponseEntity<Venta> crearVenta(@Valid @RequestBody VentaRequest ventaRequest) {
        try {
            Venta nuevaVenta = ventaService.crearVenta(ventaRequest);
            return ResponseEntity.ok(nuevaVenta);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}