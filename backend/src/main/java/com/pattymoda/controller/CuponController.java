package com.pattymoda.controller;

import com.pattymoda.entity.Cupon;
import com.pattymoda.service.CuponService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cupones")
@Tag(name = "Cupones", description = "API para gestión de cupones de descuento")
@CrossOrigin(origins = "*")
public class CuponController extends BaseController<Cupon, Long> {

    private final CuponService cuponService;

    @Autowired
    public CuponController(CuponService cuponService) {
        super(cuponService);
        this.cuponService = cuponService;
    }

    @GetMapping("/codigo/{codigo}")
    @Operation(summary = "Buscar cupón por código")
    public ResponseEntity<Cupon> getByCodigo(@PathVariable String codigo) {
        return cuponService.findByCodigo(codigo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/disponibles")
    @Operation(summary = "Obtener cupones disponibles")
    public ResponseEntity<List<Cupon>> getCuponesDisponibles() {
        return ResponseEntity.ok(cuponService.getCuponesDisponibles());
    }

    @PostMapping("/validar")
    @Operation(summary = "Validar cupón para una compra")
    public ResponseEntity<Map<String, Object>> validarCupon(@RequestBody Map<String, Object> request) {
        String codigo = (String) request.get("codigo");
        Long clienteId = Long.valueOf(request.get("clienteId").toString());
        BigDecimal montoCompra = new BigDecimal(request.get("montoCompra").toString());

        // Aquí necesitarías obtener el cliente por ID
        // Cliente cliente = clienteService.findById(clienteId).orElse(null);
        
        // Por ahora retornamos una respuesta básica
        Map<String, Object> response = Map.of(
            "valido", false,
            "mensaje", "Funcionalidad en desarrollo"
        );
        
        return ResponseEntity.ok(response);
    }

    @PostMapping("/calcular-descuento")
    @Operation(summary = "Calcular descuento de un cupón")
    public ResponseEntity<Map<String, Object>> calcularDescuento(@RequestBody Map<String, Object> request) {
        String codigo = (String) request.get("codigo");
        BigDecimal montoCompra = new BigDecimal(request.get("montoCompra").toString());

        return cuponService.findByCodigo(codigo)
                .map(cupon -> {
                    BigDecimal descuento = cuponService.calcularDescuento(cupon, montoCompra);
                    Map<String, Object> response = Map.of(
                        "descuento", descuento,
                        "montoFinal", montoCompra.subtract(descuento),
                        "cupon", cupon
                    );
                    return ResponseEntity.ok(response);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Crear nuevo cupón")
    public ResponseEntity<Cupon> create(@RequestBody Cupon cupon) {
        try {
            Cupon saved = cuponService.save(cupon);
            return ResponseEntity.status(201).body(saved);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar cupón")
    public ResponseEntity<Cupon> update(@PathVariable Long id, @RequestBody Cupon cupon) {
        if (!cuponService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        cupon.setId(id);
        try {
            Cupon updated = cuponService.save(cupon);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}