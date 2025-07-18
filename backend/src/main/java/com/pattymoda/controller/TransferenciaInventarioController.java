package com.pattymoda.controller;

import com.pattymoda.entity.TransferenciaInventario;
import com.pattymoda.service.TransferenciaInventarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transferencias-inventario")
@Tag(name = "TransferenciasInventario", description = "API para gestión de transferencias de inventario")
public class TransferenciaInventarioController extends BaseController<TransferenciaInventario, Long> {
    @Autowired
    public TransferenciaInventarioController(TransferenciaInventarioService transferenciaInventarioService) {
        super(transferenciaInventarioService);
        this.transferenciaInventarioService = transferenciaInventarioService;
    }

    private final TransferenciaInventarioService transferenciaInventarioService;

    @GetMapping("/numero/{numero}")
    @Operation(summary = "Buscar transferencia por número", description = "Busca una transferencia por su número")
    public ResponseEntity<TransferenciaInventario> getByNumero(@PathVariable String numero) {
        return transferenciaInventarioService.findByNumeroTransferencia(numero)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/estado/{estado}")
    @Operation(summary = "Buscar transferencias por estado", description = "Busca todas las transferencias por estado")
    public ResponseEntity<List<TransferenciaInventario>> getByEstado(@PathVariable TransferenciaInventario.EstadoTransferencia estado) {
        return ResponseEntity.ok(transferenciaInventarioService.findByEstado(estado));
    }
} 