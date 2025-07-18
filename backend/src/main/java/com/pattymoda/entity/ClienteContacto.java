package com.pattymoda.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "clientes_contacto")
@Data
@EqualsAndHashCode(callSuper = true)
public class ClienteContacto extends BaseEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_contacto", nullable = false)
    private TipoContacto tipoContacto;
    
    @Column(name = "valor", length = 100, nullable = false)
    private String valor;
    
    @Column(name = "descripcion", length = 200)
    private String descripcion;
    
    @Column(name = "es_principal")
    private Boolean esPrincipal = false;
    
    @Column(name = "activo")
    private Boolean activo = true;
    
    public enum TipoContacto {
        TELEFONO,
        CELULAR,
        EMAIL,
        WHATSAPP,
        FACEBOOK,
        INSTAGRAM,
        DIRECCION,
        OTROS
    }
} 