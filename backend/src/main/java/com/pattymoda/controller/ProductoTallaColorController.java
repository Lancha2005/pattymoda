package com.pattymoda.controller;

import com.pattymoda.entity.ProductoTallaColor;
import com.pattymoda.service.ProductoTallaColorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/productos-talla-color")
@Tag(name = "ProductoTallaColor", description = "API para gestión de combinaciones talla-color de productos")
@CrossOrigin(origins = "*")
public class ProductoTallaColorController extends BaseController<ProductoTallaColor, Long> {

    private final ProductoTallaColorService service;

    @Autowired
    public ProductoTallaColorController(ProductoTallaColorService service) {
        super(service);
        this.service = service;
    }

    @GetMapping("/producto/{productoId}")
    @Operation(summary = "Obtener combinaciones por producto")
    public ResponseEntity<List<ProductoTallaColor>> getByProducto(@PathVariable Long productoId) {
        return ResponseEntity.ok(service.getCombinacionesByProducto(productoId));
    }

    @GetMapping("/producto/{productoId}/disponibles")
    @Operation(summary = "Obtener combinaciones disponibles por producto")
    public ResponseEntity<List<ProductoTallaColor>> getDisponiblesByProducto(@PathVariable Long productoId) {
        return ResponseEntity.ok(service.getCombinacionesDisponibles(productoId));
    }

    @GetMapping("/combinacion")
    @Operation(summary = "Obtener combinación específica")
    public ResponseEntity<ProductoTallaColor> getCombinacionEspecifica(
            @RequestParam Long productoId,
            @RequestParam Long tallaId,
            @RequestParam Long colorId) {
        return service.getCombinacionEspecifica(productoId, tallaId, colorId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/stock-bajo")
    @Operation(summary = "Obtener combinaciones con stock bajo")
    public ResponseEntity<List<ProductoTallaColor>> getStockBajo() {
        return ResponseEntity.ok(service.getCombinacionesStockBajo());
    }

    @GetMapping("/sin-stock")
    @Operation(summary = "Obtener combinaciones sin stock")
    public ResponseEntity<List<ProductoTallaColor>> getSinStock() {
        return ResponseEntity.ok(service.getCombinacionesSinStock());
    }

    @GetMapping("/producto/{productoId}/stock-total")
    @Operation(summary = "Obtener stock total del producto")
    public ResponseEntity<Integer> getStockTotal(@PathVariable Long productoId) {
        return ResponseEntity.ok(service.getStockTotalProducto(productoId));
    }

    @PutMapping("/{id}/stock")
    @Operation(summary = "Actualizar stock de combinación")
    public ResponseEntity<ProductoTallaColor> actualizarStock(
            @PathVariable Long id,
            @RequestParam Integer nuevoStock) {
        ProductoTallaColor combinacion = service.findById(id)
                .orElseThrow(() -> new RuntimeException("Combinación no encontrada"));
        
        ProductoTallaColor actualizada = service.actualizarStock(combinacion, nuevoStock);
        return ResponseEntity.ok(actualizada);
    }

    @PutMapping("/{id}/reservar")
    @Operation(summary = "Reservar stock de combinación")
    public ResponseEntity<ProductoTallaColor> reservarStock(
            @PathVariable Long id,
            @RequestParam Integer cantidad) {
        ProductoTallaColor combinacion = service.findById(id)
                .orElseThrow(() -> new RuntimeException("Combinación no encontrada"));
        
        try {
            ProductoTallaColor actualizada = service.reservarStock(combinacion, cantidad);
            return ResponseEntity.ok(actualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}/liberar")
    @Operation(summary = "Liberar stock reservado")
    public ResponseEntity<ProductoTallaColor> liberarStock(
            @PathVariable Long id,
            @RequestParam Integer cantidad) {
        ProductoTallaColor combinacion = service.findById(id)
                .orElseThrow(() -> new RuntimeException("Combinación no encontrada"));
        
        ProductoTallaColor actualizada = service.liberarStock(combinacion, cantidad);
        return ResponseEntity.ok(actualizada);
    }

    @PostMapping
    @Operation(summary = "Crear nueva combinación talla-color")
    public ResponseEntity<ProductoTallaColor> create(@RequestBody ProductoTallaColor combinacion) {
        try {
            ProductoTallaColor saved = service.save(combinacion);
            return ResponseEntity.status(201).body(saved);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}