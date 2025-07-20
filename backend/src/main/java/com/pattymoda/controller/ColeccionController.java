package com.pattymoda.controller;

import com.pattymoda.entity.Coleccion;
import com.pattymoda.service.ColeccionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/colecciones")
@Tag(name = "Colecciones", description = "API para gestión de colecciones de moda")
@CrossOrigin(origins = "*")
public class ColeccionController extends BaseController<Coleccion, Long> {

    private final ColeccionService coleccionService;

    @Autowired
    public ColeccionController(ColeccionService coleccionService) {
        super(coleccionService);
        this.coleccionService = coleccionService;
    }

    @GetMapping("/codigo/{codigo}")
    @Operation(summary = "Buscar colección por código")
    public ResponseEntity<Coleccion> getByCodigo(@PathVariable String codigo) {
        return coleccionService.findByCodigo(codigo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/slug/{slug}")
    @Operation(summary = "Buscar colección por slug")
    public ResponseEntity<Coleccion> getBySlug(@PathVariable String slug) {
        return coleccionService.findBySlug(slug)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/activas")
    @Operation(summary = "Obtener colecciones activas paginadas")
    public ResponseEntity<Page<Coleccion>> getColeccionesActivas(Pageable pageable) {
        return ResponseEntity.ok(coleccionService.getColeccionesActivas(pageable));
    }

    @GetMapping("/destacadas")
    @Operation(summary = "Obtener colecciones destacadas")
    public ResponseEntity<List<Coleccion>> getColeccionesDestacadas() {
        return ResponseEntity.ok(coleccionService.getColeccionesDestacadas());
    }

    @GetMapping("/temporada/{temporadaId}")
    @Operation(summary = "Obtener colecciones por temporada")
    public ResponseEntity<List<Coleccion>> getByTemporada(@PathVariable Long temporadaId) {
        return ResponseEntity.ok(coleccionService.getColeccionesPorTemporada(temporadaId));
    }

    @GetMapping("/disponibles")
    @Operation(summary = "Obtener colecciones disponibles actualmente")
    public ResponseEntity<List<Coleccion>> getColeccionesDisponibles() {
        return ResponseEntity.ok(coleccionService.getColeccionesDisponibles());
    }

    @GetMapping("/tipo/{tipo}")
    @Operation(summary = "Obtener colecciones por tipo")
    public ResponseEntity<List<Coleccion>> getByTipo(@PathVariable Coleccion.TipoColeccion tipo) {
        return ResponseEntity.ok(coleccionService.getColeccionesPorTipo(tipo));
    }

    @GetMapping("/buscar")
    @Operation(summary = "Buscar colecciones")
    public ResponseEntity<Page<Coleccion>> buscar(@RequestParam String busqueda, Pageable pageable) {
        return ResponseEntity.ok(coleccionService.buscarColecciones(busqueda, pageable));
    }

    @PostMapping
    @Operation(summary = "Crear nueva colección")
    public ResponseEntity<Coleccion> create(@RequestBody Coleccion coleccion) {
        try {
            Coleccion saved = coleccionService.save(coleccion);
            return ResponseEntity.status(201).body(saved);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar colección")
    public ResponseEntity<Coleccion> update(@PathVariable Long id, @RequestBody Coleccion coleccion) {
        if (!coleccionService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        coleccion.setId(id);
        try {
            Coleccion updated = coleccionService.save(coleccion);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}