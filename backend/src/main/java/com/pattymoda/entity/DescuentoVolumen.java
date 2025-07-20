package com.pattymoda.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "descuentos_volumen")
@Data
@EqualsAndHashCode(callSuper = true)
public class DescuentoVolumen extends BaseEntity {
    
    @Column(name = "nombre", length = 200, nullable = false)
    private String nombre;
    
    @Column(name = "descripcion", length = 500)
    private String descripcion;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_descuento", nullable = false)
    private TipoDescuentoVolumen tipoDescuento;
    
    @Column(name = "cantidad_minima", nullable = false)
    private Integer cantidadMinima;
    
    @Column(name = "cantidad_gratis")
    private Integer cantidadGratis;
    
    @Column(name = "porcentaje_descuento", precision = 5, scale = 2)
    private BigDecimal porcentajeDescuento;
    
    @Column(name = "monto_descuento", precision = 10, scale = 2)
    private BigDecimal montoDescuento;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "aplica_a")
    private AplicaA aplicaA = AplicaA.TODOS;
    
    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;
    
    @Column(name = "fecha_fin", nullable = false)
    private LocalDate fechaFin;
    
    @Column(name = "activo")
    private Boolean activo = true;
    
    @Column(name = "prioridad")
    private Integer prioridad = 1;
    
    public enum TipoDescuentoVolumen {
        LLEVA_2_PAGA_1,
        LLEVA_3_PAGA_2,
        PORCENTAJE_POR_CANTIDAD,
        MONTO_FIJO_POR_CANTIDAD,
        PRODUCTO_GRATIS
    }
    
    public enum AplicaA {
        TODOS, CATEGORIA, MARCA, PRODUCTOS_ESPECIFICOS, COLECCION
    }
}