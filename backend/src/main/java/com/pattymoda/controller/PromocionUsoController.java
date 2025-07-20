package com.pattymoda.controller;

import com.pattymoda.entity.PromocionUso;
import com.pattymoda.service.PromocionUsoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/promociones-uso")
@Tag(name = "PromocionesUso", description = "API para gestión de uso de promociones")
public class PromocionUsoController extends BaseController<PromocionUso, Long> {
    @Autowired
    public PromocionUsoController(PromocionUsoService promocionUsoService) {
        super(promocionUsoService);
        this.promocionUsoService = promocionUsoService;
    }

    private final PromocionUsoService promocionUsoService;

    @GetMapping("/promocion/{promocionId}")
    @Operation(summary = "Obtener usos por promoción", description = "Obtiene todos los usos de una promoción")
    public ResponseEntity<List<PromocionUso>> getByPromocion(@PathVariable Long promocionId) {
        return ResponseEntity.ok(promocionUsoService.findByPromocionId(promocionId));
    }

    @GetMapping("/cliente/{clienteId}")
    @Operation(summary = "Obtener usos por cliente", description = "Obtiene todos los usos de promociones por cliente")
    public ResponseEntity<List<PromocionUso>> getByCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(promocionUsoService.findByClienteId(clienteId));
    }

    @GetMapping("/venta/{ventaId}")
    @Operation(summary = "Obtener usos por venta", description = "Obtiene todos los usos de promociones por venta")
    public ResponseEntity<List<PromocionUso>> getByVenta(@PathVariable Long ventaId) {
        return ResponseEntity.ok(promocionUsoService.findByVentaId(ventaId));
    }
} 