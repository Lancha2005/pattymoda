package com.pattymoda.controller;

import com.pattymoda.entity.VentaDescuento;
import com.pattymoda.service.VentaDescuentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ventas-descuentos")
@Tag(name = "VentasDescuentos", description = "API para gestión de descuentos de ventas")
public class VentaDescuentoController extends BaseController<VentaDescuento, Long> {
    @Autowired
    public VentaDescuentoController(VentaDescuentoService ventaDescuentoService) {
        super(ventaDescuentoService);
        this.ventaDescuentoService = ventaDescuentoService;
    }

    private final VentaDescuentoService ventaDescuentoService;

    @GetMapping("/venta/{ventaId}")
    @Operation(summary = "Buscar descuentos por venta", description = "Busca todos los descuentos de una venta por su ID")
    public ResponseEntity<List<VentaDescuento>> getByVenta(@PathVariable Long ventaId) {
        return ResponseEntity.ok(ventaDescuentoService.findByVentaId(ventaId));
    }
} 