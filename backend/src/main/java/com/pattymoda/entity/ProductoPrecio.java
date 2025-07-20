package com.pattymoda.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "productos_precios")
@Data
@EqualsAndHashCode(callSuper = true)
public class ProductoPrecio extends BaseEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;
    
    @Column(name = "precio_venta", precision = 10, scale = 2, nullable = false)
    private BigDecimal precioVenta;
    
    @Column(name = "precio_oferta", precision = 10, scale = 2)
    private BigDecimal precioOferta;
    
    @Column(name = "costo", precision = 10, scale = 2)
    private BigDecimal costo;
    
    @Column(name = "margen_porcentaje", precision = 5, scale = 2)
    private BigDecimal margenPorcentaje;
    
    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;
    
    @Column(name = "fecha_fin")
    private LocalDate fechaFin;
    
    @Column(name = "activo")
    private Boolean activo = true;
    
    @Column(name = "motivo_cambio", length = 200)
    private String motivoCambio;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
} 