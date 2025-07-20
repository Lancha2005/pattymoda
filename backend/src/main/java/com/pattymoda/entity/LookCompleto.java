package com.pattymoda.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;

@Entity
@Table(name = "looks_completos")
@Data
@EqualsAndHashCode(callSuper = true)
public class LookCompleto extends BaseEntity {
    
    @Column(name = "codigo", length = 20, nullable = false, unique = true)
    private String codigo;
    
    @Column(name = "nombre", length = 200, nullable = false)
    private String nombre;
    
    @Column(name = "descripcion", length = 1000)
    private String descripcion;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "temporada_id")
    private Temporada temporada;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coleccion_id")
    private Coleccion coleccion;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "ocasion")
    private Ocasion ocasion;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "estilo")
    private Estilo estilo;
    
    @Column(name = "precio_total", precision = 12, scale = 2)
    private BigDecimal precioTotal;
    
    @Column(name = "precio_con_descuento", precision = 12, scale = 2)
    private BigDecimal precioConDescuento;
    
    @Column(name = "porcentaje_descuento", precision = 5, scale = 2)
    private BigDecimal porcentajeDescuento;
    
    @Column(name = "imagen_principal", length = 255)
    private String imagenPrincipal;
    
    @Column(name = "imagen_modelo", length = 255)
    private String imagenModelo;
    
    @Column(name = "destacado")
    private Boolean destacado = false;
    
    @Column(name = "activo")
    private Boolean activo = true;
    
    @Column(name = "orden_visualizacion")
    private Integer ordenVisualizacion = 0;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estilista_id")
    private Usuario estilista;
    
    public enum Ocasion {
        CASUAL, FORMAL, DEPORTIVO, FIESTA, TRABAJO, PLAYA, NOCHE, BODA
    }
    
    public enum Estilo {
        CLASICO, MODERNO, BOHEMIO, MINIMALISTA, ROMANTICO, URBANO, VINTAGE, ELEGANTE
    }
}