package com.pattymoda.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;

@Entity
@Table(name = "cajas")
@Data
@EqualsAndHashCode(callSuper = true)
public class Caja extends BaseEntity {
    
    @Column(name = "codigo", length = 20, nullable = false, unique = true)
    private String codigo;
    
    @Column(name = "nombre", length = 100, nullable = false)
    private String nombre;
    
    @Column(name = "descripcion", length = 200)
    private String descripcion;
    
    @Column(name = "ubicacion", length = 100)
    private String ubicacion;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "responsable_id")
    private Usuario responsable;
    
    @Column(name = "monto_inicial", precision = 10, scale = 2)
    private BigDecimal montoInicial = BigDecimal.ZERO;
    
    @Column(name = "activo")
    private Boolean activo = true;
} 