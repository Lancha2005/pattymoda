package com.pattymoda.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "promociones")
@Data
@EqualsAndHashCode(callSuper = true)
public class Promocion extends BaseEntity {
    @Column(name = "codigo", length = 50, nullable = false, unique = true)
    private String codigo;

    @Column(name = "nombre", length = 200, nullable = false)
    private String nombre;

    @Column(name = "descripcion")
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_promocion", nullable = false)
    private TipoPromocion tipoPromocion;

    @Column(name = "valor_descuento", precision = 10, scale = 2)
    private BigDecimal valorDescuento;

    @Column(name = "porcentaje_descuento", precision = 5, scale = 2)
    private BigDecimal porcentajeDescuento;

    @Column(name = "monto_minimo_compra", precision = 10, scale = 2)
    private BigDecimal montoMinimoCompra;

    @Column(name = "cantidad_maxima_usos")
    private Integer cantidadMaximaUsos;

    @Column(name = "usos_por_cliente")
    private Integer usosPorCliente = 1;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin", nullable = false)
    private LocalDate fechaFin;

    @Column(name = "activo")
    private Boolean activo = true;

    @Enumerated(EnumType.STRING)
    @Column(name = "aplica_a")
    private AplicaA aplicaA = AplicaA.TODOS;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creado_por")
    private Usuario creadoPor;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    public enum TipoPromocion {
        DESCUENTO_PORCENTAJE, DESCUENTO_MONTO, _2X1, _3X2, ENVIO_GRATIS, REGALO
    }
    public enum AplicaA {
        TODOS, PRODUCTOS, CATEGORIAS, CLIENTES
    }
} 