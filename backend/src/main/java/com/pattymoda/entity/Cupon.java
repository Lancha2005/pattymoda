package com.pattymoda.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "cupones")
@Data
@EqualsAndHashCode(callSuper = true)
public class Cupon extends BaseEntity {
    
    @Column(name = "codigo", length = 50, nullable = false, unique = true)
    private String codigo;
    
    @Column(name = "nombre", length = 200, nullable = false)
    private String nombre;
    
    @Column(name = "descripcion", length = 1000)
    private String descripcion;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_descuento", nullable = false)
    private TipoDescuento tipoDescuento;
    
    @Column(name = "valor_descuento", precision = 10, scale = 2)
    private BigDecimal valorDescuento;
    
    @Column(name = "porcentaje_descuento", precision = 5, scale = 2)
    private BigDecimal porcentajeDescuento;
    
    @Column(name = "descuento_maximo", precision = 10, scale = 2)
    private BigDecimal descuentoMaximo;
    
    @Column(name = "monto_minimo_compra", precision = 10, scale = 2)
    private BigDecimal montoMinimoCompra;
    
    @Column(name = "cantidad_maxima_usos")
    private Integer cantidadMaximaUsos;
    
    @Column(name = "usos_por_cliente")
    private Integer usosPorCliente = 1;
    
    @Column(name = "usos_actuales")
    private Integer usosActuales = 0;
    
    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;
    
    @Column(name = "fecha_fin", nullable = false)
    private LocalDate fechaFin;
    
    @Column(name = "hora_inicio")
    private String horaInicio;
    
    @Column(name = "hora_fin")
    private String horaFin;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "aplica_a")
    private AplicaA aplicaA = AplicaA.TODOS;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_cliente_aplicable")
    private TipoClienteAplicable tipoClienteAplicable = TipoClienteAplicable.TODOS;
    
    @Column(name = "es_acumulable")
    private Boolean esAcumulable = false;
    
    @Column(name = "requiere_codigo")
    private Boolean requiereCodigo = true;
    
    @Column(name = "activo")
    private Boolean activo = true;
    
    @Column(name = "visible_publico")
    private Boolean visiblePublico = true;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creado_por")
    private Usuario creadoPor;
    
    public enum TipoDescuento {
        PORCENTAJE, MONTO_FIJO, ENVIO_GRATIS, PRODUCTO_GRATIS
    }
    
    public enum AplicaA {
        TODOS, PRODUCTOS_ESPECIFICOS, CATEGORIAS, MARCAS, COLECCIONES, TEMPORADAS
    }
    
    public enum TipoClienteAplicable {
        TODOS, NUEVOS, VIP, REGULARES, MAYORISTAS
    }
}