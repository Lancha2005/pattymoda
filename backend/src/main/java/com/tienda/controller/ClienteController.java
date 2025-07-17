package com.tienda.controller;

import com.tienda.entity.Cliente;
import com.tienda.service.ClienteService;
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
@RequestMapping("/api/clientes")
@Tag(name = "Clientes", description = "Gestión de clientes")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
public class ClienteController {
    
    @Autowired
    private ClienteService clienteService;
    
    @GetMapping
    @Operation(summary = "Obtener todos los clientes", description = "Devuelve una lista de todos los clientes")
    public ResponseEntity<Map<String, Object>> obtenerTodosLosClientes() {
        try {
            List<Cliente> clientes = clienteService.obtenerTodosLosClientes();
            Map<String, Object> response = new HashMap<>();
            response.put("data", clientes);
            response.put("message", "Clientes obtenidos exitosamente");
            response.put("status", 200);
            response.put("timestamp", java.time.LocalDateTime.now());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("data", null);
            errorResponse.put("message", "Error al obtener clientes: " + e.getMessage());
            errorResponse.put("status", 500);
            errorResponse.put("timestamp", java.time.LocalDateTime.now());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
    
    @GetMapping("/activos")
    @Operation(summary = "Obtener clientes activos", description = "Devuelve solo los clientes activos")
    public ResponseEntity<Map<String, Object>> obtenerClientesActivos() {
        try {
            List<Cliente> clientes = clienteService.obtenerClientesActivos();
            Map<String, Object> response = new HashMap<>();
            response.put("data", clientes);
            response.put("message", "Clientes activos obtenidos exitosamente");
            response.put("status", 200);
            response.put("timestamp", java.time.LocalDateTime.now());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("data", null);
            errorResponse.put("message", "Error al obtener clientes activos: " + e.getMessage());
            errorResponse.put("status", 500);
            errorResponse.put("timestamp", java.time.LocalDateTime.now());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Obtener cliente por ID", description = "Devuelve un cliente específico por su ID")
    public ResponseEntity<Map<String, Object>> obtenerClientePorId(@PathVariable Long id) {
        try {
            return clienteService.obtenerClientePorId(id)
                    .map(cliente -> {
                        Map<String, Object> response = new HashMap<>();
                        response.put("data", cliente);
                        response.put("message", "Cliente encontrado");
                        response.put("status", 200);
                        response.put("timestamp", java.time.LocalDateTime.now());
                        return ResponseEntity.ok(response);
                    })
                    .orElseGet(() -> {
                        Map<String, Object> errorResponse = new HashMap<>();
                        errorResponse.put("data", null);
                        errorResponse.put("message", "Cliente no encontrado");
                        errorResponse.put("status", 404);
                        errorResponse.put("timestamp", java.time.LocalDateTime.now());
                        return ResponseEntity.notFound().build();
                    });
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("data", null);
            errorResponse.put("message", "Error al obtener cliente: " + e.getMessage());
            errorResponse.put("status", 500);
            errorResponse.put("timestamp", java.time.LocalDateTime.now());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
    
    @GetMapping("/buscar")
    @Operation(summary = "Buscar clientes", description = "Busca clientes por nombre")
    public ResponseEntity<Map<String, Object>> buscarClientes(@RequestParam String q) {
        try {
            List<Cliente> clientes = clienteService.buscarClientesPorNombre(q);
            Map<String, Object> response = new HashMap<>();
            response.put("data", clientes);
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
    @Operation(summary = "Crear cliente", description = "Crea un nuevo cliente")
    public ResponseEntity<Map<String, Object>> crearCliente(@Valid @RequestBody Cliente cliente) {
        try {
            Cliente nuevoCliente = clienteService.crearCliente(cliente);
            Map<String, Object> response = new HashMap<>();
            response.put("data", nuevoCliente);
            response.put("message", "Cliente creado exitosamente");
            response.put("status", 201);
            response.put("timestamp", java.time.LocalDateTime.now());
            return ResponseEntity.status(201).body(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("data", null);
            errorResponse.put("message", "Error al crear cliente: " + e.getMessage());
            errorResponse.put("status", 400);
            errorResponse.put("timestamp", java.time.LocalDateTime.now());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar cliente", description = "Actualiza un cliente existente")
    public ResponseEntity<Map<String, Object>> actualizarCliente(@PathVariable Long id, @Valid @RequestBody Cliente cliente) {
        try {
            Cliente clienteActualizado = clienteService.actualizarCliente(id, cliente);
            Map<String, Object> response = new HashMap<>();
            response.put("data", clienteActualizado);
            response.put("message", "Cliente actualizado exitosamente");
            response.put("status", 200);
            response.put("timestamp", java.time.LocalDateTime.now());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("data", null);
            errorResponse.put("message", "Error al actualizar cliente: " + e.getMessage());
            errorResponse.put("status", 400);
            errorResponse.put("timestamp", java.time.LocalDateTime.now());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar cliente", description = "Marca un cliente como inactivo")
    public ResponseEntity<Map<String, Object>> eliminarCliente(@PathVariable Long id) {
        try {
            clienteService.eliminarCliente(id);
            Map<String, Object> response = new HashMap<>();
            response.put("data", null);
            response.put("message", "Cliente eliminado exitosamente");
            response.put("status", 200);
            response.put("timestamp", java.time.LocalDateTime.now());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("data", null);
            errorResponse.put("message", "Error al eliminar cliente: " + e.getMessage());
            errorResponse.put("status", 400);
            errorResponse.put("timestamp", java.time.LocalDateTime.now());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
}