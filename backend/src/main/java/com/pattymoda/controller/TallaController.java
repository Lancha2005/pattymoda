package com.pattymoda.controller;

import com.pattymoda.entity.Talla;
import com.pattymoda.service.TallaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tallas")
@Tag(name = "Tallas", description = "API para gestión de tallas")
public class TallaController extends BaseController<Talla, Long> {
    @Autowired
    public TallaController(TallaService tallaService) {
        super(tallaService);
        this.tallaService = tallaService;
    }

    private final TallaService tallaService;
    
    @GetMapping("/activas")
    @Operation(summary = "Obtener todas las tallas activas", description = "Retorna una lista de todas las tallas activas ordenadas por orden de visualización")
    public ResponseEntity<List<Talla>> getTallasActivas() {
        List<Talla> tallas = tallaService.findAllActivas();
        return ResponseEntity.ok(tallas);
    }
    
    @GetMapping("/categoria/{categoria}")
    @Operation(summary = "Obtener tallas por categoría", description = "Retorna las tallas activas de una categoría específica")
    public ResponseEntity<List<Talla>> getTallasByCategoria(@PathVariable Talla.CategoriaTalla categoria) {
        List<Talla> tallas = tallaService.findByCategoria(categoria);
        return ResponseEntity.ok(tallas);
    }
    
    @GetMapping("/codigo/{codigo}")
    @Operation(summary = "Buscar talla por código", description = "Busca una talla específica por su código")
    public ResponseEntity<Talla> getTallaByCodigo(@PathVariable String codigo) {
        return tallaService.findByCodigo(codigo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/count/activas")
    @Operation(summary = "Contar tallas activas", description = "Retorna el número total de tallas activas")
    public ResponseEntity<Long> countTallasActivas() {
        long count = tallaService.countActivas();
        return ResponseEntity.ok(count);
    }
    
    @PostMapping
    @Operation(summary = "Crear nueva talla", description = "Crea una nueva talla con validaciones")
    public ResponseEntity<Talla> createTalla(@RequestBody Talla talla) {
        try {
            Talla savedTalla = tallaService.save(talla);
            return ResponseEntity.ok(savedTalla);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar talla", description = "Actualiza una talla existente")
    public ResponseEntity<Talla> updateTalla(@PathVariable Long id, @RequestBody Talla talla) {
        if (!tallaService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        
        talla.setId(id);
        try {
            Talla updatedTalla = tallaService.save(talla);
            return ResponseEntity.ok(updatedTalla);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar talla", description = "Elimina lógicamente una talla (marca como inactiva)")
    public ResponseEntity<Void> deleteTalla(@PathVariable Long id) {
        if (!tallaService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        
        tallaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
} 