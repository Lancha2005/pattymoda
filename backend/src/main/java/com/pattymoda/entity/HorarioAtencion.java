package com.pattymoda.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "horarios_atencion")
@Data
@EqualsAndHashCode(callSuper = true)
public class HorarioAtencion extends BaseEntity {
    @Enumerated(EnumType.STRING)
    @Column(name = "dia_semana", nullable = false, unique = true)
    private DiaSemana diaSemana;

    @Column(name = "hora_apertura")
    private String horaApertura;

    @Column(name = "hora_cierre")
    private String horaCierre;

    @Column(name = "cerrado")
    private Boolean cerrado = false;

    @Column(name = "observaciones", length = 200)
    private String observaciones;

    @Column(name = "activo")
    private Boolean activo = true;

    public enum DiaSemana {
        LUNES, MARTES, MIERCOLES, JUEVES, VIERNES, SABADO, DOMINGO
    }
} 