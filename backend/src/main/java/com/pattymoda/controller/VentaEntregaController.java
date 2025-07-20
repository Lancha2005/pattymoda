package com.pattymoda.controller;

import com.pattymoda.entity.VentaEntrega;
import com.pattymoda.service.VentaEntregaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ventas-entrega")
@Tag(name = "VentasEntrega", description = "API para gestión de entregas de ventas")
public class VentaEntregaController extends BaseController<VentaEntrega, Long> {
    @Autowired
    public VentaEntregaController(VentaEntregaService ventaEntregaService) {
        super(ventaEntregaService);
        this.ventaEntregaService = ventaEntregaService;
    }

    private final VentaEntregaService ventaEntregaService;

    @GetMapping("/venta/{ventaId}")
    @Operation(summary = "Buscar entrega por venta", description = "Busca la entrega de una venta por su ID")
    public ResponseEntity<VentaEntrega> getByVenta(@PathVariable Long ventaId) {
        return ventaEntregaService.findByVentaId(ventaId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
} 