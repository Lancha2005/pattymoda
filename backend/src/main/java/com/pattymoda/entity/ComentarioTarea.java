package com.pattymoda.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

@Entity
@Table(name = "comentarios_tarea")
@Data
@EqualsAndHashCode(callSuper = true)
public class ComentarioTarea extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tarea_id", nullable = false)
    private TareaSeguimiento tarea;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(name = "comentario", columnDefinition = "TEXT", nullable = false)
    private String comentario;

    @Column(name = "fecha_comentario", nullable = false)
    private LocalDateTime fechaComentario = LocalDateTime.now();
} 