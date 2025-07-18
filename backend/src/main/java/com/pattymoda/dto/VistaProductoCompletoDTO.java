package com.pattymoda.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
public class VistaProductoCompletoDTO {
    private Long id;
    private String codigoProducto;
    private String nombre;
    private String sku;
    private String descripcion;
    private String descripcionCorta;
    private String categoriaNombre;
    private Long categoriaId;
    private String marcaNombre;
    private Long marcaId;
    private BigDecimal precio;
    private BigDecimal precioOferta;
    private BigDecimal costo;
    private BigDecimal margenPorcentaje;
    private Integer stock;
    private Integer stockMinimo;
    private Integer stockMaximo;
    private Integer stockDisponible;
    private String imagenPrincipal;
    private Boolean requiereTalla;
    private Boolean requiereColor;
    private Boolean destacado;
    private Boolean activo;
    private Timestamp fechaCreacion;
    private String estadoStock;
} 