package com.pattymoda.controller;

import com.pattymoda.entity.Marca;
import com.pattymoda.service.MarcaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/marcas")
@Tag(name = "Marcas", description = "API para gestión de marcas")
@CrossOrigin(origins = "*")
public class MarcaController extends BaseController<Marca, Long> {

    private final MarcaService marcaService;

    @Autowired
    public MarcaController(MarcaService marcaService) {
        super(marcaService);
        this.marcaService = marcaService;
    }

    @GetMapping("/nombre/{nombre}")
    @Operation(summary = "Buscar marca por nombre", description = "Busca una marca específica por su nombre")
    public ResponseEntity<Marca> getMarcaByNombre(@PathVariable String nombre) {
        return marcaService.findByNombre(nombre)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/activas")
    @Operation(summary = "Obtener marcas activas", description = "Retorna todas las marcas activas")
    public ResponseEntity<Page<Marca>> getMarcasActivas(Pageable pageable) {
        Page<Marca> marcas = marcaService.findByActivo(true, pageable);
        return ResponseEntity.ok(marcas);
    }

    @GetMapping("/lista")
    @Operation(summary = "Obtener lista de marcas activas", description = "Retorna una lista simple de marcas activas ordenadas por nombre")
    public ResponseEntity<List<Marca>> getListaMarcasActivas() {
        List<Marca> marcas = marcaService.findByActivoTrueOrderByNombre();
        return ResponseEntity.ok(marcas);
    }

    @GetMapping("/buscar")
    @Operation(summary = "Buscar marcas", description = "Busca marcas por nombre")
    public ResponseEntity<Page<Marca>> buscarMarcas(
            @RequestParam String busqueda,
            Pageable pageable) {
        Page<Marca> marcas = marcaService.buscarMarcas(busqueda, pageable);
        return ResponseEntity.ok(marcas);
    }

    @GetMapping("/count/activas")
    @Operation(summary = "Contar marcas activas", description = "Retorna el número total de marcas activas")
    public ResponseEntity<Long> countMarcasActivas() {
        long count = marcaService.countMarcasActivas();
        return ResponseEntity.ok(count);
    }

    @PostMapping
    @Operation(summary = "Crear nueva marca", description = "Crea una nueva marca")
    public ResponseEntity<Marca> createMarca(@RequestBody Marca marca) {
        try {
            Marca savedMarca = marcaService.save(marca);
            return ResponseEntity.status(201).body(savedMarca);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar marca", description = "Actualiza una marca existente")
    public ResponseEntity<Marca> updateMarca(@PathVariable Long id, @RequestBody Marca marca) {
        if (!marcaService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        marca.setId(id);
        try {
            Marca updatedMarca = marcaService.save(marca);
            return ResponseEntity.ok(updatedMarca);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar marca", description = "Elimina lógicamente una marca (marca como inactiva)")
    public ResponseEntity<Void> deleteMarca(@PathVariable Long id) {
        if (!marcaService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        marcaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}