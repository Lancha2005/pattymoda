package com.pattymoda.repository;

public interface VistaClienteEstadisticaProjection {
    Long getId();

    String getCodigoCliente();

    String getNombre();

    String getApellido();

    String getNumeroDocumento();

    String getTipoDocumento();

    String getTipoCliente();

    Double getTotalCompras();

    Integer getCantidadCompras();

    java.sql.Timestamp getUltimaCompra();

    Double getLimiteCredito();

    Double getDescuentoPersonalizado();

    Integer getPuntosDisponibles();

    String getNivelCliente();

    Boolean getActivo();

    String getCategoriaCliente();

    Double getTicketPromedio();
}
