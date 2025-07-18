package com.pattymoda.controller;

import com.pattymoda.dto.VistaVentaCompletaDTO;
import com.pattymoda.service.VistaVentaCompletaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reportes/ventas")
@Tag(name = "ReporteVentas", description = "Reporte de ventas completo")
public class ReporteVentaController {
    @Autowired
    private VistaVentaCompletaService service;

    @GetMapping
    @Operation(summary = "Obtener ventas con filtros y paginación", description = "Devuelve ventas con filtros por número, cliente y estado")
    public Page<VistaVentaCompletaDTO> getVentas(
            @RequestParam(required = false) String numeroVenta,
            @RequestParam(required = false) String clienteNombre,
            @RequestParam(required = false) String estado,
            Pageable pageable) {
        return service.findAllByFilters(numeroVenta, clienteNombre, estado, pageable);
    }
} 