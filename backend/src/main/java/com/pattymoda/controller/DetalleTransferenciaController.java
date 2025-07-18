package com.pattymoda.controller;

import com.pattymoda.entity.DetalleTransferencia;
import com.pattymoda.service.DetalleTransferenciaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/detalle-transferencia")
@Tag(name = "DetalleTransferencia", description = "API para gestión de detalles de transferencia")
public class DetalleTransferenciaController extends BaseController<DetalleTransferencia, Long> {
    @Autowired
    public DetalleTransferenciaController(DetalleTransferenciaService detalleTransferenciaService) {
        super(detalleTransferenciaService);
        this.detalleTransferenciaService = detalleTransferenciaService;
    }

    private final DetalleTransferenciaService detalleTransferenciaService;

    @GetMapping("/transferencia/{transferenciaId}")
    @Operation(summary = "Obtener detalles por transferencia", description = "Obtiene todos los detalles de una transferencia")
    public ResponseEntity<List<DetalleTransferencia>> getByTransferencia(@PathVariable Long transferenciaId) {
        return ResponseEntity.ok(detalleTransferenciaService.findByTransferenciaId(transferenciaId));
    }

    @GetMapping("/producto/{productoId}")
    @Operation(summary = "Obtener detalles por producto", description = "Obtiene todos los detalles de transferencia de un producto")
    public ResponseEntity<List<DetalleTransferencia>> getByProducto(@PathVariable Long productoId) {
        return ResponseEntity.ok(detalleTransferenciaService.findByProductoId(productoId));
    }
} 