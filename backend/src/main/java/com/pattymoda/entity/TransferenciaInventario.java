package com.pattymoda.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "transferencias_inventario")
@Data
@EqualsAndHashCode(callSuper = true)
public class TransferenciaInventario extends BaseEntity {
    @Column(name = "numero_transferencia", length = 20, nullable = false, unique = true)
    private String numeroTransferencia;

    @Column(name = "ubicacion_origen", length = 100, nullable = false)
    private String ubicacionOrigen;

    @Column(name = "ubicacion_destino", length = 100, nullable = false)
    private String ubicacionDestino;

    @Column(name = "fecha_transferencia", nullable = false)
    private LocalDate fechaTransferencia;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private EstadoTransferencia estado = EstadoTransferencia.PENDIENTE;

    @Column(name = "observaciones")
    private String observaciones;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "autorizado_por")
    private Usuario autorizadoPor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recibido_por")
    private Usuario recibidoPor;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    public enum EstadoTransferencia {
        PENDIENTE, EN_TRANSITO, RECIBIDA, CANCELADA
    }
} 