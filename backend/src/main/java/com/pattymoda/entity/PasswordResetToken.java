package com.pattymoda.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

@Entity
@Table(name = "password_reset_tokens")
@Data
@EqualsAndHashCode(callSuper = true)
public class PasswordResetToken extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(name = "token", length = 255, nullable = false, unique = true)
    private String token;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_expiracion", nullable = false)
    private LocalDateTime fechaExpiracion;

    @Column(name = "usado")
    private Boolean usado = false;

    @Column(name = "ip_solicitud", length = 45)
    private String ipSolicitud;

    @Column(name = "user_agent", length = 500)
    private String userAgent;
} 