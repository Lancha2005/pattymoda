package com.tienda.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "producto_colores")
public class ProductoColor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    @JsonIgnore
    private Producto producto;
    
    @Column(nullable = false, length = 50)
    private String color;
    
    @Column(name = "codigo_hex", length = 7)
    private String codigoHex = "#000000";
    
    @Column(name = "stock_color")
    private Integer stockColor = 0;
    
    // Constructors
    public ProductoColor() {}
    
    public ProductoColor(Producto producto, String color, String codigoHex, Integer stockColor) {
        this.producto = producto;
        this.color = color;
        this.codigoHex = codigoHex;
        this.stockColor = stockColor;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }
    
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    
    public String getCodigoHex() { return codigoHex; }
    public void setCodigoHex(String codigoHex) { this.codigoHex = codigoHex; }
    
    public Integer getStockColor() { return stockColor; }
    public void setStockColor(Integer stockColor) { this.stockColor = stockColor; }
}