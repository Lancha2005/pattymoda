package com.pattymoda.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "ventas_descuentos")
@Data
@EqualsAndHashCode(callSuper = true)
public class VentaDescuento extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venta_id", nullable = false)
    private Venta venta;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_descuento", nullable = false)
    private TipoDescuento tipoDescuento;

    @Column(name = "descripcion", length = 200)
    private String descripcion;

    @Column(name = "porcentaje", precision = 5, scale = 2)
    private BigDecimal porcentaje;

    @Column(name = "monto", precision = 10, scale = 2, nullable = false)
    private BigDecimal monto;

    @Column(name = "promocion_id")
    private Long promocionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aplicado_por")
    private Usuario aplicadoPor;

    @Column(name = "fecha_aplicacion")
    private LocalDateTime fechaAplicacion;

    public enum TipoDescuento {
        PORCENTAJE, MONTO_FIJO, PROMOCION, CLIENTE_VIP
    }
} 