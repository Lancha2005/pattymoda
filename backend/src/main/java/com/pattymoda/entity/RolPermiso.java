package com.pattymoda.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

@Entity
@Table(name = "roles_permisos")
@Data
@EqualsAndHashCode(callSuper = true)
public class RolPermiso extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rol_id", nullable = false)
    private Rol rol;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "permiso_id", nullable = false)
    private Permiso permiso;

    @Column(name = "concedido")
    private Boolean concedido = true;

    @Column(name = "fecha_asignacion", nullable = false)
    private LocalDateTime fechaAsignacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asignado_por")
    private Usuario asignadoPor;
} 