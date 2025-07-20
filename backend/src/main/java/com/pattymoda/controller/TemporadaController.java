package com.pattymoda.controller;

import com.pattymoda.entity.Temporada;
import com.pattymoda.service.TemporadaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/temporadas")
@Tag(name = "Temporadas", description = "API para gestión de temporadas de moda")
@CrossOrigin(origins = "*")
public class TemporadaController extends BaseController<Temporada, Long> {

    private final TemporadaService temporadaService;

    @Autowired
    public TemporadaController(TemporadaService temporadaService) {
        super(temporadaService);
        this.temporadaService = temporadaService;
    }

    @GetMapping("/codigo/{codigo}")
    @Operation(summary = "Buscar temporada por código")
    public ResponseEntity<Temporada> getByCodigo(@PathVariable String codigo) {
        return temporadaService.findByCodigo(codigo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/actual")
    @Operation(summary = "Obtener temporada actual")
    public ResponseEntity<Temporada> getTemporadaActual() {
        return temporadaService.getTemporadaActual()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/activas")
    @Operation(summary = "Obtener temporadas activas")
    public ResponseEntity<List<Temporada>> getTemporadasActivas() {
        return ResponseEntity.ok(temporadaService.getTemporadasActivas());
    }

    @GetMapping("/liquidacion")
    @Operation(summary = "Obtener temporadas en liquidación")
    public ResponseEntity<List<Temporada>> getTemporadasEnLiquidacion() {
        return ResponseEntity.ok(temporadaService.getTemporadasEnLiquidacion());
    }

    @GetMapping("/tipo/{tipo}")
    @Operation(summary = "Obtener temporadas por tipo")
    public ResponseEntity<List<Temporada>> getByTipo(@PathVariable Temporada.TipoTemporada tipo) {
        return ResponseEntity.ok(temporadaService.getTemporadasPorTipo(tipo));
    }

    @PostMapping("/{id}/establecer-actual")
    @Operation(summary = "Establecer como temporada actual")
    public ResponseEntity<Void> establecerTemporadaActual(@PathVariable Long id) {
        temporadaService.establecerTemporadaActual(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping
    @Operation(summary = "Crear nueva temporada")
    public ResponseEntity<Temporada> create(@RequestBody Temporada temporada) {
        try {
            Temporada saved = temporadaService.save(temporada);
            return ResponseEntity.status(201).body(saved);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar temporada")
    public ResponseEntity<Temporada> update(@PathVariable Long id, @RequestBody Temporada temporada) {
        if (!temporadaService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        temporada.setId(id);
        try {
            Temporada updated = temporadaService.save(temporada);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}