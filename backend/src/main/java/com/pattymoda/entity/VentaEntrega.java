package com.pattymoda.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "ventas_entrega")
@Data
@EqualsAndHashCode(callSuper = true)
public class VentaEntrega extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venta_id", nullable = false, unique = true)
    private Venta venta;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_entrega")
    private TipoEntrega tipoEntrega = TipoEntrega.RECOJO_TIENDA;

    @Column(name = "direccion_entrega")
    private String direccionEntrega;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "distrito_entrega_id")
    private Distrito distritoEntrega;

    @Column(name = "fecha_programada")
    private LocalDate fechaProgramada;

    @Column(name = "fecha_entrega_real")
    private LocalDateTime fechaEntregaReal;

    @Column(name = "costo_envio", precision = 10, scale = 2)
    private BigDecimal costoEnvio = BigDecimal.ZERO;

    @Column(name = "transportista", length = 100)
    private String transportista;

    @Column(name = "numero_guia", length = 50)
    private String numeroGuia;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_entrega")
    private EstadoEntrega estadoEntrega = EstadoEntrega.PENDIENTE;

    @Column(name = "observaciones_entrega")
    private String observacionesEntrega;

    @Column(name = "recibido_por", length = 100)
    private String recibidoPor;

    public enum TipoEntrega {
        RECOJO_TIENDA, DELIVERY, ENVIO_COURIER, ENVIO_POSTAL
    }
    public enum EstadoEntrega {
        PENDIENTE, EN_TRANSITO, ENTREGADO, DEVUELTO, CANCELADO
    }
} 