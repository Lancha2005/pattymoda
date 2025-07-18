package com.pattymoda.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "colores")
@Data
@EqualsAndHashCode(callSuper = true)
public class Color extends BaseEntity {
    
    @Column(name = "codigo", length = 20, nullable = false, unique = true)
    private String codigo;
    
    @Column(name = "nombre", length = 50, nullable = false)
    private String nombre;
    
    @Column(name = "descripcion", length = 100)
    private String descripcion;
    
    @Column(name = "codigo_hex", length = 7, nullable = false)
    private String codigoHex;
    
    @Column(name = "codigo_rgb", length = 20)
    private String codigoRgb;
    
    @Column(name = "familia_color", length = 30)
    private String familiaColor;
    
    @Column(name = "orden_visualizacion")
    private Integer ordenVisualizacion;
    
    @Column(name = "activo")
    private Boolean activo = true;
} 