package com.pattymoda.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "ventas_facturacion")
@Data
@EqualsAndHashCode(callSuper = true)
public class VentaFacturacion extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venta_id", nullable = false, unique = true)
    private Venta venta;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_comprobante")
    private TipoComprobante tipoComprobante = TipoComprobante.BOLETA;

    @Column(name = "serie_comprobante", length = 10)
    private String serieComprobante;

    @Column(name = "numero_comprobante", length = 20)
    private String numeroComprobante;

    @Column(name = "fecha_emision")
    private LocalDate fechaEmision;

    @Column(name = "moneda", length = 3)
    private String moneda = "PEN";

    @Column(name = "tipo_cambio", precision = 8, scale = 4)
    private BigDecimal tipoCambio = BigDecimal.ONE;

    @Column(name = "direccion_facturacion")
    private String direccionFacturacion;

    @Column(name = "datos_adicionales", columnDefinition = "LONGTEXT")
    private String datosAdicionales;

    @Column(name = "hash_comprobante", length = 255)
    private String hashComprobante;

    @Column(name = "enviado_sunat")
    private Boolean enviadoSunat = false;

    @Column(name = "fecha_envio_sunat")
    private java.time.LocalDateTime fechaEnvioSunat;

    public enum TipoComprobante {
        BOLETA, FACTURA, NOTA_VENTA, TICKET
    }
} 