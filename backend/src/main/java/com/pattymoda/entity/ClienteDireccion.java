package com.pattymoda.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "clientes_direcciones")
@Data
@EqualsAndHashCode(callSuper = true)
public class ClienteDireccion extends BaseEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;
    
    @Column(name = "tipo_direccion", length = 50)
    private String tipoDireccion;
    
    @Column(name = "direccion", length = 255, nullable = false)
    private String direccion;
    
    @Column(name = "referencia", length = 200)
    private String referencia;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "distrito_id")
    private Distrito distrito;
    
    @Column(name = "codigo_postal", length = 10)
    private String codigoPostal;
    
    @Column(name = "es_principal")
    private Boolean esPrincipal = false;
    
    @Column(name = "activo")
    private Boolean activo = true;
} 