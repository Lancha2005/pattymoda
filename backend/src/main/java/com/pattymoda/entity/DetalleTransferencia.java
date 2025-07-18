package com.pattymoda.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "detalle_transferencia")
@Data
@EqualsAndHashCode(callSuper = true)
public class DetalleTransferencia extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transferencia_id", nullable = false)
    private TransferenciaInventario transferencia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @Column(name = "cantidad_enviada", nullable = false)
    private Integer cantidadEnviada;

    @Column(name = "cantidad_recibida")
    private Integer cantidadRecibida = 0;

    @Column(name = "observaciones", length = 500)
    private String observaciones;
} 