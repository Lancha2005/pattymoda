package com.pattymoda.controller;

import com.pattymoda.entity.Producto;
import com.pattymoda.service.RecomendacionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recomendaciones")
@Tag(name = "Recomendaciones", description = "API para sistema de recomendaciones de productos")
@CrossOrigin(origins = "*")
public class RecomendacionController {

    @Autowired
    private RecomendacionService recomendacionService;

    @GetMapping("/cliente/{clienteId}")
    @Operation(summary = "Recomendaciones personalizadas", description = "Obtiene productos recomendados para un cliente específico")
    public ResponseEntity<List<Producto>> getRecomendacionesCliente(
            @PathVariable Long clienteId,
            @RequestParam(defaultValue = "8") int limite) {
        
        List<Producto> recomendaciones = recomendacionService.getRecomendacionesParaCliente(clienteId, limite);
        return ResponseEntity.ok(recomendaciones);
    }

    @GetMapping("/relacionados/{productoId}")
    @Operation(summary = "Productos relacionados", description = "Obtiene productos relacionados con el producto actual")
    public ResponseEntity<List<Producto>> getProductosRelacionados(
            @PathVariable Long productoId,
            @RequestParam(defaultValue = "6") int limite) {
        
        List<Producto> relacionados = recomendacionService.getProductosRelacionados(productoId, limite);
        return ResponseEntity.ok(relacionados);
    }

    @GetMapping("/populares")
    @Operation(summary = "Productos populares", description = "Obtiene los productos más populares/vendidos")
    public ResponseEntity<List<Producto>> getProductosPopulares(
            @RequestParam(defaultValue = "12") int limite) {
        
        List<Producto> populares = recomendacionService.getProductosPopulares(limite);
        return ResponseEntity.ok(populares);
    }

    @GetMapping("/nuevos")
    @Operation(summary = "Productos nuevos", description = "Obtiene los productos más recientes")
    public ResponseEntity<List<Producto>> getProductosNuevos(
            @RequestParam(defaultValue = "8") int limite) {
        
        List<Producto> nuevos = recomendacionService.getProductosNuevos(limite);
        return ResponseEntity.ok(nuevos);
    }

    @GetMapping("/ofertas")
    @Operation(summary = "Productos en oferta", description = "Obtiene productos con descuentos especiales")
    public ResponseEntity<List<Producto>> getProductosEnOferta(
            @RequestParam(defaultValue = "10") int limite) {
        
        List<Producto> ofertas = recomendacionService.getProductosEnOferta(limite);
        return ResponseEntity.ok(ofertas);
    }
}