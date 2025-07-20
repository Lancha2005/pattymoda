package com.pattymoda.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "configuracion_tienda")
@Data
@EqualsAndHashCode(callSuper = true)
public class ConfiguracionTienda extends BaseEntity {
    
    @Column(name = "nombre", length = 100, nullable = false)
    private String nombre;
    
    @Column(name = "slogan", length = 200)
    private String slogan;
    
    @Column(name = "descripcion")
    private String descripcion;
    
    @Column(name = "ruc", length = 11)
    private String ruc;
    
    @Column(name = "direccion", length = 255)
    private String direccion;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "distrito_id")
    private Distrito distrito;
    
    @Column(name = "telefono", length = 20)
    private String telefono;
    
    @Column(name = "telefono_secundario", length = 20)
    private String telefonoSecundario;
    
    @Column(name = "email", length = 100)
    private String email;
    
    @Column(name = "email_ventas", length = 100)
    private String emailVentas;
    
    @Column(name = "email_soporte", length = 100)
    private String emailSoporte;
    
    @Column(name = "sitio_web", length = 100)
    private String sitioWeb;
    
    @Column(name = "facebook", length = 100)
    private String facebook;
    
    @Column(name = "instagram", length = 100)
    private String instagram;
    
    @Column(name = "whatsapp", length = 20)
    private String whatsapp;
    
    @Column(name = "logo", length = 255)
    private String logo;
    
    @Column(name = "favicon", length = 255)
    private String favicon;
    
    @Column(name = "moneda_principal", length = 3)
    private String monedaPrincipal = "PEN";
    
    @Column(name = "idioma_principal", length = 5)
    private String idiomaPrincipal = "es_PE";
    
    @Column(name = "zona_horaria", length = 50)
    private String zonaHoraria = "America/Lima";
    
    @Column(name = "activo")
    private Boolean activo = true;
} 