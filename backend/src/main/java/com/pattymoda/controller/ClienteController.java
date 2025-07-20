package com.pattymoda.controller;

import com.pattymoda.entity.Cliente;
import com.pattymoda.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")
@Tag(name = "Clientes", description = "API para gestión de clientes")
@CrossOrigin(origins = "*")
public class ClienteController extends BaseController<Cliente, Long> {

    private final ClienteService clienteService;

    @Autowired
    public ClienteController(ClienteService clienteService) {
        super(clienteService);
        this.clienteService = clienteService;
    }

    @GetMapping("/codigo/{codigoCliente}")
    @Operation(summary = "Buscar cliente por código", description = "Busca un cliente específico por su código")
    public ResponseEntity<Cliente> getClienteByCodigo(@PathVariable String codigoCliente) {
        return clienteService.findByCodigoCliente(codigoCliente)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/documento/{numeroDocumento}")
    @Operation(summary = "Buscar cliente por documento", description = "Busca un cliente por su número de documento")
    public ResponseEntity<Cliente> getClienteByDocumento(@PathVariable String numeroDocumento) {
        return clienteService.findByNumeroDocumento(numeroDocumento)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/activos")
    @Operation(summary = "Obtener clientes activos", description = "Retorna todos los clientes activos")
    public ResponseEntity<Page<Cliente>> getClientesActivos(Pageable pageable) {
        Page<Cliente> clientes = clienteService.findByActivo(true, pageable);
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/buscar")
    @Operation(summary = "Buscar clientes", description = "Busca clientes por nombre, apellido, documento o código")
    public ResponseEntity<Page<Cliente>> buscarClientes(
            @RequestParam String busqueda,
            Pageable pageable) {
        Page<Cliente> clientes = clienteService.buscarClientes(busqueda, pageable);
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/documento/{tipoDocumento}/{numeroDocumento}")
    @Operation(summary = "Buscar cliente por tipo y número de documento", description = "Busca un cliente por tipo y número de documento")
    public ResponseEntity<Cliente> getClienteByTipoYDocumento(
            @PathVariable Cliente.TipoDocumento tipoDocumento,
            @PathVariable String numeroDocumento) {
        return clienteService.findByTipoDocumentoAndNumeroDocumento(tipoDocumento, numeroDocumento)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/count/activos")
    @Operation(summary = "Contar clientes activos", description = "Retorna el número total de clientes activos")
    public ResponseEntity<Long> countClientesActivos() {
        long count = clienteService.countClientesActivos();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/nuevos")
    @Operation(summary = "Obtener clientes nuevos", description = "Retorna los clientes registrados en el último mes")
    public ResponseEntity<List<Cliente>> getClientesNuevos() {
        List<Cliente> clientes = clienteService.findClientesNuevosUltimoMes();
        return ResponseEntity.ok(clientes);
    }

    @PostMapping
    @Operation(summary = "Crear nuevo cliente", description = "Crea un nuevo cliente")
    public ResponseEntity<Cliente> createCliente(@RequestBody Cliente cliente) {
        try {
            Cliente savedCliente = clienteService.save(cliente);
            return ResponseEntity.status(201).body(savedCliente);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar cliente", description = "Actualiza un cliente existente")
    public ResponseEntity<Cliente> updateCliente(@PathVariable Long id, @RequestBody Cliente cliente) {
        if (!clienteService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        cliente.setId(id);
        try {
            Cliente updatedCliente = clienteService.save(cliente);
            return ResponseEntity.ok(updatedCliente);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar cliente", description = "Elimina lógicamente un cliente (marca como inactivo)")
    public ResponseEntity<Void> deleteCliente(@PathVariable Long id) {
        if (!clienteService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        clienteService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}