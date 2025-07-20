package com.pattymoda.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

@Entity
@Table(name = "auditoria_logs")
@Data
@EqualsAndHashCode(callSuper = true)
public class AuditoriaLog extends BaseEntity {
    @Column(name = "tabla", length = 50, nullable = false)
    private String tabla;

    @Column(name = "registro_id", nullable = false)
    private Long registroId;

    @Enumerated(EnumType.STRING)
    @Column(name = "accion", nullable = false)
    private Accion accion;

    @Column(name = "datos_anteriores", columnDefinition = "LONGTEXT")
    private String datosAnteriores;

    @Column(name = "datos_nuevos", columnDefinition = "LONGTEXT")
    private String datosNuevos;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    @Column(name = "user_agent", length = 500)
    private String userAgent;

    @Column(name = "fecha_accion", nullable = false)
    private LocalDateTime fechaAccion;

    public enum Accion {
        INSERT, UPDATE, DELETE
    }
} 