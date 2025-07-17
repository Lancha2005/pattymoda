package com.tienda.controller;

import com.tienda.entity.Producto;
import com.tienda.entity.ProductoTalla;
import com.tienda.entity.ProductoColor;
import com.tienda.entity.ProductoImagen;
import com.tienda.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/productos")
@Tag(name = "Productos", description = "Gestión de productos")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
public class ProductoController {
    
    @Autowired
    private ProductoService productoService;
    
    @GetMapping
    @Operation(summary = "Obtener todos los productos", description = "Devuelve una lista de todos los productos")
    public ResponseEntity<Map<String, Object>> obtenerTodosLosProductos() {
        try {
            List<Producto> productos = productoService.obtenerTodosLosProductos();
            Map<String, Object> response = new HashMap<>();
            response.put("data", productos);
            response.put("message", "Productos obtenidos exitosamente");
            response.put("status", 200);
            response.put("timestamp", java.time.LocalDateTime.now());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("data", null);
            errorResponse.put("message", "Error al obtener productos: " + e.getMessage());
            errorResponse.put("status", 500);
            errorResponse.put("timestamp", java.time.LocalDateTime.now());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
    
    @GetMapping("/activos")
    @Operation(summary = "Obtener productos activos", description = "Devuelve solo los productos activos")
    public ResponseEntity<Map<String, Object>> obtenerProductosActivos() {
        try {
            List<Producto> productos = productoService.obtenerProductosActivos();
            Map<String, Object> response = new HashMap<>();
            response.put("data", productos);
            response.put("message", "Productos activos obtenidos exitosamente");
            response.put("status", 200);
            response.put("timestamp", java.time.LocalDateTime.now());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("data", null);
            errorResponse.put("message", "Error al obtener productos activos: " + e.getMessage());
            errorResponse.put("status", 500);
            errorResponse.put("timestamp", java.time.LocalDateTime.now());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Obtener producto por ID", description = "Devuelve un producto específico por su ID")
    public ResponseEntity<Map<String, Object>> obtenerProductoPorId(@PathVariable Long id) {
        try {
            return productoService.obtenerProductoPorId(id)
                    .map(producto -> {
                        Map<String, Object> response = new HashMap<>();
                        response.put("data", producto);
                        response.put("message", "Producto encontrado");
                        response.put("status", 200);
                        response.put("timestamp", java.time.LocalDateTime.now());
                        return ResponseEntity.ok(response);
                    })
                    .orElseGet(() -> {
                        Map<String, Object> errorResponse = new HashMap<>();
                        errorResponse.put("data", null);
                        errorResponse.put("message", "Producto no encontrado");
                        errorResponse.put("status", 404);
                        errorResponse.put("timestamp", java.time.LocalDateTime.now());
                        return ResponseEntity.notFound().build();
                    });
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("data", null);
            errorResponse.put("message", "Error al obtener producto: " + e.getMessage());
            errorResponse.put("status", 500);
            errorResponse.put("timestamp", java.time.LocalDateTime.now());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
    
    @GetMapping("/categoria/{categoriaId}")
    @Operation(summary = "Obtener productos por categoría", description = "Devuelve productos de una categoría específica")
    public ResponseEntity<Map<String, Object>> obtenerProductosPorCategoria(@PathVariable Long categoriaId) {
        try {
            List<Producto> productos = productoService.obtenerProductosPorCategoria(categoriaId);
            Map<String, Object> response = new HashMap<>();
            response.put("data", productos);
            response.put("message", "Productos por categoría obtenidos exitosamente");
            response.put("status", 200);
            response.put("timestamp", java.time.LocalDateTime.now());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("data", null);
            errorResponse.put("message", "Error al obtener productos por categoría: " + e.getMessage());
            errorResponse.put("status", 500);
            errorResponse.put("timestamp", java.time.LocalDateTime.now());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
    
    @GetMapping("/buscar")
    @Operation(summary = "Buscar productos", description = "Busca productos por nombre o SKU")
    public ResponseEntity<Map<String, Object>> buscarProductos(@RequestParam String q) {
        try {
            List<Producto> productos = productoService.buscarProductos(q);
            Map<String, Object> response = new HashMap<>();
            response.put("data", productos);
            response.put("message", "Búsqueda completada exitosamente");
            response.put("status", 200);
            response.put("timestamp", java.time.LocalDateTime.now());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("data", null);
            errorResponse.put("message", "Error en la búsqueda: " + e.getMessage());
            errorResponse.put("status", 500);
            errorResponse.put("timestamp", java.time.LocalDateTime.now());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
    
    @PostMapping
    @Operation(summary = "Crear producto", description = "Crea un nuevo producto")
    public ResponseEntity<Map<String, Object>> crearProducto(@Valid @RequestBody Producto producto) {
        try {
            Producto nuevoProducto = productoService.crearProducto(producto);
            Map<String, Object> response = new HashMap<>();
            response.put("data", nuevoProducto);
            response.put("message", "Producto creado exitosamente");
            response.put("status", 201);
            response.put("timestamp", java.time.LocalDateTime.now());
            return ResponseEntity.status(201).body(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("data", null);
            errorResponse.put("message", "Error al crear producto: " + e.getMessage());
            errorResponse.put("status", 400);
            errorResponse.put("timestamp", java.time.LocalDateTime.now());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar producto", description = "Actualiza un producto existente")
    public ResponseEntity<Map<String, Object>> actualizarProducto(@PathVariable Long id, @Valid @RequestBody Producto producto) {
        try {
            Producto productoActualizado = productoService.actualizarProducto(id, producto);
            Map<String, Object> response = new HashMap<>();
            response.put("data", productoActualizado);
            response.put("message", "Producto actualizado exitosamente");
            response.put("status", 200);
            response.put("timestamp", java.time.LocalDateTime.now());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("data", null);
            errorResponse.put("message", "Error al actualizar producto: " + e.getMessage());
            errorResponse.put("status", 400);
            errorResponse.put("timestamp", java.time.LocalDateTime.now());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar producto", description = "Marca un producto como inactivo")
    public ResponseEntity<Map<String, Object>> eliminarProducto(@PathVariable Long id) {
        try {
            productoService.eliminarProducto(id);
            Map<String, Object> response = new HashMap<>();
            response.put("data", null);
            response.put("message", "Producto eliminado exitosamente");
            response.put("status", 200);
            response.put("timestamp", java.time.LocalDateTime.now());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("data", null);
            errorResponse.put("message", "Error al eliminar producto: " + e.getMessage());
            errorResponse.put("status", 400);
            errorResponse.put("timestamp", java.time.LocalDateTime.now());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    
    @GetMapping("/stock-bajo")
    @Operation(summary = "Productos con stock bajo", description = "Devuelve productos con stock menor al mínimo")
    public ResponseEntity<Map<String, Object>> obtenerProductosConStockBajo(@RequestParam(defaultValue = "0") Integer stockMinimo) {
        try {
            List<Producto> productos = stockMinimo > 0 ? 
                productoService.obtenerProductosConStockBajo(stockMinimo) : 
                productoService.obtenerProductosConStockBajo();
            
            Map<String, Object> response = new HashMap<>();
            response.put("data", productos);
            response.put("message", "Productos con stock bajo obtenidos exitosamente");
            response.put("status", 200);
            response.put("timestamp", java.time.LocalDateTime.now());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("data", null);
            errorResponse.put("message", "Error al obtener productos con stock bajo: " + e.getMessage());
            errorResponse.put("status", 500);
            errorResponse.put("timestamp", java.time.LocalDateTime.now());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
    
    // Endpoints para tallas
    @GetMapping("/{id}/tallas")
    @Operation(summary = "Obtener tallas de producto", description = "Devuelve las tallas disponibles de un producto")
    public ResponseEntity<Map<String, Object>> obtenerTallasProducto(@PathVariable Long id) {
        try {
            List<ProductoTalla> tallas = productoService.obtenerTallasProducto(id);
            Map<String, Object> response = new HashMap<>();
            response.put("data", tallas);
            response.put("message", "Tallas obtenidas exitosamente");
            response.put("status", 200);
            response.put("timestamp", java.time.LocalDateTime.now());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("data", null);
            errorResponse.put("message", "Error al obtener tallas: " + e.getMessage());
            errorResponse.put("status", 500);
            errorResponse.put("timestamp", java.time.LocalDateTime.now());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
    
    // Endpoints para colores
    @GetMapping("/{id}/colores")
    @Operation(summary = "Obtener colores de producto", description = "Devuelve los colores disponibles de un producto")
    public ResponseEntity<Map<String, Object>> obtenerColoresProducto(@PathVariable Long id) {
        try {
            List<ProductoColor> colores = productoService.obtenerColoresProducto(id);
            Map<String, Object> response = new HashMap<>();
            response.put("data", colores);
            response.put("message", "Colores obtenidos exitosamente");
            response.put("status", 200);
            response.put("timestamp", java.time.LocalDateTime.now());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("data", null);
            errorResponse.put("message", "Error al obtener colores: " + e.getMessage());
            errorResponse.put("status", 500);
            errorResponse.put("timestamp", java.time.LocalDateTime.now());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
}