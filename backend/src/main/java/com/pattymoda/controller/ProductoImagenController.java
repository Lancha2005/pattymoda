package com.pattymoda.controller;

import com.pattymoda.entity.ProductoImagen;
import com.pattymoda.service.ProductoImagenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/productos-imagenes")
@Tag(name = "ProductoImagen", description = "API para gestión de imágenes de productos")
public class ProductoImagenController extends BaseController<ProductoImagen, Long> {
    @Autowired
    public ProductoImagenController(ProductoImagenService productoImagenService) {
        super(productoImagenService);
        this.productoImagenService = productoImagenService;
    }

    private final ProductoImagenService productoImagenService;

    @GetMapping("/producto/{productoId}")
    @Operation(summary = "Buscar imágenes por producto", description = "Busca todas las imágenes de un producto")
    public ResponseEntity<List<ProductoImagen>> getByProducto(@PathVariable Long productoId) {
        return ResponseEntity.ok(productoImagenService.findByProductoId(productoId));
    }

    @GetMapping("/color/{colorId}")
    @Operation(summary = "Buscar imágenes por color", description = "Busca todas las imágenes de un color")
    public ResponseEntity<List<ProductoImagen>> getByColor(@PathVariable Long colorId) {
        return ResponseEntity.ok(productoImagenService.findByColorId(colorId));
    }

    @GetMapping("/principales")
    @Operation(summary = "Buscar imágenes principales", description = "Busca todas las imágenes principales")
    public ResponseEntity<List<ProductoImagen>> getPrincipales() {
        return ResponseEntity.ok(productoImagenService.findPrincipales());
    }

    @GetMapping("/activas")
    @Operation(summary = "Buscar imágenes activas", description = "Busca todas las imágenes activas")
    public ResponseEntity<List<ProductoImagen>> getActivas() {
        return ResponseEntity.ok(productoImagenService.findActivas());
    }
} 