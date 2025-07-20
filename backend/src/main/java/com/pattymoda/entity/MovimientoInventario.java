package com.pattymoda.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "movimientos_inventario")
@Data
@EqualsAndHashCode(callSuper = true)
public class MovimientoInventario extends BaseEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_movimiento", nullable = false)
    private TipoMovimiento tipoMovimiento;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "motivo", nullable = false)
    private MotivoMovimiento motivo;
    
    @Column(name = "cantidad_anterior", nullable = false)
    private Integer cantidadAnterior;
    
    @Column(name = "cantidad_movimiento", nullable = false)
    private Integer cantidadMovimiento;
    
    @Column(name = "cantidad_actual", nullable = false)
    private Integer cantidadActual;
    
    @Column(name = "costo_unitario", precision = 10, scale = 2)
    private BigDecimal costoUnitario;
    
    @Column(name = "valor_total", precision = 12, scale = 2)
    private BigDecimal valorTotal;
    
    @Column(name = "referencia_documento", length = 100)
    private String referenciaDocumento;
    
    @Column(name = "lote", length = 50)
    private String lote;
    
    @Column(name = "fecha_vencimiento")
    private LocalDate fechaVencimiento;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venta_id")
    private Venta venta;
    
    @Column(name = "compra_id")
    private Long compraId;
    
    @Column(name = "devolucion_id")
    private Long devolucionId;
    
    @Column(name = "transferencia_id")
    private Long transferenciaId;
    
    @Column(name = "observaciones")
    private String observaciones;
    
    @Column(name = "fecha_movimiento", nullable = false)
    private LocalDateTime fechaMovimiento;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
    
    public enum TipoMovimiento {
        ENTRADA,
        SALIDA,
        AJUSTE,
        TRANSFERENCIA,
        DEVOLUCION
    }
    
    public enum MotivoMovimiento {
        COMPRA,
        VENTA,
        AJUSTE_INVENTARIO,
        MERMA,
        ROBO,
        DEVOLUCION_CLIENTE,
        DEVOLUCION_PROVEEDOR,
        PROMOCION,
        MUESTRA,
        TRANSFERENCIA,
        OTROS
    }
} 