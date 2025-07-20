package com.pattymoda.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "productos_colecciones",
       uniqueConstraints = @UniqueConstraint(columnNames = {"producto_id", "coleccion_id"}))
@Data
@EqualsAndHashCode(callSuper = true)
public class ProductoColeccion extends BaseEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coleccion_id", nullable = false)
    private Coleccion coleccion;
    
    @Column(name = "orden_en_coleccion")
    private Integer ordenEnColeccion = 0;
    
    @Column(name = "es_producto_estrella")
    private Boolean esProductoEstrella = false;
    
    @Column(name = "activo")
    private Boolean activo = true;
}