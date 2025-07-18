package com.pattymoda.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

@Entity
@Table(name = "clientes_preferencias")
@Data
@EqualsAndHashCode(callSuper = true)
public class ClientePreferencia extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_preferida_id")
    private Categoria categoriaPreferida;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "talla_preferida_id")
    private Talla tallaPreferida;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "color_preferido_id")
    private Color colorPreferido;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "marca_preferida_id")
    private Marca marcaPreferida;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "canal_preferido_id")
    private CanalVenta canalPreferido;

    @Column(name = "recibir_promociones")
    private Boolean recibirPromociones = true;

    @Column(name = "recibir_newsletter")
    private Boolean recibirNewsletter = true;

    @Enumerated(EnumType.STRING)
    @Column(name = "frecuencia_contacto")
    private FrecuenciaContacto frecuenciaContacto = FrecuenciaContacto.MENSUAL;

    @Column(name = "notas", columnDefinition = "TEXT")
    private String notas;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion = LocalDateTime.now();

    public enum FrecuenciaContacto {
        DIARIO, SEMANAL, MENSUAL, NUNCA
    }
} 