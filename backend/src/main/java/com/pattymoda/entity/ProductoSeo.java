package com.pattymoda.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

@Entity
@Table(name = "productos_seo")
@Data
@EqualsAndHashCode(callSuper = true)
public class ProductoSeo extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false, unique = true)
    private Producto producto;

    @Column(name = "meta_title", length = 255)
    private String metaTitle;

    @Column(name = "meta_description", length = 500)
    private String metaDescription;

    @Column(name = "slug", length = 255, unique = true)
    private String slug;

    @Column(name = "tags", columnDefinition = "LONGTEXT")
    private String tags;

    @Column(name = "palabras_clave", columnDefinition = "TEXT")
    private String palabrasClave;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion = LocalDateTime.now();
} 