package com.pattymoda.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
public class VistaClienteEstadisticaDTO {
    private Long id;
    private String codigoCliente;
    private String nombre;
    private String apellido;
    private String numeroDocumento;
    private String tipoDocumento;
    private String tipoCliente;
    private BigDecimal totalCompras;
    private Integer cantidadCompras;
    private Timestamp ultimaCompra;
    private BigDecimal limiteCredito;
    private BigDecimal descuentoPersonalizado;
    private Integer puntosDisponibles;
    private String nivelCliente;
    private Boolean activo;
    private String categoriaCliente;
    private BigDecimal ticketPromedio;
} 