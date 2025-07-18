package com.pattymoda.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;

@Entity
@Table(name = "productos_tallas")
@Data
@EqualsAndHashCode(callSuper = true)
public class ProductoTalla extends BaseEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "talla_id", nullable = false)
    private Talla talla;
    
    @Column(name = "stock_talla", nullable = false)
    private Integer stockTalla = 0;
    
    @Column(name = "precio_adicional", precision = 10, scale = 2)
    private BigDecimal precioAdicional = BigDecimal.ZERO;
    
    @Column(name = "activo")
    private Boolean activo = true;
} 