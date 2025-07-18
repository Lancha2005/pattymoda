package com.pattymoda.controller;

import com.pattymoda.entity.Venta;
import com.pattymoda.service.VentaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/ventas")
@Tag(name = "Ventas", description = "API para gestión de ventas")
@CrossOrigin(origins = "*")
public class VentaController extends BaseController<Venta, Long> {

    private final VentaService ventaService;

    @Autowired
    public VentaController(VentaService ventaService) {
        super(ventaService);
        this.ventaService = ventaService;
    }

    @GetMapping("/numero/{numeroVenta}")
    @Operation(summary = "Buscar venta por número", description = "Busca una venta específica por su número")
    public ResponseEntity<Venta> getVentaByNumero(@PathVariable String numeroVenta) {
        return ventaService.findByNumeroVenta(numeroVenta)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/cliente/{clienteId}")
    @Operation(summary = "Obtener ventas por cliente", description = "Retorna todas las ventas de un cliente")
    public ResponseEntity<Page<Venta>> getVentasByCliente(@PathVariable Long clienteId, Pageable pageable) {
        Page<Venta> ventas = ventaService.findByClienteId(clienteId, pageable);
        return ResponseEntity.ok(ventas);
    }

    @GetMapping("/estado/{estado}")
    @Operation(summary = "Obtener ventas por estado", description = "Retorna todas las ventas con un estado específico")
    public ResponseEntity<Page<Venta>> getVentasByEstado(@PathVariable Venta.EstadoVenta estado, Pageable pageable) {
        Page<Venta> ventas = ventaService.findByEstado(estado, pageable);
        return ResponseEntity.ok(ventas);
    }

    @GetMapping("/vendedor/{vendedorId}")
    @Operation(summary = "Obtener ventas por vendedor", description = "Retorna todas las ventas de un vendedor")
    public ResponseEntity<Page<Venta>> getVentasByVendedor(@PathVariable Long vendedorId, Pageable pageable) {
        Page<Venta> ventas = ventaService.findByVendedorId(vendedorId, pageable);
        return ResponseEntity.ok(ventas);
    }

    @GetMapping("/cajero/{cajeroId}")
    @Operation(summary = "Obtener ventas por cajero", description = "Retorna todas las ventas procesadas por un cajero")
    public ResponseEntity<Page<Venta>> getVentasByCajero(@PathVariable Long cajeroId, Pageable pageable) {
        Page<Venta> ventas = ventaService.findByCajeroId(cajeroId, pageable);
        return ResponseEntity.ok(ventas);
    }

    @GetMapping("/canal/{canalVentaId}")
    @Operation(summary = "Obtener ventas por canal", description = "Retorna todas las ventas de un canal específico")
    public ResponseEntity<Page<Venta>> getVentasByCanal(@PathVariable Long canalVentaId, Pageable pageable) {
        Page<Venta> ventas = ventaService.findByCanalVentaId(canalVentaId, pageable);
        return ResponseEntity.ok(ventas);
    }

    @GetMapping("/fecha")
    @Operation(summary = "Obtener ventas por rango de fechas", description = "Retorna las ventas en un rango de fechas")
    public ResponseEntity<Page<Venta>> getVentasByFecha(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin,
            Pageable pageable) {
        Page<Venta> ventas = ventaService.findByFechaBetween(fechaInicio, fechaFin, pageable);
        return ResponseEntity.ok(ventas);
    }

    @GetMapping("/cliente/{clienteId}/estado/{estado}")
    @Operation(summary = "Obtener ventas por cliente y estado", description = "Retorna las ventas de un cliente con estado específico")
    public ResponseEntity<List<Venta>> getVentasByClienteAndEstado(
            @PathVariable Long clienteId,
            @PathVariable Venta.EstadoVenta estado) {
        List<Venta> ventas = ventaService.findByClienteIdAndEstado(clienteId, estado);
        return ResponseEntity.ok(ventas);
    }

    @GetMapping("/vencidas")
    @Operation(summary = "Obtener ventas vencidas", description = "Retorna las ventas pendientes que han vencido")
    public ResponseEntity<List<Venta>> getVentasVencidas() {
        List<Venta> ventas = ventaService.findVentasVencidas(LocalDate.now());
        return ResponseEntity.ok(ventas);
    }

    @GetMapping("/estadisticas/total")
    @Operation(summary = "Obtener total de ventas pagadas", description = "Retorna el total de ventas pagadas en un rango de fechas")
    public ResponseEntity<BigDecimal> getTotalVentasPagadas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        BigDecimal total = ventaService.sumTotalVentasPagadas(fechaInicio, fechaFin);
        return ResponseEntity.ok(total != null ? total : BigDecimal.ZERO);
    }

    @GetMapping("/estadisticas/count")
    @Operation(summary = "Contar ventas pagadas", description = "Retorna el número de ventas pagadas en un rango de fechas")
    public ResponseEntity<Long> countVentasPagadas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        long count = ventaService.countVentasPagadas(fechaInicio, fechaFin);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/count/activas")
    @Operation(summary = "Contar ventas activas", description = "Retorna el número total de ventas activas")
    public ResponseEntity<Long> countVentasActivas() {
        long count = ventaService.countVentasActivas();
        return ResponseEntity.ok(count);
    }

    @PostMapping
    @Operation(summary = "Crear nueva venta", description = "Crea una nueva venta")
    public ResponseEntity<Venta> createVenta(@RequestBody Venta venta) {
        try {
            Venta savedVenta = ventaService.save(venta);
            return ResponseEntity.status(201).body(savedVenta);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar venta", description = "Actualiza una venta existente")
    public ResponseEntity<Venta> updateVenta(@PathVariable Long id, @RequestBody Venta venta) {
        if (!ventaService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        venta.setId(id);
        try {
            Venta updatedVenta = ventaService.save(venta);
            return ResponseEntity.ok(updatedVenta);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/{id}/estado")
    @Operation(summary = "Cambiar estado de venta", description = "Cambia el estado de una venta")
    public ResponseEntity<Void> cambiarEstado(@PathVariable Long id, @RequestParam Venta.EstadoVenta estado) {
        try {
            ventaService.cambiarEstado(id, estado);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Anular venta", description = "Anula una venta (cambia estado a ANULADA)")
    public ResponseEntity<Void> anularVenta(@PathVariable Long id) {
        try {
            ventaService.anularVenta(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}