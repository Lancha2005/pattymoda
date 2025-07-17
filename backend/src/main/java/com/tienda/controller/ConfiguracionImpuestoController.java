package com.tienda.controller;

import com.tienda.entity.ConfiguracionImpuesto;
import com.tienda.service.ConfiguracionImpuestoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/impuestos")
@Tag(name = "Configuración de Impuestos", description = "Gestión de impuestos y IGV")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
public class ConfiguracionImpuestoController {
    
    @Autowired
    private ConfiguracionImpuestoService configuracionImpuestoService;
    
    @GetMapping
    @Operation(summary = "Obtener todos los impuestos", description = "Devuelve la configuración de todos los impuestos")
    public ResponseEntity<Map<String, Object>> obtenerTodosLosImpuestos() {
        try {
            List<ConfiguracionImpuesto> impuestos = configuracionImpuestoService.obtenerTodosLosImpuestos();
            Map<String, Object> response = new HashMap<>();
            response.put("data", impuestos);
            response.put("message", "Impuestos obtenidos exitosamente");
            response.put("status", 200);
            response.put("timestamp", java.time.LocalDateTime.now());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("data", null);
            errorResponse.put("message", "Error al obtener impuestos: " + e.getMessage());
            errorResponse.put("status", 500);
            errorResponse.put("timestamp", java.time.LocalDateTime.now());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
    
    @GetMapping("/activos")
    @Operation(summary = "Obtener impuestos activos", description = "Devuelve solo los impuestos activos")
    public ResponseEntity<Map<String, Object>> obtenerImpuestosActivos() {
        try {
            List<ConfiguracionImpuesto> impuestos = configuracionImpuestoService.obtenerImpuestosActivos();
            Map<String, Object> response = new HashMap<>();
            response.put("data", impuestos);
            response.put("message", "Impuestos activos obtenidos exitosamente");
            response.put("status", 200);
            response.put("timestamp", java.time.LocalDateTime.now());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("data", null);
            errorResponse.put("message", "Error al obtener impuestos activos: " + e.getMessage());
            errorResponse.put("status", 500);
            errorResponse.put("timestamp", java.time.LocalDateTime.now());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
    
    @GetMapping("/igv")
    @Operation(summary = "Obtener configuración del IGV", description = "Devuelve la configuración actual del IGV")
    public ResponseEntity<Map<String, Object>> obtenerConfiguracionIGV() {
        try {
            var igv = configuracionImpuestoService.obtenerIGV();
            Map<String, Object> response = new HashMap<>();
            response.put("data", igv.orElse(null));
            response.put("message", igv.isPresent() ? "Configuración IGV obtenida" : "IGV no configurado");
            response.put("status", 200);
            response.put("timestamp", java.time.LocalDateTime.now());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("data", null);
            errorResponse.put("message", "Error al obtener configuración IGV: " + e.getMessage());
            errorResponse.put("status", 500);
            errorResponse.put("timestamp", java.time.LocalDateTime.now());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
    
    @PostMapping
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('ADMIN')")
    @Operation(summary = "Crear impuesto", description = "Crea una nueva configuración de impuesto")
    public ResponseEntity<Map<String, Object>> crearImpuesto(@Valid @RequestBody ConfiguracionImpuesto impuesto) {
        try {
            ConfiguracionImpuesto nuevoImpuesto = configuracionImpuestoService.crearImpuesto(impuesto);
            Map<String, Object> response = new HashMap<>();
            response.put("data", nuevoImpuesto);
            response.put("message", "Impuesto creado exitosamente");
            response.put("status", 201);
            response.put("timestamp", java.time.LocalDateTime.now());
            return ResponseEntity.status(201).body(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("data", null);
            errorResponse.put("message", "Error al crear impuesto: " + e.getMessage());
            errorResponse.put("status", 400);
            errorResponse.put("timestamp", java.time.LocalDateTime.now());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('ADMIN')")
    @Operation(summary = "Actualizar impuesto", description = "Actualiza una configuración de impuesto existente")
    public ResponseEntity<Map<String, Object>> actualizarImpuesto(@PathVariable Long id, @Valid @RequestBody ConfiguracionImpuesto impuesto) {
        try {
            ConfiguracionImpuesto impuestoActualizado = configuracionImpuestoService.actualizarImpuesto(id, impuesto);
            Map<String, Object> response = new HashMap<>();
            response.put("data", impuestoActualizado);
            response.put("message", "Impuesto actualizado exitosamente");
            response.put("status", 200);
            response.put("timestamp", java.time.LocalDateTime.now());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("data", null);
            errorResponse.put("message", "Error al actualizar impuesto: " + e.getMessage());
            errorResponse.put("status", 400);
            errorResponse.put("timestamp", java.time.LocalDateTime.now());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    
    @PutMapping("/igv/activar")
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('ADMIN')")
    @Operation(summary = "Activar/Desactivar IGV", description = "Activa o desactiva el IGV")
    public ResponseEntity<Map<String, Object>> activarDesactivarIGV(@RequestBody Map<String, Boolean> request) {
        try {
            Boolean activar = request.get("activar");
            configuracionImpuestoService.activarDesactivarIGV(activar);
            Map<String, Object> response = new HashMap<>();
            response.put("data", null);
            response.put("message", "IGV " + (activar ? "activado" : "desactivado") + " exitosamente");
            response.put("status", 200);
            response.put("timestamp", java.time.LocalDateTime.now());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("data", null);
            errorResponse.put("message", "Error al cambiar estado del IGV: " + e.getMessage());
            errorResponse.put("status", 400);
            errorResponse.put("timestamp", java.time.LocalDateTime.now());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    
    @PutMapping("/igv/porcentaje")
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('ADMIN')")
    @Operation(summary = "Actualizar porcentaje IGV", description = "Actualiza el porcentaje del IGV")
    public ResponseEntity<Map<String, Object>> actualizarPorcentajeIGV(@RequestBody Map<String, BigDecimal> request) {
        try {
            BigDecimal porcentaje = request.get("porcentaje");
            configuracionImpuestoService.actualizarPorcentajeIGV(porcentaje);
            Map<String, Object> response = new HashMap<>();
            response.put("data", null);
            response.put("message", "Porcentaje de IGV actualizado a " + porcentaje + "%");
            response.put("status", 200);
            response.put("timestamp", java.time.LocalDateTime.now());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("data", null);
            errorResponse.put("message", "Error al actualizar porcentaje IGV: " + e.getMessage());
            errorResponse.put("status", 400);
            errorResponse.put("timestamp", java.time.LocalDateTime.now());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @Operation(summary = "Eliminar impuesto", description = "Desactiva una configuración de impuesto")
    public ResponseEntity<Map<String, Object>> eliminarImpuesto(@PathVariable Long id) {
        try {
            configuracionImpuestoService.eliminarImpuesto(id);
            Map<String, Object> response = new HashMap<>();
            response.put("data", null);
            response.put("message", "Impuesto eliminado exitosamente");
            response.put("status", 200);
            response.put("timestamp", java.time.LocalDateTime.now());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("data", null);
            errorResponse.put("message", "Error al eliminar impuesto: " + e.getMessage());
            errorResponse.put("status", 400);
            errorResponse.put("timestamp", java.time.LocalDateTime.now());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
}