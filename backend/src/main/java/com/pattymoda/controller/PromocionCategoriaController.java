package com.pattymoda.controller;

import com.pattymoda.entity.PromocionCategoria;
import com.pattymoda.service.PromocionCategoriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/promociones-categorias")
@Tag(name = "PromocionesCategorias", description = "API para gestión de categorías de promociones")
public class PromocionCategoriaController extends BaseController<PromocionCategoria, Long> {
    @Autowired
    public PromocionCategoriaController(PromocionCategoriaService promocionCategoriaService) {
        super(promocionCategoriaService);
        this.promocionCategoriaService = promocionCategoriaService;
    }

    private final PromocionCategoriaService promocionCategoriaService;

    @GetMapping("/promocion/{promocionId}")
    @Operation(summary = "Obtener categorías por promoción", description = "Obtiene todas las categorías asociadas a una promoción")
    public ResponseEntity<List<PromocionCategoria>> getByPromocion(@PathVariable Long promocionId) {
        return ResponseEntity.ok(promocionCategoriaService.findByPromocionId(promocionId));
    }

    @GetMapping("/categoria/{categoriaId}")
    @Operation(summary = "Obtener promociones por categoría", description = "Obtiene todas las promociones asociadas a una categoría")
    public ResponseEntity<List<PromocionCategoria>> getByCategoria(@PathVariable Long categoriaId) {
        return ResponseEntity.ok(promocionCategoriaService.findByCategoriaId(categoriaId));
    }
} 