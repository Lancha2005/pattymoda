package com.pattymoda.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductoUpdateDTO {
    
    @NotBlank(message = "El nombre del producto es obligatorio")
    @Size(max = 255, message = "El nombre no puede exceder 255 caracteres")
    private String nombre;
    
    @Size(max = 2000, message = "La descripción no puede exceder 2000 caracteres")
    private String descripcion;
    
    @Size(max = 500, message = "La descripción corta no puede exceder 500 caracteres")
    private String descripcionCorta;
    
    @NotNull(message = "La categoría es obligatoria")
    private Long categoriaId;
    
    private Long marcaId;
    
    @Size(max = 100, message = "El modelo no puede exceder 100 caracteres")
    private String modelo;
    
    @DecimalMin(value = "0.0", inclusive = false, message = "El peso debe ser mayor a 0")
    @Digits(integer = 5, fraction = 3, message = "El peso debe tener máximo 5 dígitos enteros y 3 decimales")
    private BigDecimal peso;
    
    @Size(max = 100, message = "Las dimensiones no pueden exceder 100 caracteres")
    private String dimensiones;
    
    @Size(max = 255, message = "La URL de imagen no puede exceder 255 caracteres")
    private String imagenPrincipal;
    
    private Boolean requiereTalla;
    
    private Boolean requiereColor;
    
    private Boolean esPerecedero;
    
    @Min(value = 1, message = "El tiempo de entrega debe ser al menos 1 día")
    @Max(value = 365, message = "El tiempo de entrega no puede exceder 365 días")
    private Integer tiempoEntregaDias;
    
    @Min(value = 0, message = "La garantía no puede ser negativa")
    @Max(value = 120, message = "La garantía no puede exceder 120 meses")
    private Integer garantiaMeses;
    
    private Boolean destacado;
    
    private Boolean nuevo;
    
    private Boolean activo;
}