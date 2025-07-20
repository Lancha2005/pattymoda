package com.pattymoda.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "cupones_uso")
@Data
@EqualsAndHashCode(callSuper = true)
public class CuponUso extends BaseEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cupon_id", nullable = false)
    private Cupon cupon;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venta_id", nullable = false)
    private Venta venta;
    
    @Column(name = "monto_descuento", precision = 10, scale = 2, nullable = false)
    private BigDecimal montoDescuento;
    
    @Column(name = "fecha_uso", nullable = false)
    private LocalDateTime fechaUso;
    
    @Column(name = "ip_address", length = 45)
    private String ipAddress;
}