package com.pattymoda.controller;

import com.pattymoda.entity.Devolucion;
import com.pattymoda.service.DevolucionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/devoluciones")
@Tag(name = "Devoluciones", description = "API para gestión de devoluciones")
public class DevolucionController extends BaseController<Devolucion, Long> {
    @Autowired
    public DevolucionController(DevolucionService devolucionService) {
        super(devolucionService);
        this.devolucionService = devolucionService;
    }

    private final DevolucionService devolucionService;

    @GetMapping("/numero/{numero}")
    @Operation(summary = "Buscar devolución por número", description = "Busca una devolución por su número")
    public ResponseEntity<Devolucion> getByNumero(@PathVariable String numero) {
        return devolucionService.findByNumeroDevolucion(numero)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/cliente/{clienteId}")
    @Operation(summary = "Buscar devoluciones por cliente", description = "Busca todas las devoluciones de un cliente")
    public ResponseEntity<List<Devolucion>> getByCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(devolucionService.findByClienteId(clienteId));
    }

    @GetMapping("/venta/{ventaId}")
    @Operation(summary = "Buscar devoluciones por venta", description = "Busca todas las devoluciones de una venta")
    public ResponseEntity<List<Devolucion>> getByVenta(@PathVariable Long ventaId) {
        return ResponseEntity.ok(devolucionService.findByVentaId(ventaId));
    }
} 