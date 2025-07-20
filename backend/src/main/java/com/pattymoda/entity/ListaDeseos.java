package com.pattymoda.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

@Entity
@Table(name = "lista_deseos",
       uniqueConstraints = @UniqueConstraint(columnNames = {"cliente_id", "producto_id"}))
@Data
@EqualsAndHashCode(callSuper = true)
public class ListaDeseos extends BaseEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "talla_preferida_id")
    private Talla tallaPreferida;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "color_preferido_id")
    private Color colorPreferido;
    
    @Column(name = "fecha_agregado", nullable = false)
    private LocalDateTime fechaAgregado = LocalDateTime.now();
    
    @Column(name = "prioridad")
    private Integer prioridad = 1;
    
    @Column(name = "notificar_disponibilidad")
    private Boolean notificarDisponibilidad = true;
    
    @Column(name = "notificar_precio")
    private Boolean notificarPrecio = true;
    
    @Column(name = "precio_deseado", precision = 10, scale = 2)
    private java.math.BigDecimal precioDeseado;
    
    @Column(name = "notas", length = 500)
    private String notas;
    
    @Column(name = "activo")
    private Boolean activo = true;
}