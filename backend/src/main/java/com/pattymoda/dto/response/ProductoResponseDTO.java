package com.pattymoda.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ProductoResponseDTO {
    private Long id;
    private String codigoProducto;
    private String nombre;
    private String sku;
    private String codigoBarras;
    private String descripcion;
    private String descripcionCorta;
    private CategoriaSimpleDTO categoria;
    private MarcaSimpleDTO marca;
    private String modelo;
    private BigDecimal peso;
    private String dimensiones;
    private String imagenPrincipal;
    private Boolean requiereTalla;
    private Boolean requiereColor;
    private Boolean esPerecedero;
    private Integer tiempoEntregaDias;
    private Integer garantiaMeses;
    private Boolean destacado;
    private Boolean nuevo;
    private Boolean activo;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    
    @Data
    public static class CategoriaSimpleDTO {
        private Long id;
        private String codigo;
        private String nombre;
    }
    
    @Data
    public static class MarcaSimpleDTO {
        private Long id;
        private String nombre;
        private String logo;
    }
}