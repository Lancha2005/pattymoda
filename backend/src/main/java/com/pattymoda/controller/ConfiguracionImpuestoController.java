package com.pattymoda.controller;

import com.pattymoda.entity.ConfiguracionImpuesto;
import com.pattymoda.service.ConfiguracionImpuestoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/configuracion-impuestos")
@Tag(name = "ConfiguracionImpuestos", description = "API para gestión de configuración de impuestos")
public class ConfiguracionImpuestoController extends BaseController<ConfiguracionImpuesto, Long> {
    @Autowired
    public ConfiguracionImpuestoController(ConfiguracionImpuestoService configuracionImpuestoService) {
        super(configuracionImpuestoService);
        this.configuracionImpuestoService = configuracionImpuestoService;
    }

    private final ConfiguracionImpuestoService configuracionImpuestoService;

    @GetMapping("/codigo/{codigo}")
    @Operation(summary = "Buscar impuesto por código", description = "Busca un impuesto por su código")
    public ResponseEntity<ConfiguracionImpuesto> getByCodigo(@PathVariable String codigo) {
        return configuracionImpuestoService.findByCodigo(codigo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/activos")
    @Operation(summary = "Obtener impuestos activos", description = "Obtiene todos los impuestos activos")
    public ResponseEntity<List<ConfiguracionImpuesto>> getActivos() {
        return ResponseEntity.ok(configuracionImpuestoService.findActivos());
    }
} 