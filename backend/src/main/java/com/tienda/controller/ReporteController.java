package com.tienda.controller;

import com.tienda.dto.ReporteVentas;
import com.tienda.service.ReporteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/reportes")
@Tag(name = "Reportes", description = "Reportes y estadísticas")
public class ReporteController {
    
    @Autowired
    private ReporteService reporteService;
    
    @GetMapping("/ventas")
    @Operation(summary = "Reporte de ventas", description = "Genera un reporte de ventas por fecha")
    public ResponseEntity<ReporteVentas> obtenerReporteVentas(
            @RequestParam String fechaInicio,
            @RequestParam String fechaFin) {
        try {
            LocalDate inicio = LocalDate.parse(fechaInicio);
            LocalDate fin = LocalDate.parse(fechaFin);
            ReporteVentas reporte = reporteService.obtenerReporteVentasPorFecha(inicio, fin);
            return ResponseEntity.ok(reporte);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/productos-mas-vendidos")
    @Operation(summary = "Productos más vendidos", description = "Devuelve los productos más vendidos")
    public ResponseEntity<Map<String, Object>> obtenerProductosMasVendidos() {
        Map<String, Object> productos = reporteService.obtenerProductosMasVendidos();
        return ResponseEntity.ok(productos);
    }
    
    @GetMapping("/estadisticas-generales")
    @Operation(summary = "Estadísticas generales", description = "Devuelve estadísticas generales del sistema")
    public ResponseEntity<Map<String, Object>> obtenerEstadisticasGenerales() {
        Map<String, Object> estadisticas = reporteService.obtenerEstadisticasGenerales();
        return ResponseEntity.ok(estadisticas);
    }
}