package com.pattymoda.controller;

import com.pattymoda.entity.Compra;
import com.pattymoda.service.CompraService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/compras")
@Tag(name = "Compras", description = "API para gestión de compras")
public class CompraController extends BaseController<Compra, Long> {
    @Autowired
    public CompraController(CompraService compraService) {
        super(compraService);
        this.compraService = compraService;
    }

    private final CompraService compraService;

    @GetMapping("/numero/{numero}")
    @Operation(summary = "Buscar compra por número", description = "Busca una compra por su número")
    public ResponseEntity<Compra> getByNumero(@PathVariable String numero) {
        return compraService.findByNumeroCompra(numero)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
} 