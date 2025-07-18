package com.pattymoda.controller;

import com.pattymoda.entity.TareaSeguimiento;
import com.pattymoda.service.TareaSeguimientoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tareas-seguimiento")
@Tag(name = "TareasSeguimiento", description = "API para gestión de tareas de seguimiento")
public class TareaSeguimientoController extends BaseController<TareaSeguimiento, Long> {
    @Autowired
    public TareaSeguimientoController(TareaSeguimientoService tareaSeguimientoService) {
        super(tareaSeguimientoService);
        this.tareaSeguimientoService = tareaSeguimientoService;
    }

    private final TareaSeguimientoService tareaSeguimientoService;

    @GetMapping("/usuario/{usuarioId}")
    @Operation(summary = "Buscar tareas por usuario asignado", description = "Busca todas las tareas asignadas a un usuario")
    public ResponseEntity<List<TareaSeguimiento>> getByUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(tareaSeguimientoService.findByUsuarioAsignadoId(usuarioId));
    }

    @GetMapping("/estado/{estado}")
    @Operation(summary = "Buscar tareas por estado", description = "Busca todas las tareas por estado")
    public ResponseEntity<List<TareaSeguimiento>> getByEstado(@PathVariable TareaSeguimiento.EstadoTarea estado) {
        return ResponseEntity.ok(tareaSeguimientoService.findByEstado(estado));
    }

    @GetMapping("/prioridad/{prioridad}")
    @Operation(summary = "Buscar tareas por prioridad", description = "Busca todas las tareas por prioridad")
    public ResponseEntity<List<TareaSeguimiento>> getByPrioridad(@PathVariable TareaSeguimiento.PrioridadTarea prioridad) {
        return ResponseEntity.ok(tareaSeguimientoService.findByPrioridad(prioridad));
    }

    @GetMapping("/cliente/{clienteId}")
    @Operation(summary = "Buscar tareas por cliente", description = "Busca todas las tareas asociadas a un cliente")
    public ResponseEntity<List<TareaSeguimiento>> getByCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(tareaSeguimientoService.findByClienteId(clienteId));
    }

    @GetMapping("/venta/{ventaId}")
    @Operation(summary = "Buscar tareas por venta", description = "Busca todas las tareas asociadas a una venta")
    public ResponseEntity<List<TareaSeguimiento>> getByVenta(@PathVariable Long ventaId) {
        return ResponseEntity.ok(tareaSeguimientoService.findByVentaId(ventaId));
    }

    @GetMapping("/cotizacion/{cotizacionId}")
    @Operation(summary = "Buscar tareas por cotización", description = "Busca todas las tareas asociadas a una cotización")
    public ResponseEntity<List<TareaSeguimiento>> getByCotizacion(@PathVariable Long cotizacionId) {
        return ResponseEntity.ok(tareaSeguimientoService.findByCotizacionId(cotizacionId));
    }
} 