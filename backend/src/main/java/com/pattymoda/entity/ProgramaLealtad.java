package com.pattymoda.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

@Entity
@Table(name = "programa_lealtad")
@Data
@EqualsAndHashCode(callSuper = true)
public class ProgramaLealtad extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false, unique = true)
    private Cliente cliente;

    @Column(name = "puntos_acumulados")
    private Integer puntosAcumulados = 0;

    @Column(name = "puntos_canjeados")
    private Integer puntosCanjeados = 0;

    @Column(name = "nivel_cliente")
    @Enumerated(EnumType.STRING)
    private NivelCliente nivelCliente = NivelCliente.BRONCE;

    @Column(name = "fecha_ultimo_movimiento")
    private LocalDateTime fechaUltimoMovimiento;

    @Column(name = "activo")
    private Boolean activo = true;

    public enum NivelCliente {
        BRONCE, PLATA, ORO, PLATINO
    }
} 