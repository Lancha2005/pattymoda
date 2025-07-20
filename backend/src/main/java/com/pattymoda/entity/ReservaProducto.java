package com.pattymoda.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservas_productos")
@Data
@EqualsAndHashCode(callSuper = true)
public class ReservaProducto extends BaseEntity {
    
    @Column(name = "numero_reserva", length = 20, nullable = false, unique = true)
    private String numeroReserva;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "talla_id")
    private Talla talla;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "color_id")
    private Color color;
    
    @Column(name = "cantidad_reservada", nullable = false)
    private Integer cantidadReservada;
    
    @Column(name = "fecha_reserva", nullable = false)
    private LocalDateTime fechaReserva;
    
    @Column(name = "fecha_expiracion", nullable = false)
    private LocalDateTime fechaExpiracion;
    
    @Column(name = "tiempo_reserva_horas")
    private Integer tiempoReservaHoras = 24;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoReserva estado = EstadoReserva.ACTIVA;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "motivo_reserva")
    private MotivoReserva motivoReserva = MotivoReserva.CLIENTE;
    
    @Column(name = "observaciones", length = 500)
    private String observaciones;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendedor_id")
    private Usuario vendedor;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venta_id")
    private Venta venta;
    
    @Column(name = "telefono_contacto", length = 20)
    private String telefonoContacto;
    
    @Column(name = "email_contacto", length = 100)
    private String emailContacto;
    
    public enum EstadoReserva {
        ACTIVA, VENDIDA, EXPIRADA, CANCELADA
    }
    
    public enum MotivoReserva {
        CLIENTE, VENDEDOR, PROMOCION, EVENTO
    }
}