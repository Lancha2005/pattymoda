package com.pattymoda.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "clientes_comercial")
@Data
@EqualsAndHashCode(callSuper = true)
public class ClienteComercial extends BaseEntity {
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false, unique = true)
    private Cliente cliente;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_cliente", nullable = false)
    private TipoCliente tipoCliente;
    
    @Column(name = "total_compras", precision = 12, scale = 2)
    private BigDecimal totalCompras = BigDecimal.ZERO;
    
    @Column(name = "cantidad_compras")
    private Integer cantidadCompras = 0;
    
    @Column(name = "ultima_compra")
    private LocalDateTime ultimaCompra;
    
    @Column(name = "limite_credito", precision = 10, scale = 2)
    private BigDecimal limiteCredito = BigDecimal.ZERO;
    
    @Column(name = "descuento_personalizado", precision = 5, scale = 2)
    private BigDecimal descuentoPersonalizado = BigDecimal.ZERO;
    
    @Column(name = "puntos_disponibles")
    private Integer puntosDisponibles = 0;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "nivel_cliente")
    private NivelCliente nivelCliente = NivelCliente.BRONCE;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendedor_asignado_id")
    private Usuario vendedorAsignado;
    
    public enum TipoCliente {
        REGULAR,
        VIP,
        MAYORISTA,
        MINORISTA,
        CORPORATIVO
    }
    
    public enum NivelCliente {
        BRONCE,
        PLATA,
        ORO,
        PLATINO
    }
} 