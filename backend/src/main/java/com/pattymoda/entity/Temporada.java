package com.pattymoda.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDate;

@Entity
@Table(name = "temporadas")
@Data
@EqualsAndHashCode(callSuper = true)
public class Temporada extends BaseEntity {
    
    @Column(name = "codigo", length = 20, nullable = false, unique = true)
    private String codigo;
    
    @Column(name = "nombre", length = 100, nullable = false)
    private String nombre;
    
    @Column(name = "descripcion", length = 500)
    private String descripcion;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_temporada", nullable = false)
    private TipoTemporada tipoTemporada;
    
    @Column(name = "año", nullable = false)
    private Integer año;
    
    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;
    
    @Column(name = "fecha_fin", nullable = false)
    private LocalDate fechaFin;
    
    @Column(name = "fecha_inicio_liquidacion")
    private LocalDate fechaInicioLiquidacion;
    
    @Column(name = "porcentaje_descuento_liquidacion", precision = 5, scale = 2)
    private java.math.BigDecimal porcentajeDescuentoLiquidacion;
    
    @Column(name = "activo")
    private Boolean activo = true;
    
    @Column(name = "es_temporada_actual")
    private Boolean esTemporadaActual = false;
    
    @Column(name = "imagen_banner", length = 255)
    private String imagenBanner;
    
    @Column(name = "color_tema", length = 7)
    private String colorTema;
    
    public enum TipoTemporada {
        PRIMAVERA_VERANO,
        OTOÑO_INVIERNO,
        ESPECIAL,
        LIQUIDACION,
        NAVIDAD,
        VERANO,
        INVIERNO
    }
}