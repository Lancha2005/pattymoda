package com.pattymoda.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "metodos_pago_venta")
@Data
@EqualsAndHashCode(callSuper = true)
public class MetodoPagoVenta extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venta_id", nullable = false)
    private Venta venta;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_pago", nullable = false)
    private TipoPago tipoPago;

    @Column(name = "monto", precision = 10, scale = 2, nullable = false)
    private BigDecimal monto;

    @Column(name = "referencia", length = 100)
    private String referencia;

    @Column(name = "numero_operacion", length = 50)
    private String numeroOperacion;

    @Column(name = "banco", length = 100)
    private String banco;

    @Column(name = "tipo_tarjeta", length = 50)
    private String tipoTarjeta;

    @Column(name = "ultimos_4_digitos", length = 4)
    private String ultimos4Digitos;

    @Column(name = "numero_cuotas")
    private Integer numeroCuotas = 1;

    @Column(name = "tasa_interes", precision = 5, scale = 2)
    private BigDecimal tasaInteres = BigDecimal.ZERO;

    @Column(name = "comision", precision = 10, scale = 2)
    private BigDecimal comision = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoPago estado = EstadoPago.APROBADO;

    @Column(name = "fecha_pago")
    private LocalDateTime fechaPago;

    @Column(name = "notas", length = 500)
    private String notas;

    public enum TipoPago {
        EFECTIVO, TARJETA_DEBITO, TARJETA_CREDITO, YAPE, PLIN, TRANSFERENCIA, CHEQUE, CREDITO
    }
    public enum EstadoPago {
        PENDIENTE, APROBADO, RECHAZADO, ANULADO
    }
} 