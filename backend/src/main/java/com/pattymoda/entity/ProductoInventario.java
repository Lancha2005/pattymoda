package com.pattymoda.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "productos_inventario")
@Data
public class ProductoInventario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "producto_id", referencedColumnName = "id", unique = true)
    private Producto producto;

    @Column(name = "stock_actual")
    private Integer stockActual = 0;

    @Column(name = "stock_minimo")
    private Integer stockMinimo = 0;

    @Column(name = "stock_maximo")
    private Integer stockMaximo = 0;

    @Column(name = "stock_disponible")
    private Integer stockDisponible = 0;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;
}
