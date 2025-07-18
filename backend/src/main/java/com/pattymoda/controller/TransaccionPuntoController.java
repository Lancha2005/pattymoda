package com.pattymoda.controller;

import com.pattymoda.entity.TransaccionPunto;
import com.pattymoda.service.TransaccionPuntoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transacciones-puntos")
@Tag(name = "TransaccionesPuntos", description = "API para gestión de transacciones de puntos")
public class TransaccionPuntoController extends BaseController<TransaccionPunto, Long> {
    @Autowired
    public TransaccionPuntoController(TransaccionPuntoService transaccionPuntoService) {
        super(transaccionPuntoService);
        this.transaccionPuntoService = transaccionPuntoService;
    }

    private final TransaccionPuntoService transaccionPuntoService;

    @GetMapping("/cliente/{clienteId}")
    @Operation(summary = "Obtener transacciones por cliente", description = "Obtiene todas las transacciones de puntos de un cliente")
    public ResponseEntity<List<TransaccionPunto>> getByCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(transaccionPuntoService.findByClienteId(clienteId));
    }

    @GetMapping("/venta/{ventaId}")
    @Operation(summary = "Obtener transacciones por venta", description = "Obtiene todas las transacciones de puntos asociadas a una venta")
    public ResponseEntity<List<TransaccionPunto>> getByVenta(@PathVariable Long ventaId) {
        return ResponseEntity.ok(transaccionPuntoService.findByVentaId(ventaId));
    }
} 