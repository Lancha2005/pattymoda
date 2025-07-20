package com.pattymoda.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

@Entity
@Table(name = "productos_reviews")
@Data
@EqualsAndHashCode(callSuper = true)
public class ProductoReview extends BaseEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venta_id")
    private Venta venta;
    
    @Column(name = "calificacion", nullable = false)
    private Integer calificacion; // 1-5 estrellas
    
    @Column(name = "titulo", length = 200)
    private String titulo;
    
    @Column(name = "comentario", columnDefinition = "TEXT")
    private String comentario;
    
    @Column(name = "fecha_review", nullable = false)
    private LocalDateTime fechaReview = LocalDateTime.now();
    
    @Enumerated(EnumType.STRING)
    @Column(name = "calificacion_talla")
    private CalificacionTalla calificacionTalla;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "calificacion_calidad")
    private CalificacionCalidad calificacionCalidad;
    
    @Column(name = "recomendaria")
    private Boolean recomendaria = true;
    
    @Column(name = "verificado")
    private Boolean verificado = false;
    
    @Column(name = "aprobado")
    private Boolean aprobado = false;
    
    @Column(name = "util_positivos")
    private Integer utilPositivos = 0;
    
    @Column(name = "util_negativos")
    private Integer utilNegativos = 0;
    
    @Column(name = "respuesta_tienda", columnDefinition = "TEXT")
    private String respuestaTienda;
    
    @Column(name = "fecha_respuesta")
    private LocalDateTime fechaRespuesta;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "respondido_por")
    private Usuario respondidoPor;
    
    public enum CalificacionTalla {
        MUY_PEQUEÑO, PEQUEÑO, PERFECTO, GRANDE, MUY_GRANDE
    }
    
    public enum CalificacionCalidad {
        EXCELENTE, BUENA, REGULAR, MALA, PESIMA
    }
}