package com.pattymoda.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "compras")
@Data
@EqualsAndHashCode(callSuper = true)
public class Compra extends BaseEntity {
    @Column(name = "numero_compra", length = 20, nullable = false, unique = true)
    private String numeroCompra;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proveedor_id", nullable = false)
    private Proveedor proveedor;

    @Column(name = "fecha_compra", nullable = false)
    private LocalDate fechaCompra;

    @Column(name = "fecha_entrega_esperada")
    private LocalDate fechaEntregaEsperada;

    @Column(name = "fecha_entrega_real")
    private LocalDate fechaEntregaReal;

    @Column(name = "subtotal", precision = 12, scale = 2, nullable = false)
    private BigDecimal subtotal = BigDecimal.ZERO;

    @Column(name = "descuento_porcentaje", precision = 5, scale = 2)
    private BigDecimal descuentoPorcentaje = BigDecimal.ZERO;

    @Column(name = "descuento_monto", precision = 10, scale = 2)
    private BigDecimal descuentoMonto = BigDecimal.ZERO;

    @Column(name = "impuesto_porcentaje", precision = 5, scale = 2)
    private BigDecimal impuestoPorcentaje = BigDecimal.ZERO;

    @Column(name = "impuesto_monto", precision = 10, scale = 2)
    private BigDecimal impuestoMonto = BigDecimal.ZERO;

    @Column(name = "total", precision = 12, scale = 2, nullable = false)
    private BigDecimal total;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private EstadoCompra estado = EstadoCompra.PENDIENTE;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_comprobante")
    private TipoComprobante tipoComprobante = TipoComprobante.FACTURA;

    @Column(name = "numero_comprobante", length = 50)
    private String numeroComprobante;

    @Column(name = "observaciones")
    private String observaciones;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    public enum EstadoCompra {
        PENDIENTE, CONFIRMADA, PARCIAL, RECIBIDA, CANCELADA
    }
    public enum TipoComprobante {
        FACTURA, BOLETA, RECIBO, ORDEN_COMPRA
    }
} 