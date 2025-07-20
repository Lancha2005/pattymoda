package com.pattymoda.controller;

import com.pattymoda.entity.MetodoPagoVenta;
import com.pattymoda.service.MetodoPagoVentaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/metodos-pago-venta")
@Tag(name = "MetodosPagoVenta", description = "API para gestión de métodos de pago de ventas")
public class MetodoPagoVentaController extends BaseController<MetodoPagoVenta, Long> {
    @Autowired
    public MetodoPagoVentaController(MetodoPagoVentaService metodoPagoVentaService) {
        super(metodoPagoVentaService);
        this.metodoPagoVentaService = metodoPagoVentaService;
    }

    private final MetodoPagoVentaService metodoPagoVentaService;

    @GetMapping("/venta/{ventaId}")
    @Operation(summary = "Buscar métodos de pago por venta", description = "Busca todos los métodos de pago de una venta por su ID")
    public ResponseEntity<List<MetodoPagoVenta>> getByVenta(@PathVariable Long ventaId) {
        return ResponseEntity.ok(metodoPagoVentaService.findByVentaId(ventaId));
    }
} 