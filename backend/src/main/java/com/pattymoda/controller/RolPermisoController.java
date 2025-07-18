package com.pattymoda.controller;

import com.pattymoda.entity.RolPermiso;
import com.pattymoda.service.RolPermisoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles-permisos")
@Tag(name = "RolesPermisos", description = "API para gestión de permisos por rol")
public class RolPermisoController extends BaseController<RolPermiso, Long> {
    @Autowired
    public RolPermisoController(RolPermisoService rolPermisoService) {
        super(rolPermisoService);
        this.rolPermisoService = rolPermisoService;
    }

    private final RolPermisoService rolPermisoService;

    @GetMapping("/rol/{rolId}")
    @Operation(summary = "Obtener permisos por rol", description = "Obtiene todos los permisos asignados a un rol")
    public ResponseEntity<List<RolPermiso>> getByRol(@PathVariable Long rolId) {
        return ResponseEntity.ok(rolPermisoService.findByRolId(rolId));
    }

    @GetMapping("/permiso/{permisoId}")
    @Operation(summary = "Obtener roles por permiso", description = "Obtiene todos los roles que tienen un permiso específico")
    public ResponseEntity<List<RolPermiso>> getByPermiso(@PathVariable Long permisoId) {
        return ResponseEntity.ok(rolPermisoService.findByPermisoId(permisoId));
    }
} 