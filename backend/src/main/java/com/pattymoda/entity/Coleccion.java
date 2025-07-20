package com.pattymoda.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDate;

@Entity
@Table(name = "colecciones")
@Data
@EqualsAndHashCode(callSuper = true)
public class Coleccion extends BaseEntity {
    
    @Column(name = "codigo", length = 20, nullable = false, unique = true)
    private String codigo;
    
    @Column(name = "nombre", length = 100, nullable = false)
    private String nombre;
    
    @Column(name = "descripcion", length = 1000)
    private String descripcion;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "temporada_id", nullable = false)
    private Temporada temporada;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "marca_id")
    private Marca marca;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_coleccion", nullable = false)
    private TipoColeccion tipoColeccion;
    
    @Column(name = "fecha_lanzamiento")
    private LocalDate fechaLanzamiento;
    
    @Column(name = "fecha_fin_venta")
    private LocalDate fechaFinVenta;
    
    @Column(name = "precio_desde", precision = 10, scale = 2)
    private java.math.BigDecimal precioDesde;
    
    @Column(name = "precio_hasta", precision = 10, scale = 2)
    private java.math.BigDecimal precioHasta;
    
    @Column(name = "imagen_portada", length = 255)
    private String imagenPortada;
    
    @Column(name = "imagen_banner", length = 255)
    private String imagenBanner;
    
    @Column(name = "color_tema", length = 7)
    private String colorTema;
    
    @Column(name = "destacado")
    private Boolean destacado = false;
    
    @Column(name = "activo")
    private Boolean activo = true;
    
    @Column(name = "orden_visualizacion")
    private Integer ordenVisualizacion = 0;
    
    @Column(name = "meta_title", length = 255)
    private String metaTitle;
    
    @Column(name = "meta_description", length = 500)
    private String metaDescription;
    
    @Column(name = "slug", length = 255, unique = true)
    private String slug;
    
    public enum TipoColeccion {
        REGULAR,
        PREMIUM,
        LIMITADA,
        COLABORACION,
        EXCLUSIVA,
        BASICA
    }
}