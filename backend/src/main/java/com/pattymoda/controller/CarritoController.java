package com.pattymoda.controller;

import com.pattymoda.service.CarritoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/carrito")
@Tag(name = "Carrito", description = "API para gestión del carrito de compras")
@CrossOrigin(origins = "*")
public class CarritoController {

    @Autowired
    private CarritoService carritoService;

    @PostMapping("/agregar")
    @Operation(summary = "Agregar producto al carrito", description = "Agrega un producto con talla y color específicos al carrito")
    public ResponseEntity<Void> agregarProducto(
            @RequestParam Long clienteId,
            @RequestParam Long productoId,
            @RequestParam Integer cantidad,
            @RequestParam(required = false) Long tallaId,
            @RequestParam(required = false) Long colorId) {
        
        carritoService.agregarProducto(clienteId, productoId, cantidad, tallaId, colorId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/remover")
    @Operation(summary = "Remover producto del carrito", description = "Remueve un producto específico del carrito")
    public ResponseEntity<Void> removerProducto(
            @RequestParam Long clienteId,
            @RequestParam Long productoId,
            @RequestParam(required = false) Long tallaId,
            @RequestParam(required = false) Long colorId) {
        
        carritoService.removerProducto(clienteId, productoId, tallaId, colorId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{clienteId}")
    @Operation(summary = "Obtener carrito", description = "Obtiene todos los productos en el carrito del cliente")
    public ResponseEntity<Map<Object, Object>> obtenerCarrito(@PathVariable Long clienteId) {
        Map<Object, Object> carrito = carritoService.obtenerCarrito(clienteId);
        return ResponseEntity.ok(carrito);
    }

    @PutMapping("/actualizar")
    @Operation(summary = "Actualizar cantidad", description = "Actualiza la cantidad de un producto en el carrito")
    public ResponseEntity<Void> actualizarCantidad(
            @RequestParam Long clienteId,
            @RequestParam Long productoId,
            @RequestParam Integer cantidad,
            @RequestParam(required = false) Long tallaId,
            @RequestParam(required = false) Long colorId) {
        
        carritoService.actualizarCantidad(clienteId, productoId, cantidad, tallaId, colorId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/limpiar/{clienteId}")
    @Operation(summary = "Limpiar carrito", description = "Elimina todos los productos del carrito")
    public ResponseEntity<Void> limpiarCarrito(@PathVariable Long clienteId) {
        carritoService.limpiarCarrito(clienteId);
        return ResponseEntity.ok().build();
    }
}