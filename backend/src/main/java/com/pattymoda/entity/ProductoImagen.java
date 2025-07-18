package com.pattymoda.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

@Entity
@Table(name = "productos_imagenes")
@Data
@EqualsAndHashCode(callSuper = true)
public class ProductoImagen extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @Column(name = "url_imagen", nullable = false, length = 255)
    private String urlImagen;

    @Column(name = "alt_text", length = 255)
    private String altText;

    @Column(name = "orden")
    private Integer orden = 0;

    @Column(name = "es_principal")
    private Boolean esPrincipal = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "color_id")
    private Color color;

    @Column(name = "activo")
    private Boolean activo = true;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion = LocalDateTime.now();
} 