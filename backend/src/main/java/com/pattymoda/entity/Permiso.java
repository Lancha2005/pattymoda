package com.pattymoda.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "permisos")
@Data
@EqualsAndHashCode(callSuper = true)
public class Permiso extends BaseEntity {
    @Column(name = "codigo", length = 50, nullable = false, unique = true)
    private String codigo;

    @Column(name = "modulo", length = 50, nullable = false)
    private String modulo;

    @Column(name = "accion", length = 50, nullable = false)
    private String accion;

    @Column(name = "descripcion", length = 200)
    private String descripcion;

    @Column(name = "activo")
    private Boolean activo = true;
} 