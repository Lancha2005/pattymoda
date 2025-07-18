package com.pattymoda.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "promociones_productos")
@Data
@EqualsAndHashCode(callSuper = true)
public class PromocionProducto extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "promocion_id", nullable = false)
    private Promocion promocion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;
} 