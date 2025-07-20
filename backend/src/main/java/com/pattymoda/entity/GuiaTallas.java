package com.pattymoda.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "guia_tallas")
@Data
@EqualsAndHashCode(callSuper = true)
public class GuiaTallas extends BaseEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "marca_id")
    private Marca marca;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "talla_id", nullable = false)
    private Talla talla;
    
    @Column(name = "busto_cm", precision = 5, scale = 2)
    private java.math.BigDecimal bustoCm;
    
    @Column(name = "cintura_cm", precision = 5, scale = 2)
    private java.math.BigDecimal cinturaCm;
    
    @Column(name = "cadera_cm", precision = 5, scale = 2)
    private java.math.BigDecimal caderaCm;
    
    @Column(name = "largo_cm", precision = 5, scale = 2)
    private java.math.BigDecimal largoCm;
    
    @Column(name = "ancho_cm", precision = 5, scale = 2)
    private java.math.BigDecimal anchoCm;
    
    @Column(name = "peso_min_kg", precision = 5, scale = 2)
    private java.math.BigDecimal pesoMinKg;
    
    @Column(name = "peso_max_kg", precision = 5, scale = 2)
    private java.math.BigDecimal pesoMaxKg;
    
    @Column(name = "altura_min_cm", precision = 5, scale = 2)
    private java.math.BigDecimal alturaMinCm;
    
    @Column(name = "altura_max_cm", precision = 5, scale = 2)
    private java.math.BigDecimal alturaMaxCm;
    
    @Column(name = "equivalencia_us", length = 10)
    private String equivalenciaUs;
    
    @Column(name = "equivalencia_eu", length = 10)
    private String equivalenciaEu;
    
    @Column(name = "equivalencia_uk", length = 10)
    private String equivalenciaUk;
    
    @Column(name = "notas", length = 500)
    private String notas;
    
    @Column(name = "activo")
    private Boolean activo = true;
}