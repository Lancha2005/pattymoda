package com.pattymoda.controller;

import com.pattymoda.entity.Color;
import com.pattymoda.service.ColorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/colores")
@Tag(name = "Colores", description = "API para gestión de colores")
public class ColorController extends BaseController<Color, Long> {
    @Autowired
    public ColorController(ColorService colorService) {
        super(colorService);
        this.colorService = colorService;
    }

    private final ColorService colorService;
    
    @GetMapping("/activos")
    @Operation(summary = "Obtener todos los colores activos", description = "Retorna una lista de todos los colores activos ordenados por orden de visualización")
    public ResponseEntity<List<Color>> getColoresActivos() {
        List<Color> colores = colorService.findAllActivos();
        return ResponseEntity.ok(colores);
    }
    
    @GetMapping("/familia/{familia}")
    @Operation(summary = "Obtener colores por familia", description = "Retorna los colores activos de una familia específica")
    public ResponseEntity<List<Color>> getColoresByFamilia(@PathVariable String familia) {
        List<Color> colores = colorService.findByFamiliaColor(familia);
        return ResponseEntity.ok(colores);
    }
    
    @GetMapping("/codigo/{codigo}")
    @Operation(summary = "Buscar color por código", description = "Busca un color específico por su código")
    public ResponseEntity<Color> getColorByCodigo(@PathVariable String codigo) {
        return colorService.findByCodigo(codigo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/hex/{codigoHex}")
    @Operation(summary = "Buscar color por código hexadecimal", description = "Busca un color específico por su código hexadecimal")
    public ResponseEntity<Color> getColorByCodigoHex(@PathVariable String codigoHex) {
        return colorService.findByCodigoHex(codigoHex)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/count/activos")
    @Operation(summary = "Contar colores activos", description = "Retorna el número total de colores activos")
    public ResponseEntity<Long> countColoresActivos() {
        long count = colorService.countActivos();
        return ResponseEntity.ok(count);
    }
    
    @PostMapping
    @Operation(summary = "Crear nuevo color", description = "Crea un nuevo color con validaciones")
    public ResponseEntity<Color> createColor(@RequestBody Color color) {
        try {
            Color savedColor = colorService.save(color);
            return ResponseEntity.ok(savedColor);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar color", description = "Actualiza un color existente")
    public ResponseEntity<Color> updateColor(@PathVariable Long id, @RequestBody Color color) {
        if (!colorService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        
        color.setId(id);
        try {
            Color updatedColor = colorService.save(color);
            return ResponseEntity.ok(updatedColor);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar color", description = "Elimina lógicamente un color (marca como inactivo)")
    public ResponseEntity<Void> deleteColor(@PathVariable Long id) {
        if (!colorService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        
        colorService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
} 