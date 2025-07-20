package com.pattymoda.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "looks_productos")
@Data
@EqualsAndHashCode(callSuper = true)
public class LookProducto extends BaseEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "look_id", nullable = false)
    private LookCompleto look;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "talla_sugerida_id")
    private Talla tallaSugerida;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "color_sugerido_id")
    private Color colorSugerido;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_prenda", nullable = false)
    private TipoPrenda tipoPrenda;
    
    @Column(name = "es_principal")
    private Boolean esPrincipal = false;
    
    @Column(name = "es_opcional")
    private Boolean esOpcional = false;
    
    @Column(name = "orden_en_look")
    private Integer ordenEnLook = 0;
    
    @Column(name = "notas_estilista", length = 500)
    private String notasEstilista;
    
    public enum TipoPrenda {
        BLUSA, PANTALON, FALDA, VESTIDO, CHAQUETA, ABRIGO, ZAPATOS, ACCESORIOS, BOLSO, JOYERIA
    }
}