package com.pattymoda.controller;

import com.pattymoda.entity.ProductoSeo;
import com.pattymoda.service.ProductoSeoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/productos-seo")
@Tag(name = "ProductoSeo", description = "API para gestión SEO de productos")
public class ProductoSeoController extends BaseController<ProductoSeo, Long> {
    @Autowired
    public ProductoSeoController(ProductoSeoService productoSeoService) {
        super(productoSeoService);
        this.productoSeoService = productoSeoService;
    }

    private final ProductoSeoService productoSeoService;

    @GetMapping("/producto/{productoId}")
    @Operation(summary = "Buscar SEO por producto", description = "Busca el SEO de un producto por su ID")
    public ResponseEntity<ProductoSeo> getByProducto(@PathVariable Long productoId) {
        return ResponseEntity.ok(productoSeoService.findByProductoId(productoId));
    }

    @GetMapping("/slug/{slug}")
    @Operation(summary = "Buscar SEO por slug", description = "Busca el SEO de un producto por su slug")
    public ResponseEntity<ProductoSeo> getBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(productoSeoService.findBySlug(slug));
    }
} 