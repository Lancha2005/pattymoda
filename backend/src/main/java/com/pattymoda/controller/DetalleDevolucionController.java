package com.pattymoda.controller;

import com.pattymoda.entity.DetalleDevolucion;
import com.pattymoda.service.DetalleDevolucionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/detalle-devolucion")
@Tag(name = "DetalleDevolucion", description = "API para gestión de detalles de devolución")
public class DetalleDevolucionController extends BaseController<DetalleDevolucion, Long> {
    @Autowired
    public DetalleDevolucionController(DetalleDevolucionService detalleDevolucionService) {
        super(detalleDevolucionService);
        this.detalleDevolucionService = detalleDevolucionService;
    }

    private final DetalleDevolucionService detalleDevolucionService;

    @GetMapping("/devolucion/{devolucionId}")
    @Operation(summary = "Obtener detalles por devolución", description = "Obtiene todos los detalles de una devolución")
    public ResponseEntity<List<DetalleDevolucion>> getByDevolucion(@PathVariable Long devolucionId) {
        return ResponseEntity.ok(detalleDevolucionService.findByDevolucionId(devolucionId));
    }

    @GetMapping("/detalle-venta/{detalleVentaId}")
    @Operation(summary = "Obtener detalles por detalle de venta", description = "Obtiene todos los detalles de devolución de un detalle de venta")
    public ResponseEntity<List<DetalleDevolucion>> getByDetalleVenta(@PathVariable Long detalleVentaId) {
        return ResponseEntity.ok(detalleDevolucionService.findByDetalleVentaId(detalleVentaId));
    }
} 