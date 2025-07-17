package com.tienda.controller;

import com.tienda.entity.Usuario;
import com.tienda.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/usuarios")
@Tag(name = "Usuarios", description = "Gestión de usuarios del sistema")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
public class UsuarioController {
    
    @Autowired
    private UsuarioService usuarioService;
    
    @GetMapping
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('ADMIN')")
    @Operation(summary = "Obtener todos los usuarios", description = "Devuelve una lista de todos los usuarios")
    public ResponseEntity<Map<String, Object>> obtenerTodosLosUsuarios() {
        try {
            List<Usuario> usuarios = usuarioService.obtenerTodosLosUsuarios();
            Map<String, Object> response = new HashMap<>();
            response.put("data", usuarios);
            response.put("message", "Usuarios obtenidos exitosamente");
            response.put("status", 200);
            response.put("timestamp", java.time.LocalDateTime.now());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("data", null);
            errorResponse.put("message", "Error al obtener usuarios: " + e.getMessage());
            errorResponse.put("status", 500);
            errorResponse.put("timestamp", java.time.LocalDateTime.now());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('ADMIN')")
    @Operation(summary = "Obtener usuario por ID", description = "Devuelve un usuario específico por su ID")
    public ResponseEntity<Map<String, Object>> obtenerUsuarioPorId(@PathVariable Long id) {
        try {
            return usuarioService.obtenerUsuarioPorId(id)
                    .map(usuario -> {
                        Map<String, Object> response = new HashMap<>();
                        response.put("data", usuario);
                        response.put("message", "Usuario encontrado");
                        response.put("status", 200);
                        response.put("timestamp", java.time.LocalDateTime.now());
                        return ResponseEntity.ok(response);
                    })
                    .orElseGet(() -> {
                        Map<String, Object> errorResponse = new HashMap<>();
                        errorResponse.put("data", null);
                        errorResponse.put("message", "Usuario no encontrado");
                        errorResponse.put("status", 404);
                        errorResponse.put("timestamp", java.time.LocalDateTime.now());
                        return ResponseEntity.notFound().build();
                    });
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("data", null);
            errorResponse.put("message", "Error al obtener usuario: " + e.getMessage());
            errorResponse.put("status", 500);
            errorResponse.put("timestamp", java.time.LocalDateTime.now());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
    
    @PostMapping
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('ADMIN')")
    @Operation(summary = "Crear usuario", description = "Crea un nuevo usuario")
    public ResponseEntity<Map<String, Object>> crearUsuario(@Valid @RequestBody Usuario usuario) {
        try {
            Usuario nuevoUsuario = usuarioService.crearUsuario(usuario);
            Map<String, Object> response = new HashMap<>();
            response.put("data", nuevoUsuario);
            response.put("message", "Usuario creado exitosamente");
            response.put("status", 201);
            response.put("timestamp", java.time.LocalDateTime.now());
            return ResponseEntity.status(201).body(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("data", null);
            errorResponse.put("message", "Error al crear usuario: " + e.getMessage());
            errorResponse.put("status", 400);
            errorResponse.put("timestamp", java.time.LocalDateTime.now());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('ADMIN')")
    @Operation(summary = "Actualizar usuario", description = "Actualiza un usuario existente")
    public ResponseEntity<Map<String, Object>> actualizarUsuario(@PathVariable Long id, @Valid @RequestBody Usuario usuario) {
        try {
            Usuario usuarioActualizado = usuarioService.actualizarUsuario(id, usuario);
            Map<String, Object> response = new HashMap<>();
            response.put("data", usuarioActualizado);
            response.put("message", "Usuario actualizado exitosamente");
            response.put("status", 200);
            response.put("timestamp", java.time.LocalDateTime.now());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("data", null);
            errorResponse.put("message", "Error al actualizar usuario: " + e.getMessage());
            errorResponse.put("status", 400);
            errorResponse.put("timestamp", java.time.LocalDateTime.now());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @Operation(summary = "Eliminar usuario", description = "Desactiva un usuario")
    public ResponseEntity<Map<String, Object>> eliminarUsuario(@PathVariable Long id) {
        try {
            usuarioService.eliminarUsuario(id);
            Map<String, Object> response = new HashMap<>();
            response.put("data", null);
            response.put("message", "Usuario eliminado exitosamente");
            response.put("status", 200);
            response.put("timestamp", java.time.LocalDateTime.now());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("data", null);
            errorResponse.put("message", "Error al eliminar usuario: " + e.getMessage());
            errorResponse.put("status", 400);
            errorResponse.put("timestamp", java.time.LocalDateTime.now());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    
    @GetMapping("/roles")
    @Operation(summary = "Obtener roles disponibles", description = "Devuelve todos los roles del sistema")
    public ResponseEntity<Map<String, Object>> obtenerRoles() {
        try {
            Usuario.Rol[] roles = Usuario.Rol.values();
            Map<String, Object> rolesData = new HashMap<>();
            for (Usuario.Rol rol : roles) {
                Map<String, Object> rolInfo = new HashMap<>();
                rolInfo.put("name", rol.name());
                rolInfo.put("displayName", rol.getDisplayName());
                rolInfo.put("permissions", rol.getPermissions());
                rolesData.put(rol.name(), rolInfo);
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("data", rolesData);
            response.put("message", "Roles obtenidos exitosamente");
            response.put("status", 200);
            response.put("timestamp", java.time.LocalDateTime.now());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("data", null);
            errorResponse.put("message", "Error al obtener roles: " + e.getMessage());
            errorResponse.put("status", 500);
            errorResponse.put("timestamp", java.time.LocalDateTime.now());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
}