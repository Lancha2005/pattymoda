package com.tienda.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ReporteVentas {
    private LocalDate fecha;
    private BigDecimal totalVentas;
    private Long cantidadVentas;
    
    // Constructors
    public ReporteVentas() {}
    
    public ReporteVentas(LocalDate fecha, BigDecimal totalVentas, Long cantidadVentas) {
        this.fecha = fecha;
        this.totalVentas = totalVentas;
        this.cantidadVentas = cantidadVentas;
    }
    
    // Getters and Setters
    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
    
    public BigDecimal getTotalVentas() { return totalVentas; }
    public void setTotalVentas(BigDecimal totalVentas) { this.totalVentas = totalVentas; }
    
    public Long getCantidadVentas() { return cantidadVentas; }
    public void setCantidadVentas(Long cantidadVentas) { this.cantidadVentas = cantidadVentas; }
}