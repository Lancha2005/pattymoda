package com.pattymoda.controller;

import com.pattymoda.entity.AuditoriaLog;
import com.pattymoda.service.AuditoriaLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auditoria-logs")
@Tag(name = "AuditoriaLogs", description = "API para gestión de logs de auditoría")
public class AuditoriaLogController extends BaseController<AuditoriaLog, Long> {
    @Autowired
    public AuditoriaLogController(AuditoriaLogService auditoriaLogService) {
        super(auditoriaLogService);
        this.auditoriaLogService = auditoriaLogService;
    }

    private final AuditoriaLogService auditoriaLogService;

    @GetMapping("/tabla/{tabla}")
    @Operation(summary = "Buscar logs por tabla", description = "Obtiene todos los logs de una tabla específica")
    public ResponseEntity<List<AuditoriaLog>> getByTabla(@PathVariable String tabla) {
        return ResponseEntity.ok(auditoriaLogService.findByTabla(tabla));
    }

    @GetMapping("/usuario/{usuarioId}")
    @Operation(summary = "Buscar logs por usuario", description = "Obtiene todos los logs de un usuario específico")
    public ResponseEntity<List<AuditoriaLog>> getByUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(auditoriaLogService.findByUsuarioId(usuarioId));
    }

    @GetMapping("/accion/{accion}")
    @Operation(summary = "Buscar logs por acción", description = "Obtiene todos los logs de una acción específica")
    public ResponseEntity<List<AuditoriaLog>> getByAccion(@PathVariable AuditoriaLog.Accion accion) {
        return ResponseEntity.ok(auditoriaLogService.findByAccion(accion));
    }
} 