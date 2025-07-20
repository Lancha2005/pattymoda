package com.pattymoda.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;

@Entity
@Table(name = "productos_talla_color", 
       uniqueConstraints = @UniqueConstraint(columnNames = {"producto_id", "talla_id", "color_id"}))
@Data
@EqualsAndHashCode(callSuper = true)
public class ProductoTallaColor extends BaseEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "talla_id", nullable = false)
    private Talla talla;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "color_id", nullable = false)
    private Color color;
    
    @Column(name = "sku_combinacion", length = 100, unique = true)
    private String skuCombinacion;
    
    @Column(name = "codigo_barras_combinacion", length = 50, unique = true)
    private String codigoBarrasCombinacion;
    
    @Column(name = "stock_actual", nullable = false)
    private Integer stockActual = 0;
    
    @Column(name = "stock_minimo")
    private Integer stockMinimo = 0;
    
    @Column(name = "stock_maximo")
    private Integer stockMaximo = 0;
    
    @Column(name = "stock_reservado")
    private Integer stockReservado = 0;
    
    @Column(name = "stock_disponible")
    private Integer stockDisponible = 0;
    
    @Column(name = "precio_adicional", precision = 10, scale = 2)
    private BigDecimal precioAdicional = BigDecimal.ZERO;
    
    @Column(name = "costo_adicional", precision = 10, scale = 2)
    private BigDecimal costoAdicional = BigDecimal.ZERO;
    
    @Column(name = "imagen_combinacion", length = 255)
    private String imagenCombinacion;
    
    @Column(name = "activo")
    private Boolean activo = true;
    
    @Column(name = "disponible_venta")
    private Boolean disponibleVenta = true;
    
    @Column(name = "orden_visualizacion")
    private Integer ordenVisualizacion = 0;
    
    // Método para calcular stock disponible
    @PrePersist
    @PreUpdate
    private void calcularStockDisponible() {
        this.stockDisponible = this.stockActual - (this.stockReservado != null ? this.stockReservado : 0);
    }
}