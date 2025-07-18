package com.pattymoda.controller;

import com.pattymoda.entity.ClientePreferencia;
import com.pattymoda.service.ClientePreferenciaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes-preferencias")
@Tag(name = "ClientePreferencia", description = "API para gestión de preferencias de clientes")
public class ClientePreferenciaController extends BaseController<ClientePreferencia, Long> {
    @Autowired
    public ClientePreferenciaController(ClientePreferenciaService clientePreferenciaService) {
        super(clientePreferenciaService);
        this.clientePreferenciaService = clientePreferenciaService;
    }

    private final ClientePreferenciaService clientePreferenciaService;

    @GetMapping("/cliente/{clienteId}")
    @Operation(summary = "Buscar preferencias por cliente", description = "Busca todas las preferencias de un cliente")
    public ResponseEntity<List<ClientePreferencia>> getByCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(clientePreferenciaService.findByClienteId(clienteId));
    }
} 