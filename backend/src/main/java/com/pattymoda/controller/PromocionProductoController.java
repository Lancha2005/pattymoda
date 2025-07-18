package com.pattymoda.controller;

import com.pattymoda.entity.PromocionProducto;
import com.pattymoda.service.PromocionProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/promociones-productos")
@Tag(name = "PromocionesProductos", description = "API para gestión de productos de promociones")
public class PromocionProductoController extends BaseController<PromocionProducto, Long> {
    @Autowired
    public PromocionProductoController(PromocionProductoService promocionProductoService) {
        super(promocionProductoService);
        this.promocionProductoService = promocionProductoService;
    }

    private final PromocionProductoService promocionProductoService;

    @GetMapping("/promocion/{promocionId}")
    @Operation(summary = "Obtener productos por promoción", description = "Obtiene todos los productos asociados a una promoción")
    public ResponseEntity<List<PromocionProducto>> getByPromocion(@PathVariable Long promocionId) {
        return ResponseEntity.ok(promocionProductoService.findByPromocionId(promocionId));
    }

    @GetMapping("/producto/{productoId}")
    @Operation(summary = "Obtener promociones por producto", description = "Obtiene todas las promociones asociadas a un producto")
    public ResponseEntity<List<PromocionProducto>> getByProducto(@PathVariable Long productoId) {
        return ResponseEntity.ok(promocionProductoService.findByProductoId(productoId));
    }
} 