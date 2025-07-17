package com.tienda.controller;

import com.tienda.entity.ConfiguracionTienda;
import com.tienda.service.ConfiguracionTiendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/configuracion")
@Tag(name = "Configuración", description = "Configuración de la tienda")
public class ConfiguracionController {
    
    @Autowired
    private ConfiguracionTiendaService configuracionTiendaService;
    
    @GetMapping
    @Operation(summary = "Obtener configuración", description = "Devuelve la configuración actual de la tienda")
    public ResponseEntity<ConfiguracionTienda> obtenerConfiguracion() {
        return configuracionTiendaService.obtenerConfiguracion()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PutMapping
    @Operation(summary = "Actualizar configuración", description = "Actualiza la configuración de la tienda")
    public ResponseEntity<ConfiguracionTienda> actualizarConfiguracion(@Valid @RequestBody ConfiguracionTienda configuracion) {
        try {
            ConfiguracionTienda configuracionActualizada = configuracionTiendaService.crearOActualizarConfiguracion(configuracion);
            return ResponseEntity.ok(configuracionActualizada);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}