package com.pattymoda.controller;

import com.pattymoda.entity.Proveedor;
import com.pattymoda.service.ProveedorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/proveedores")
@Tag(name = "Proveedores", description = "API para gestión de proveedores")
public class ProveedorController extends BaseController<Proveedor, Long> {
    @Autowired
    public ProveedorController(ProveedorService proveedorService) {
        super(proveedorService);
        this.proveedorService = proveedorService;
    }

    private final ProveedorService proveedorService;

    @GetMapping("/codigo/{codigo}")
    @Operation(summary = "Buscar proveedor por código", description = "Busca un proveedor por su código")
    public ResponseEntity<Proveedor> getByCodigo(@PathVariable String codigo) {
        return proveedorService.findByCodigoProveedor(codigo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/documento/{numero}")
    @Operation(summary = "Buscar proveedor por número de documento", description = "Busca un proveedor por su número de documento")
    public ResponseEntity<Proveedor> getByNumeroDocumento(@PathVariable String numero) {
        return proveedorService.findByNumeroDocumento(numero)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
} 