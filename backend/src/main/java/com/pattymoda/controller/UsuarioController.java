package com.pattymoda.controller;

import com.pattymoda.entity.Usuario;
import com.pattymoda.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@Tag(name = "Usuarios", description = "API para gestión de usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController extends BaseController<Usuario, Long> {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        super(usuarioService);
        this.usuarioService = usuarioService;
    }

    @Operation(summary = "Obtener todos los usuarios")
    @GetMapping
    public ResponseEntity<List<Usuario>> getAllUsuarios() {
        return super.findAll();
    }

    @Operation(summary = "Obtener usuarios paginados")
    @GetMapping("/page")
    public ResponseEntity<Page<Usuario>> getUsuariosPaginados(Pageable pageable) {
        return super.findAll(pageable);
    }

    @Operation(summary = "Obtener usuario por ID")
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> getUsuarioById(@PathVariable Long id) {
        return super.findById(id);
    }

    @Operation(summary = "Buscar usuario por email")
    @GetMapping("/email/{email}")
    public ResponseEntity<Usuario> getUsuarioByEmail(@PathVariable String email) {
        return usuarioService.findByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Buscar usuario por código de empleado")
    @GetMapping("/codigo/{codigoEmpleado}")
    public ResponseEntity<Usuario> getUsuarioByCodigoEmpleado(@PathVariable String codigoEmpleado) {
        return usuarioService.findByCodigoEmpleado(codigoEmpleado)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Buscar usuarios activos")
    @GetMapping("/activos")
    public ResponseEntity<Page<Usuario>> getUsuariosActivos(
            @RequestParam(defaultValue = "true") Boolean activo,
            Pageable pageable) {
        Page<Usuario> usuarios = usuarioService.findByActivo(activo, pageable);
        return ResponseEntity.ok(usuarios);
    }

    @Operation(summary = "Buscar usuarios por rol")
    @GetMapping("/rol/{rolId}")
    public ResponseEntity<Page<Usuario>> getUsuariosByRol(
            @PathVariable Long rolId,
            Pageable pageable) {
        Page<Usuario> usuarios = usuarioService.findByRolId(rolId, pageable);
        return ResponseEntity.ok(usuarios);
    }

    @Operation(summary = "Buscar usuarios por código de rol")
    @GetMapping("/rol-codigo/{rolCodigo}")
    public ResponseEntity<List<Usuario>> getUsuariosByRolCodigo(@PathVariable String rolCodigo) {
        List<Usuario> usuarios = usuarioService.findByRolCodigo(rolCodigo);
        return ResponseEntity.ok(usuarios);
    }

    @Operation(summary = "Buscar usuarios")
    @GetMapping("/buscar")
    public ResponseEntity<Page<Usuario>> buscarUsuarios(
            @RequestParam String busqueda,
            Pageable pageable) {
        Page<Usuario> usuarios = usuarioService.buscarUsuariosActivos(busqueda, pageable);
        return ResponseEntity.ok(usuarios);
    }

    @Operation(summary = "Crear nuevo usuario")
    @PostMapping
    public ResponseEntity<Usuario> createUsuario(@RequestBody Usuario usuario) {
        try {
            Usuario nuevoUsuario = usuarioService.crearUsuario(usuario);
            return ResponseEntity.status(201).body(nuevoUsuario);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Actualizar usuario")
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> updateUsuario(@PathVariable Long id, @RequestBody Usuario usuario) {
        try {
            Usuario usuarioActualizado = usuarioService.actualizarUsuario(id, usuario);
            return ResponseEntity.ok(usuarioActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Cambiar estado de usuario")
    @PatchMapping("/{id}/estado")
    public ResponseEntity<Void> cambiarEstado(@PathVariable Long id, @RequestParam Boolean activo) {
        try {
            usuarioService.cambiarEstado(id, activo);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Eliminar usuario")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsuario(@PathVariable Long id) {
        return super.delete(id);
    }

    @Operation(summary = "Contar usuarios activos")
    @GetMapping("/count/activos")
    public ResponseEntity<Long> countUsuariosActivos() {
        long count = usuarioService.countUsuariosActivos();
        return ResponseEntity.ok(count);
    }

    @Operation(summary = "Verificar si existe email")
    @GetMapping("/exists/email/{email}")
    public ResponseEntity<Boolean> existsByEmail(@PathVariable String email) {
        boolean exists = usuarioService.existsByEmail(email);
        return ResponseEntity.ok(exists);
    }

    @Operation(summary = "Verificar si existe código de empleado")
    @GetMapping("/exists/codigo/{codigoEmpleado}")
    public ResponseEntity<Boolean> existsByCodigoEmpleado(@PathVariable String codigoEmpleado) {
        boolean exists = usuarioService.existsByCodigoEmpleado(codigoEmpleado);
        return ResponseEntity.ok(exists);
    }
} 