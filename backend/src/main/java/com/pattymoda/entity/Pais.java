package com.pattymoda.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "paises")
public class Pais extends BaseEntity {

    @Column(name = "codigo_iso", length = 3, nullable = false, unique = true)
    private String codigoIso;

    @Column(name = "nombre", length = 100, nullable = false)
    private String nombre;

    @Column(name = "codigo_telefono", length = 5)
    private String codigoTelefono;

    @Column(name = "activo")
    private Boolean activo = true;

    // Getters and Setters
    public String getCodigoIso() {
        return codigoIso;
    }

    public void setCodigoIso(String codigoIso) {
        this.codigoIso = codigoIso;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodigoTelefono() {
        return codigoTelefono;
    }

    public void setCodigoTelefono(String codigoTelefono) {
        this.codigoTelefono = codigoTelefono;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
} 