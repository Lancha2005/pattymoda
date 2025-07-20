package com.pattymoda.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;

@Entity
@Table(name = "detalle_devolucion")
@Data
@EqualsAndHashCode(callSuper = true)
public class DetalleDevolucion extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "devolucion_id", nullable = false)
    private Devolucion devolucion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "detalle_venta_id", nullable = false)
    private DetalleVenta detalleVenta;

    @Column(name = "cantidad_devuelta", nullable = false)
    private Integer cantidadDevuelta;

    @Column(name = "precio_unitario", precision = 10, scale = 2, nullable = false)
    private BigDecimal precioUnitario;

    @Column(name = "subtotal", precision = 10, scale = 2, nullable = false)
    private BigDecimal subtotal;

    @Enumerated(EnumType.STRING)
    @Column(name = "condicion_producto")
    private CondicionProducto condicionProducto = CondicionProducto.NUEVO;

    @Column(name = "observaciones", length = 500)
    private String observaciones;

    public enum CondicionProducto {
        NUEVO, USADO_BUENO, USADO_REGULAR, DEFECTUOSO
    }
} 