package com.pattymoda.controller;

import com.pattymoda.entity.Rol;
import com.pattymoda.service.RolService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
@Tag(name = "Roles", description = "API para gestión de roles")
@CrossOrigin(origins = "*")
public class RolController extends BaseController<Rol, Long> {

    private final RolService rolService;

    @Autowired
    public RolController(RolService rolService) {
        super(rolService);
        this.rolService = rolService;
    }

    @GetMapping("/codigo/{codigo}")
    @Operation(summary = "Buscar rol por código", description = "Busca un rol específico por su código")
    public ResponseEntity<Rol> getRolByCodigo(@PathVariable String codigo) {
        return rolService.findByCodigo(codigo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/activos")
    @Operation(summary = "Obtener roles activos", description = "Retorna todos los roles activos")
    public ResponseEntity<Page<Rol>> getRolesActivos(Pageable pageable) {
        Page<Rol> roles = rolService.findByActivo(true, pageable);
        return ResponseEntity.ok(roles);
    }

    @GetMapping("/lista")
    @Operation(summary = "Obtener lista de roles activos", description = "Retorna una lista simple de roles activos")
    public ResponseEntity<List<Rol>> getListaRolesActivos() {
        List<Rol> roles = rolService.findByActivoTrueOrderByNombre();
        return ResponseEntity.ok(roles);
    }

    @GetMapping("/count/activos")
    @Operation(summary = "Contar roles activos", description = "Retorna el número total de roles activos")
    public ResponseEntity<Long> countRolesActivos() {
        long count = rolService.countRolesActivos();
        return ResponseEntity.ok(count);
    }

    @PostMapping
    @Operation(summary = "Crear nuevo rol", description = "Crea un nuevo rol")
    public ResponseEntity<Rol> createRol(@RequestBody Rol rol) {
        try {
            Rol savedRol = rolService.save(rol);
            return ResponseEntity.status(201).body(savedRol);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar rol", description = "Actualiza un rol existente")
    public ResponseEntity<Rol> updateRol(@PathVariable Long id, @RequestBody Rol rol) {
        if (!rolService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        rol.setId(id);
        try {
            Rol updatedRol = rolService.save(rol);
            return ResponseEntity.ok(updatedRol);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar rol", description = "Elimina lógicamente un rol (marca como inactivo)")
    public ResponseEntity<Void> deleteRol(@PathVariable Long id) {
        if (!rolService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        rolService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}