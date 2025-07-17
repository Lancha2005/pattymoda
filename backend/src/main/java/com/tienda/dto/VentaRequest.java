package com.tienda.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public class VentaRequest {
    @NotNull(message = "El cliente es requerido")
    private Long clienteId;
    
    @NotEmpty(message = "Los productos son requeridos")
    private List<DetalleVentaRequest> productos;
    
    // Constructors
    public VentaRequest() {}
    
    public VentaRequest(Long clienteId, List<DetalleVentaRequest> productos) {
        this.clienteId = clienteId;
        this.productos = productos;
    }
    
    // Getters and Setters
    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }
    
    public List<DetalleVentaRequest> getProductos() { return productos; }
    public void setProductos(List<DetalleVentaRequest> productos) { this.productos = productos; }
    
    public static class DetalleVentaRequest {
        @NotNull(message = "El producto es requerido")
        private Long productoId;
        
        @NotNull(message = "La cantidad es requerida")
        private Integer cantidad;
        
        // Constructors
        public DetalleVentaRequest() {}
        
        public DetalleVentaRequest(Long productoId, Integer cantidad) {
            this.productoId = productoId;
            this.cantidad = cantidad;
        }
        
        // Getters and Setters
        public Long getProductoId() { return productoId; }
        public void setProductoId(Long productoId) { this.productoId = productoId; }
        
        public Integer getCantidad() { return cantidad; }
        public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
    }
}