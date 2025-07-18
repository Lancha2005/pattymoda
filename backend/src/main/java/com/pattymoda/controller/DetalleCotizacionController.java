package com.pattymoda.controller;

import com.pattymoda.entity.DetalleCotizacion;
import com.pattymoda.service.DetalleCotizacionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/detalle-cotizacion")
@Tag(name = "DetalleCotizacion", description = "API para gestión de detalles de cotización")
public class DetalleCotizacionController extends BaseController<DetalleCotizacion, Long> {
    @Autowired
    public DetalleCotizacionController(DetalleCotizacionService detalleCotizacionService) {
        super(detalleCotizacionService);
        this.detalleCotizacionService = detalleCotizacionService;
    }

    private final DetalleCotizacionService detalleCotizacionService;

    @GetMapping("/cotizacion/{cotizacionId}")
    @Operation(summary = "Buscar detalles por cotización", description = "Busca todos los detalles de una cotización")
    public ResponseEntity<List<DetalleCotizacion>> getByCotizacion(@PathVariable Long cotizacionId) {
        return ResponseEntity.ok(detalleCotizacionService.findByCotizacionId(cotizacionId));
    }

    @GetMapping("/producto/{productoId}")
    @Operation(summary = "Buscar detalles por producto", description = "Busca todos los detalles de cotización de un producto")
    public ResponseEntity<List<DetalleCotizacion>> getByProducto(@PathVariable Long productoId) {
        return ResponseEntity.ok(detalleCotizacionService.findByProductoId(productoId));
    }
} 