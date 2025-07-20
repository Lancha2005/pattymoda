package com.pattymoda.controller;

import com.pattymoda.service.ReporteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/reportes/dashboard")
@Tag(name = "Dashboard", description = "API para reportes del dashboard principal")
@CrossOrigin(origins = "*")
public class ReporteDashboardController {

    @Autowired
    private ReporteService reporteService;

    @GetMapping
    @Operation(summary = "Obtener datos del dashboard", description = "Retorna las métricas principales para el dashboard")
    public ResponseEntity<Map<String, Object>> getDashboard() {
        Map<String, Object> dashboard = reporteService.getReporteDashboard();
        return ResponseEntity.ok(dashboard);
    }

    @GetMapping("/ventas-periodo")
    @Operation(summary = "Reporte de ventas por período", description = "Obtiene las ventas en un rango de fechas específico")
    public ResponseEntity<Map<String, Object>> getVentasPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        Map<String, Object> reporte = reporteService.getReporteVentasPorPeriodo(inicio, fin);
        return ResponseEntity.ok(reporte);
    }

    @GetMapping("/productos-mas-vendidos")
    @Operation(summary = "Productos más vendidos", description = "Obtiene los productos más vendidos")
    public ResponseEntity<List<Map<String, Object>>> getProductosMasVendidos(
            @RequestParam(defaultValue = "10") int limite) {
        List<Map<String, Object>> productos = reporteService.getProductosMasVendidos(limite);
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/clientes-top")
    @Operation(summary = "Mejores clientes", description = "Obtiene los clientes que más compran")
    public ResponseEntity<List<Map<String, Object>>> getClientesTop(
            @RequestParam(defaultValue = "10") int limite) {
        List<Map<String, Object>> clientes = reporteService.getClientesTopCompradores(limite);
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/inventario")
    @Operation(summary = "Reporte de inventario", description = "Obtiene el estado actual del inventario")
    public ResponseEntity<Map<String, Object>> getReporteInventario() {
        Map<String, Object> reporte = reporteService.getReporteInventario();
        return ResponseEntity.ok(reporte);
    }
}