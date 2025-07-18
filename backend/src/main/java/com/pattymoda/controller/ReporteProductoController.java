package com.pattymoda.controller;

import com.pattymoda.dto.VistaProductoCompletoDTO;
import com.pattymoda.service.VistaProductoCompletoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reportes/productos")
@Tag(name = "ReporteProductos", description = "Reporte de productos completo")
public class ReporteProductoController {
    @Autowired
    private VistaProductoCompletoService service;

    @GetMapping
    @Operation(summary = "Obtener productos con filtros y paginación", description = "Devuelve productos con filtros por nombre, categoría y marca")
    public Page<VistaProductoCompletoDTO> getProductos(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) Long categoriaId,
            @RequestParam(required = false) Long marcaId,
            Pageable pageable) {
        return service.findAllByFilters(nombre, categoriaId, marcaId, pageable);
    }
} 