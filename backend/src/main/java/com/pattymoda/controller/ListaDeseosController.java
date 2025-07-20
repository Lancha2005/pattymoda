package com.pattymoda.controller;

import com.pattymoda.entity.ListaDeseos;
import com.pattymoda.service.ListaDeseosService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/lista-deseos")
@Tag(name = "Lista de Deseos", description = "API para gestión de lista de deseos de clientes")
@CrossOrigin(origins = "*")
public class ListaDeseosController extends BaseController<ListaDeseos, Long> {

    private final ListaDeseosService listaDeseosService;

    @Autowired
    public ListaDeseosController(ListaDeseosService listaDeseosService) {
        super(listaDeseosService);
        this.listaDeseosService = listaDeseosService;
    }

    @GetMapping("/cliente/{clienteId}")
    @Operation(summary = "Obtener lista de deseos del cliente")
    public ResponseEntity<List<ListaDeseos>> getByCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(listaDeseosService.getListaDeseosCliente(clienteId));
    }

    @GetMapping("/cliente/{clienteId}/paginado")
    @Operation(summary = "Obtener lista de deseos paginada")
    public ResponseEntity<Page<ListaDeseos>> getByClientePaginado(@PathVariable Long clienteId, Pageable pageable) {
        return ResponseEntity.ok(listaDeseosService.getListaDeseosClientePaginada(clienteId, pageable));
    }

    @PostMapping("/agregar")
    @Operation(summary = "Agregar producto a lista de deseos")
    public ResponseEntity<ListaDeseos> agregar(@RequestBody Map<String, Object> request) {
        Long clienteId = Long.valueOf(request.get("clienteId").toString());
        Long productoId = Long.valueOf(request.get("productoId").toString());
        
        try {
            ListaDeseos item = listaDeseosService.agregarProducto(clienteId, productoId);
            return ResponseEntity.status(201).body(item);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/remover")
    @Operation(summary = "Remover producto de lista de deseos")
    public ResponseEntity<Void> remover(@RequestParam Long clienteId, @RequestParam Long productoId) {
        listaDeseosService.removerProducto(clienteId, productoId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/productos-mas-deseados")
    @Operation(summary = "Obtener productos más deseados")
    public ResponseEntity<List<Map<String, Object>>> getProductosMasDeseados(
            @RequestParam(defaultValue = "10") int limite) {
        return ResponseEntity.ok(listaDeseosService.getProductosMasDeseados(limite));
    }

    @GetMapping("/existe")
    @Operation(summary = "Verificar si producto está en lista de deseos")
    public ResponseEntity<Boolean> existeEnLista(@RequestParam Long clienteId, @RequestParam Long productoId) {
        boolean existe = listaDeseosService.existeEnListaDeseos(clienteId, productoId);
        return ResponseEntity.ok(existe);
    }
}