package com.tienda.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "producto_tallas")
public class ProductoTalla {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    @JsonIgnore
    private Producto producto;
    
    @Column(nullable = false, length = 10)
    private String talla;
    
    @Column(name = "stock_talla")
    private Integer stockTalla = 0;
    
    // Constructors
    public ProductoTalla() {}
    
    public ProductoTalla(Producto producto, String talla, Integer stockTalla) {
        this.producto = producto;
        this.talla = talla;
        this.stockTalla = stockTalla;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }
    
    public String getTalla() { return talla; }
    public void setTalla(String talla) { this.talla = talla; }
    
    public Integer getStockTalla() { return stockTalla; }
    public void setStockTalla(Integer stockTalla) { this.stockTalla = stockTalla; }
}