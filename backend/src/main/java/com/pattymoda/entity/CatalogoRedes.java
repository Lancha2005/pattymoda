package com.pattymoda.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

@Entity
@Table(name = "catalogo_redes")
@Data
@EqualsAndHashCode(callSuper = true)
public class CatalogoRedes extends BaseEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "plataforma", nullable = false)
    private PlataformaRed plataforma;
    
    @Column(name = "id_publicacion", length = 100)
    private String idPublicacion;
    
    @Column(name = "url_publicacion", length = 500)
    private String urlPublicacion;
    
    @Column(name = "titulo_publicacion", length = 255)
    private String tituloPublicacion;
    
    @Column(name = "descripcion_publicacion", length = 1000)
    private String descripcionPublicacion;
    
    @Column(name = "hashtags", length = 500)
    private String hashtags;
    
    @Column(name = "imagen_publicacion", length = 255)
    private String imagenPublicacion;
    
    @Column(name = "fecha_publicacion")
    private LocalDateTime fechaPublicacion;
    
    @Column(name = "activo")
    private Boolean activo = true;
    
    @Column(name = "publicado")
    private Boolean publicado = false;
    
    @Column(name = "likes")
    private Integer likes = 0;
    
    @Column(name = "comentarios")
    private Integer comentarios = 0;
    
    @Column(name = "compartidos")
    private Integer compartidos = 0;
    
    @Column(name = "clicks")
    private Integer clicks = 0;
    
    @Column(name = "conversiones")
    private Integer conversiones = 0;
    
    public enum PlataformaRed {
        INSTAGRAM, FACEBOOK, TIKTOK, PINTEREST, WHATSAPP_BUSINESS
    }
}