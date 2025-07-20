package com.pattymoda.controller;

import com.pattymoda.entity.VentaFacturacion;
import com.pattymoda.service.VentaFacturacionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ventas-facturacion")
@Tag(name = "VentasFacturacion", description = "API para gestión de facturación de ventas")
public class VentaFacturacionController extends BaseController<VentaFacturacion, Long> {
    @Autowired
    public VentaFacturacionController(VentaFacturacionService ventaFacturacionService) {
        super(ventaFacturacionService);
        this.ventaFacturacionService = ventaFacturacionService;
    }

    private final VentaFacturacionService ventaFacturacionService;

    @GetMapping("/venta/{ventaId}")
    @Operation(summary = "Buscar facturación por venta", description = "Busca la facturación de una venta por su ID")
    public ResponseEntity<VentaFacturacion> getByVenta(@PathVariable Long ventaId) {
        return ventaFacturacionService.findByVentaId(ventaId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/comprobante/{numero}")
    @Operation(summary = "Buscar facturación por número de comprobante", description = "Busca la facturación por número de comprobante")
    public ResponseEntity<VentaFacturacion> getByComprobante(@PathVariable String numero) {
        return ventaFacturacionService.findByNumeroComprobante(numero)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
} 