package com.pattymoda.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Entity
@Table(name = "unidades_medida")
@Data
@EqualsAndHashCode(callSuper = true)
public class UnidadMedida extends BaseEntity {
    
    @Column(name = "codigo", length = 10, nullable = false, unique = true)
    private String codigo;
    
    @Column(name = "nombre", length = 50, nullable = false)
    private String nombre;
    
    @Column(name = "simbolo", length = 10)
    private String simbolo;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo")
    private TipoUnidad tipo;
    
    @Column(name = "factor_conversion", precision = 10, scale = 6)
    private BigDecimal factorConversion = BigDecimal.valueOf(1.0);
    
    @Column(name = "activo")
    private Boolean activo = true;
    
    public enum TipoUnidad {
        PESO,
        LONGITUD,
        VOLUMEN,
        UNIDAD,
        TIEMPO
    }
} 