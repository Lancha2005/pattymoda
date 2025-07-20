package com.pattymoda.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "cotizaciones")
@Data
@EqualsAndHashCode(callSuper = true)
public class Cotizacion extends BaseEntity {
    @Column(name = "numero_cotizacion", length = 20, nullable = false, unique = true)
    private String numeroCotizacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @Column(name = "fecha_cotizacion", nullable = false)
    private LocalDateTime fechaCotizacion = LocalDateTime.now();

    @Column(name = "subtotal", precision = 12, scale = 2)
    private BigDecimal subtotal = BigDecimal.ZERO;

    @Column(name = "total", precision = 12, scale = 2, nullable = false)
    private BigDecimal total = BigDecimal.ZERO;

    @Column(name = "observaciones", length = 500)
    private String observaciones;

    // Puedes agregar más campos según tus necesidades
} 