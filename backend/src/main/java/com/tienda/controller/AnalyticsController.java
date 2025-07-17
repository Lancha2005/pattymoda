package com.tienda.controller;

import com.tienda.service.AnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/analytics")
@Tag(name = "Analytics", description = "Análisis avanzado de datos")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
public class AnalyticsController {
    
    @Autowired
    private AnalyticsService analyticsService;
    
    @GetMapping("/kpi")
    @Operation(summary = "Obtener KPIs principales", description = "Devuelve los indicadores clave de rendimiento")
    public ResponseEntity<Map<String, Object>> getKPIs() {
        try {
            Map<String, Object> kpis = analyticsService.getKPIs();
            Map<String, Object> response = new HashMap<>();
            response.put("data", kpis);
            response.put("message", "KPIs obtenidos exitosamente");
            response.put("status", 200);
            response.put("timestamp", java.time.LocalDateTime.now());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("data", null);
            errorResponse.put("message", "Error al obtener KPIs: " + e.getMessage());
            errorResponse.put("status", 500);
            errorResponse.put("timestamp", java.time.LocalDateTime.now());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
    
    @GetMapping("/customer-segments")
    @Operation(summary = "Obtener segmentación de clientes", description = "Devuelve la segmentación de clientes")
    public ResponseEntity<Map<String, Object>> getCustomerSegments() {
        try {
            Map<String, Object> segments = analyticsService.getCustomerSegments();
            Map<String, Object> response = new HashMap<>();
            response.put("data", segments);
            response.put("message", "Segmentación obtenida exitosamente");
            response.put("status", 200);
            response.put("timestamp", java.time.LocalDateTime.now());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("data", null);
            errorResponse.put("message", "Error al obtener segmentación: " + e.getMessage());
            errorResponse.put("status", 500);
            errorResponse.put("timestamp", java.time.LocalDateTime.now());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
    
    @GetMapping("/sales-trends")
    @Operation(summary = "Obtener tendencias de ventas", description = "Devuelve las tendencias de ventas por período")
    public ResponseEntity<Map<String, Object>> getSalesTrends(@RequestParam(defaultValue = "30") int days) {
        try {
            Map<String, Object> trends = analyticsService.getSalesTrends(days);
            Map<String, Object> response = new HashMap<>();
            response.put("data", trends);
            response.put("message", "Tendencias obtenidas exitosamente");
            response.put("status", 200);
            response.put("timestamp", java.time.LocalDateTime.now());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("data", null);
            errorResponse.put("message", "Error al obtener tendencias: " + e.getMessage());
            errorResponse.put("status", 500);
            errorResponse.put("timestamp", java.time.LocalDateTime.now());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
}