package com.pattymoda.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;

@Entity
@Table(name = "productos")
@Data
@EqualsAndHashCode(callSuper = true)
public class Producto extends BaseEntity {

    @NotBlank(message = "El código del producto es obligatorio")
    @Size(max = 50, message = "El código no puede exceder 50 caracteres")
    @Column(name = "codigo_producto", length = 50, nullable = false, unique = true)
    private String codigoProducto;

    @NotBlank(message = "El nombre del producto es obligatorio")
    @Size(max = 255, message = "El nombre no puede exceder 255 caracteres")
    @Column(name = "nombre", length = 255, nullable = false)
    private String nombre;

    @NotBlank(message = "El SKU es obligatorio")
    @Size(max = 100, message = "El SKU no puede exceder 100 caracteres")
    @Column(name = "sku", length = 100, nullable = false, unique = true)
    private String sku;

    @Size(max = 50, message = "El código de barras no puede exceder 50 caracteres")
    @Column(name = "codigo_barras", length = 50, unique = true)
    private String codigoBarras;

    @Size(max = 2000, message = "La descripción no puede exceder 2000 caracteres")
    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Size(max = 500, message = "La descripción corta no puede exceder 500 caracteres")
    @Column(name = "descripcion_corta", length = 500)
    private String descripcionCorta;

    @NotNull(message = "La categoría es obligatoria")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "marca_id")
    private Marca marca;

    @Size(max = 100, message = "El modelo no puede exceder 100 caracteres")
    @Column(name = "modelo", length = 100)
    private String modelo;

    @DecimalMin(value = "0.0", inclusive = false, message = "El peso debe ser mayor a 0")
    @Digits(integer = 5, fraction = 3, message = "El peso debe tener máximo 5 dígitos enteros y 3 decimales")
    @Column(name = "peso", precision = 8, scale = 3)
    private BigDecimal peso;

    @Size(max = 100, message = "Las dimensiones no pueden exceder 100 caracteres")
    @Column(name = "dimensiones", length = 100)
    private String dimensiones;

    @Size(max = 255, message = "La URL de imagen no puede exceder 255 caracteres")
    @Column(name = "imagen_principal", length = 255)
    private String imagenPrincipal;

    @Column(name = "requiere_talla")
    private Boolean requiereTalla = true;

    @Column(name = "requiere_color")
    private Boolean requiereColor = true;

    @Column(name = "es_perecedero")
    private Boolean esPerecedero = false;

    @Min(value = 1, message = "El tiempo de entrega debe ser al menos 1 día")
    @Max(value = 365, message = "El tiempo de entrega no puede exceder 365 días")
    @Column(name = "tiempo_entrega_dias")
    private Integer tiempoEntregaDias = 1;

    @Min(value = 0, message = "La garantía no puede ser negativa")
    @Max(value = 120, message = "La garantía no puede exceder 120 meses")
    @Column(name = "garantia_meses")
    private Integer garantiaMeses = 0;

    @Column(name = "destacado")
    private Boolean destacado = false;

    @Column(name = "nuevo")
    private Boolean nuevo = true;

    @Column(name = "activo")
    private Boolean activo = true;

    // Relación con inventario
    @OneToOne(mappedBy = "producto", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private ProductoInventario inventario;
}