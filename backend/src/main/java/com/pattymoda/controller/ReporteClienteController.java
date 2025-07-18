package com.pattymoda.controller;

import com.pattymoda.dto.VistaClienteEstadisticaDTO;
import com.pattymoda.service.VistaClienteEstadisticaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reportes/clientes")
@Tag(name = "ReporteClientes", description = "Reporte de clientes con estadísticas avanzadas")
public class ReporteClienteController {
    @Autowired
    private VistaClienteEstadisticaService service;

    @GetMapping
    @Operation(summary = "Obtener clientes con filtros y paginación", description = "Devuelve clientes con filtros por nombre, documento y tipo de cliente")
    public Page<VistaClienteEstadisticaDTO> getClientes(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String documento,
            @RequestParam(required = false) String tipoCliente,
            Pageable pageable) {
        return service.findAllByFilters(nombre, documento, tipoCliente, pageable);
    }
} 