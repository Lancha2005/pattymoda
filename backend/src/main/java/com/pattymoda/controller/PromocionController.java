package com.pattymoda.controller;

import com.pattymoda.entity.Promocion;
import com.pattymoda.service.PromocionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/promociones")
@Tag(name = "Promociones", description = "API para gestión de promociones")
public class PromocionController extends BaseController<Promocion, Long> {
    @Autowired
    public PromocionController(PromocionService promocionService) {
        super(promocionService);
        this.promocionService = promocionService;
    }

    private final PromocionService promocionService;

    @GetMapping("/codigo/{codigo}")
    @Operation(summary = "Buscar promoción por código", description = "Busca una promoción por su código")
    public ResponseEntity<Promocion> getByCodigo(@PathVariable String codigo) {
        return promocionService.findByCodigo(codigo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/activas")
    @Operation(summary = "Obtener promociones activas", description = "Obtiene todas las promociones activas")
    public ResponseEntity<List<Promocion>> getActivas() {
        return ResponseEntity.ok(promocionService.findActivas());
    }
} 