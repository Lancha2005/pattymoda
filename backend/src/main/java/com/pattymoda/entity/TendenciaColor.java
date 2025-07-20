package com.pattymoda.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "tendencias_colores")
@Data
@EqualsAndHashCode(callSuper = true)
public class TendenciaColor extends BaseEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "temporada_id", nullable = false)
    private Temporada temporada;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "color_id", nullable = false)
    private Color color;
    
    @Column(name = "nombre_tendencia", length = 100, nullable = false)
    private String nombreTendencia;
    
    @Column(name = "descripcion", length = 500)
    private String descripcion;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "nivel_tendencia", nullable = false)
    private NivelTendencia nivelTendencia;
    
    @Column(name = "orden_importancia")
    private Integer ordenImportancia = 0;
    
    @Column(name = "activo")
    private Boolean activo = true;
    
    @Column(name = "imagen_referencia", length = 255)
    private String imagenReferencia;
    
    public enum NivelTendencia {
        PRINCIPAL, SECUNDARIA, COMPLEMENTARIA, ACENTO
    }
}