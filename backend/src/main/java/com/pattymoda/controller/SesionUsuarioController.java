package com.pattymoda.controller;

import com.pattymoda.entity.SesionUsuario;
import com.pattymoda.service.SesionUsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sesiones-usuario")
@Tag(name = "SesionesUsuario", description = "API para gestión de sesiones de usuario")
public class SesionUsuarioController extends BaseController<SesionUsuario, Long> {
    @Autowired
    public SesionUsuarioController(SesionUsuarioService sesionUsuarioService) {
        super(sesionUsuarioService);
        this.sesionUsuarioService = sesionUsuarioService;
    }

    private final SesionUsuarioService sesionUsuarioService;

    @GetMapping("/usuario/{usuarioId}")
    @Operation(summary = "Obtener sesiones por usuario", description = "Obtiene todas las sesiones de un usuario")
    public ResponseEntity<List<SesionUsuario>> getByUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(sesionUsuarioService.findByUsuarioId(usuarioId));
    }

    @GetMapping("/token/{token}")
    @Operation(summary = "Obtener sesión por token", description = "Obtiene la sesión por token de sesión")
    public ResponseEntity<SesionUsuario> getByToken(@PathVariable String token) {
        return sesionUsuarioService.findByTokenSesion(token)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
} 