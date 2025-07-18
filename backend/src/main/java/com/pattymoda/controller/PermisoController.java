package com.pattymoda.controller;

import com.pattymoda.entity.Permiso;
import com.pattymoda.service.PermisoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/permisos")
@Tag(name = "Permisos", description = "API para gestión de permisos")
public class PermisoController extends BaseController<Permiso, Long> {
    @Autowired
    public PermisoController(PermisoService permisoService) {
        super(permisoService);
        this.permisoService = permisoService;
    }

    private final PermisoService permisoService;

    @GetMapping("/codigo/{codigo}")
    @Operation(summary = "Buscar permiso por código", description = "Busca un permiso por su código")
    public ResponseEntity<Permiso> getByCodigo(@PathVariable String codigo) {
        return permisoService.findByCodigo(codigo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/activos")
    @Operation(summary = "Obtener permisos activos", description = "Obtiene todos los permisos activos")
    public ResponseEntity<List<Permiso>> getActivos() {
        return ResponseEntity.ok(permisoService.findActivos());
    }
} 