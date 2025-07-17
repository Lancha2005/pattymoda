package com.tienda.controller;

import com.tienda.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/dashboard")
@Tag(name = "Dashboard", description = "Estadísticas del dashboard")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
public class DashboardController {
    
    @Autowired
    private DashboardService dashboardService;
    
    @GetMapping("/stats")
    @Operation(summary = "Obtener estadísticas del dashboard", description = "Devuelve las estadísticas principales del sistema")
    public ResponseEntity<Map<String, Object>> getDashboardStats() {
        try {
            Map<String, Object> stats = dashboardService.getDashboardStats();
            Map<String, Object> response = new HashMap<>();
            response.put("data", stats);
            response.put("message", "Estadísticas obtenidas exitosamente");
            response.put("status", 200);
            response.put("timestamp", java.time.LocalDateTime.now());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("data", null);
            errorResponse.put("message", "Error al obtener estadísticas: " + e.getMessage());
            errorResponse.put("status", 500);
            errorResponse.put("timestamp", java.time.LocalDateTime.now());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
    
    @GetMapping("/recent-activity")
    @Operation(summary = "Obtener actividad reciente", description = "Devuelve la actividad reciente del sistema")
    public ResponseEntity<Map<String, Object>> getRecentActivity() {
        try {
            Map<String, Object> activity = dashboardService.getRecentActivity();
            Map<String, Object> response = new HashMap<>();
            response.put("data", activity);
            response.put("message", "Actividad reciente obtenida exitosamente");
            response.put("status", 200);
            response.put("timestamp", java.time.LocalDateTime.now());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("data", null);
            errorResponse.put("message", "Error al obtener actividad reciente: " + e.getMessage());
            errorResponse.put("status", 500);
            errorResponse.put("timestamp", java.time.LocalDateTime.now());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
}