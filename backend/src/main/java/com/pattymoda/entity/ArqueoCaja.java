package com.pattymoda.entity;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "arqueos_caja")
@Data
@EqualsAndHashCode(callSuper = true)
public class ArqueoCaja extends BaseEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "caja_id", nullable = false)
    private Caja caja;
    
    @Column(name = "fecha_arqueo", nullable = false)
    private LocalDate fechaArqueo;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "turno")
    private Turno turno = Turno.COMPLETO;
    
    @Column(name = "efectivo_inicio", precision = 10, scale = 2, nullable = false)
    private BigDecimal efectivoInicio = BigDecimal.ZERO;
    
    @Column(name = "efectivo_fin_sistema", precision = 10, scale = 2, nullable = false)
    private BigDecimal efectivoFinSistema = BigDecimal.ZERO;
    
    @Column(name = "efectivo_fin_fisico", precision = 10, scale = 2, nullable = false)
    private BigDecimal efectivoFinFisico = BigDecimal.ZERO;
    
    @Column(name = "diferencia", precision = 10, scale = 2)
    private BigDecimal diferencia;
    
    @Column(name = "total_ventas", precision = 10, scale = 2)
    private BigDecimal totalVentas = BigDecimal.ZERO;
    
    @Column(name = "total_gastos", precision = 10, scale = 2)
    private BigDecimal totalGastos = BigDecimal.ZERO;
    
    @Column(name = "observaciones")
    private String observaciones;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private EstadoArqueo estado = EstadoArqueo.ABIERTO;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_apertura")
    private Usuario usuarioApertura;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_cierre")
    private Usuario usuarioCierre;
    
    @Column(name = "fecha_apertura", nullable = false)
    private LocalDateTime fechaApertura;
    
    @Column(name = "fecha_cierre")
    private LocalDateTime fechaCierre;
    
    public enum Turno {
        MAÑANA,
        TARDE,
        NOCHE,
        COMPLETO
    }
    
    public enum EstadoArqueo {
        ABIERTO,
        CERRADO,
        CUADRADO,
        DESCUADRADO
    }
} 