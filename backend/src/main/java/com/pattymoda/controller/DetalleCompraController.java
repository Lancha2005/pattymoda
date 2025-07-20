package com.pattymoda.controller;

import com.pattymoda.entity.DetalleCompra;
import com.pattymoda.service.DetalleCompraService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/detalle-compra")
@Tag(name = "DetalleCompra", description = "API para gestión de detalles de compra")
public class DetalleCompraController extends BaseController<DetalleCompra, Long> {
    @Autowired
    public DetalleCompraController(DetalleCompraService detalleCompraService) {
        super(detalleCompraService);
        this.detalleCompraService = detalleCompraService;
    }

    private final DetalleCompraService detalleCompraService;

    @GetMapping("/compra/{compraId}")
    @Operation(summary = "Obtener detalles por compra", description = "Obtiene todos los detalles de una compra")
    public ResponseEntity<List<DetalleCompra>> getByCompra(@PathVariable Long compraId) {
        return ResponseEntity.ok(detalleCompraService.findByCompraId(compraId));
    }

    @GetMapping("/producto/{productoId}")
    @Operation(summary = "Obtener detalles por producto", description = "Obtiene todos los detalles de compra de un producto")
    public ResponseEntity<List<DetalleCompra>> getByProducto(@PathVariable Long productoId) {
        return ResponseEntity.ok(detalleCompraService.findByProductoId(productoId));
    }
} 