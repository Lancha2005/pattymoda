package com.pattymoda.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
public class VistaVentaCompletaDTO {
    private Long id;
    private String numeroVenta;
    private Timestamp fecha;
    private BigDecimal subtotal;
    private BigDecimal total;
    private String estado;
    private String tipoComprobante;
    private String serieComprobante;
    private String numeroComprobante;
    private String canalVenta;
    private String clienteNombre;
    private String clienteDocumento;
    private String vendedorNombre;
    private Integer cantidadItems;
    private BigDecimal comisionVendedor;
    private String estadoEntrega;
    private Timestamp fechaEntregaReal;
} 