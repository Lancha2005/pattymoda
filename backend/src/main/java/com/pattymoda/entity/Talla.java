package com.pattymoda.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "tallas")
@Data
@EqualsAndHashCode(callSuper = true)
public class Talla extends BaseEntity {
    
    @Column(name = "codigo", length = 10, nullable = false, unique = true)
    private String codigo;
    
    @Column(name = "nombre", length = 50, nullable = false)
    private String nombre;
    
    @Column(name = "descripcion", length = 100)
    private String descripcion;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "categoria_talla", nullable = false)
    private CategoriaTalla categoriaTalla;
    
    @Column(name = "orden_visualizacion")
    private Integer ordenVisualizacion;
    
    @Column(name = "medidas_cm", length = 200)
    private String medidasCm;
    
    @Column(name = "equivalencia_internacional", length = 50)
    private String equivalenciaInternacional;
    
    @Column(name = "activo")
    private Boolean activo = true;
    
    public enum CategoriaTalla {
        ROPA_MUJER,
        ROPA_HOMBRE,
        CALZADO_MUJER,
        CALZADO_HOMBRE,
        ACCESORIOS,
        INFANTIL
    }
} 