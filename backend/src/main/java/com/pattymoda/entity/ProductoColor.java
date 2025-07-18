package com.pattymoda.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "productos_colores")
@Data
@EqualsAndHashCode(callSuper = true)
public class ProductoColor extends BaseEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "color_id", nullable = false)
    private Color color;
    
    @Column(name = "stock_color")
    private Integer stockColor = 0;
    
    @Column(name = "activo")
    private Boolean activo = true;
} 