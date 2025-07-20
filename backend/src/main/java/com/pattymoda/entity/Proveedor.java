package com.pattymoda.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;
import java.math.BigDecimal;

@Entity
@Table(name = "proveedores")
@Data
@EqualsAndHashCode(callSuper = true)
public class Proveedor extends BaseEntity {
    @Column(name = "codigo_proveedor", length = 20, unique = true)
    private String codigoProveedor;

    @Column(name = "razon_social", length = 200, nullable = false)
    private String razonSocial;

    @Column(name = "nombre_comercial", length = 200)
    private String nombreComercial;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_documento")
    private TipoDocumento tipoDocumento = TipoDocumento.RUC;

    @Column(name = "numero_documento", length = 20)
    private String numeroDocumento;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "telefono", length = 20)
    private String telefono;

    @Column(name = "direccion", length = 255)
    private String direccion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "distrito_id")
    private Distrito distrito;

    @Column(name = "contacto_principal", length = 100)
    private String contactoPrincipal;

    @Column(name = "telefono_contacto", length = 20)
    private String telefonoContacto;

    @Column(name = "email_contacto", length = 100)
    private String emailContacto;

    @Column(name = "condiciones_pago", length = 200)
    private String condicionesPago;

    @Column(name = "tiempo_entrega_dias")
    private Integer tiempoEntregaDias;

    @Column(name = "calificacion", precision = 3, scale = 2)
    private BigDecimal calificacion = BigDecimal.ZERO;

    @Column(name = "activo")
    private Boolean activo = true;

    @Column(name = "notas")
    private String notas;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    public enum TipoDocumento {
        RUC, DNI, PASAPORTE
    }
} 