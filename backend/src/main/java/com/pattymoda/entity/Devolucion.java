package com.pattymoda.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "devoluciones")
@Data
@EqualsAndHashCode(callSuper = true)
public class Devolucion extends BaseEntity {
    @Column(name = "numero_devolucion", length = 20, nullable = false, unique = true)
    private String numeroDevolucion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venta_id", nullable = false)
    private Venta venta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @Column(name = "fecha_devolucion", nullable = false)
    private LocalDateTime fechaDevolucion;

    @Enumerated(EnumType.STRING)
    @Column(name = "motivo", nullable = false)
    private MotivoDevolucion motivo;

    @Column(name = "descripcion_motivo")
    private String descripcionMotivo;

    @Column(name = "subtotal_devuelto", precision = 10, scale = 2, nullable = false)
    private BigDecimal subtotalDevuelto = BigDecimal.ZERO;

    @Column(name = "total_devuelto", precision = 10, scale = 2, nullable = false)
    private BigDecimal totalDevuelto;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private EstadoDevolucion estado = EstadoDevolucion.PENDIENTE;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_devolucion")
    private TipoDevolucion tipoDevolucion = TipoDevolucion.REEMBOLSO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "autorizado_por")
    private Usuario autorizadoPor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "procesado_por")
    private Usuario procesadoPor;

    @Column(name = "observaciones")
    private String observaciones;

    public enum MotivoDevolucion {
        DEFECTO_PRODUCTO, TALLA_INCORRECTA, COLOR_INCORRECTO, NO_SATISFECHO, CAMBIO_OPINION, GARANTIA, OTRO
    }
    public enum EstadoDevolucion {
        PENDIENTE, APROBADA, RECHAZADA, PROCESADA
    }
    public enum TipoDevolucion {
        REEMBOLSO, CAMBIO, NOTA_CREDITO
    }
} 