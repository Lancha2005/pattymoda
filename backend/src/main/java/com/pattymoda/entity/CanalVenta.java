package com.pattymoda.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "canales_venta")
public class CanalVenta extends BaseEntity {

    @Column(name = "codigo", length = 20, nullable = false, unique = true)
    private String codigo;

    @Column(name = "nombre", length = 50, nullable = false)
    private String nombre;

    @Column(name = "descripcion", length = 200)
    private String descripcion;

    @Column(name = "comision_porcentaje", precision = 5, scale = 2)
    private BigDecimal comisionPorcentaje = BigDecimal.ZERO;

    @Column(name = "requiere_entrega")
    private Boolean requiereEntrega = false;

    @Column(name = "activo")
    private Boolean activo = true;

    // Getters and Setters
    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getComisionPorcentaje() {
        return comisionPorcentaje;
    }

    public void setComisionPorcentaje(BigDecimal comisionPorcentaje) {
        this.comisionPorcentaje = comisionPorcentaje;
    }

    public Boolean getRequiereEntrega() {
        return requiereEntrega;
    }

    public void setRequiereEntrega(Boolean requiereEntrega) {
        this.requiereEntrega = requiereEntrega;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
} 