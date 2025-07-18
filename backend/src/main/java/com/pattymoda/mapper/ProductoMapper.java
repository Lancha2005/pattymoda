package com.pattymoda.mapper;

import com.pattymoda.dto.request.ProductoCreateDTO;
import com.pattymoda.dto.request.ProductoUpdateDTO;
import com.pattymoda.dto.response.ProductoResponseDTO;
import com.pattymoda.entity.Categoria;
import com.pattymoda.entity.Marca;
import com.pattymoda.entity.Producto;
import org.springframework.stereotype.Component;

@Component
public class ProductoMapper {
    
    public Producto toEntity(ProductoCreateDTO dto) {
        Producto producto = new Producto();
        producto.setCodigoProducto(dto.getCodigoProducto());
        producto.setNombre(dto.getNombre());
        producto.setSku(dto.getSku());
        producto.setCodigoBarras(dto.getCodigoBarras());
        producto.setDescripcion(dto.getDescripcion());
        producto.setDescripcionCorta(dto.getDescripcionCorta());
        producto.setModelo(dto.getModelo());
        producto.setPeso(dto.getPeso());
        producto.setDimensiones(dto.getDimensiones());
        producto.setImagenPrincipal(dto.getImagenPrincipal());
        producto.setRequiereTalla(dto.getRequiereTalla());
        producto.setRequiereColor(dto.getRequiereColor());
        producto.setEsPerecedero(dto.getEsPerecedero());
        producto.setTiempoEntregaDias(dto.getTiempoEntregaDias());
        producto.setGarantiaMeses(dto.getGarantiaMeses());
        producto.setDestacado(dto.getDestacado());
        producto.setNuevo(dto.getNuevo());
        producto.setActivo(dto.getActivo());
        
        // Las relaciones se setean en el service
        return producto;
    }
    
    public void updateEntity(Producto producto, ProductoUpdateDTO dto) {
        producto.setNombre(dto.getNombre());
        producto.setDescripcion(dto.getDescripcion());
        producto.setDescripcionCorta(dto.getDescripcionCorta());
        producto.setModelo(dto.getModelo());
        producto.setPeso(dto.getPeso());
        producto.setDimensiones(dto.getDimensiones());
        producto.setImagenPrincipal(dto.getImagenPrincipal());
        
        if (dto.getRequiereTalla() != null) {
            producto.setRequiereTalla(dto.getRequiereTalla());
        }
        if (dto.getRequiereColor() != null) {
            producto.setRequiereColor(dto.getRequiereColor());
        }
        if (dto.getEsPerecedero() != null) {
            producto.setEsPerecedero(dto.getEsPerecedero());
        }
        if (dto.getTiempoEntregaDias() != null) {
            producto.setTiempoEntregaDias(dto.getTiempoEntregaDias());
        }
        if (dto.getGarantiaMeses() != null) {
            producto.setGarantiaMeses(dto.getGarantiaMeses());
        }
        if (dto.getDestacado() != null) {
            producto.setDestacado(dto.getDestacado());
        }
        if (dto.getNuevo() != null) {
            producto.setNuevo(dto.getNuevo());
        }
        if (dto.getActivo() != null) {
            producto.setActivo(dto.getActivo());
        }
    }
    
    public ProductoResponseDTO toResponseDTO(Producto producto) {
        ProductoResponseDTO dto = new ProductoResponseDTO();
        dto.setId(producto.getId());
        dto.setCodigoProducto(producto.getCodigoProducto());
        dto.setNombre(producto.getNombre());
        dto.setSku(producto.getSku());
        dto.setCodigoBarras(producto.getCodigoBarras());
        dto.setDescripcion(producto.getDescripcion());
        dto.setDescripcionCorta(producto.getDescripcionCorta());
        dto.setModelo(producto.getModelo());
        dto.setPeso(producto.getPeso());
        dto.setDimensiones(producto.getDimensiones());
        dto.setImagenPrincipal(producto.getImagenPrincipal());
        dto.setRequiereTalla(producto.getRequiereTalla());
        dto.setRequiereColor(producto.getRequiereColor());
        dto.setEsPerecedero(producto.getEsPerecedero());
        dto.setTiempoEntregaDias(producto.getTiempoEntregaDias());
        dto.setGarantiaMeses(producto.getGarantiaMeses());
        dto.setDestacado(producto.getDestacado());
        dto.setNuevo(producto.getNuevo());
        dto.setActivo(producto.getActivo());
        dto.setFechaCreacion(producto.getFechaCreacion());
        dto.setFechaActualizacion(producto.getFechaActualizacion());
        
        // Mapear categoria
        if (producto.getCategoria() != null) {
            ProductoResponseDTO.CategoriaSimpleDTO categoriaDTO = new ProductoResponseDTO.CategoriaSimpleDTO();
            categoriaDTO.setId(producto.getCategoria().getId());
            categoriaDTO.setCodigo(producto.getCategoria().getCodigo());
            categoriaDTO.setNombre(producto.getCategoria().getNombre());
            dto.setCategoria(categoriaDTO);
        }
        
        // Mapear marca
        if (producto.getMarca() != null) {
            ProductoResponseDTO.MarcaSimpleDTO marcaDTO = new ProductoResponseDTO.MarcaSimpleDTO();
            marcaDTO.setId(producto.getMarca().getId());
            marcaDTO.setNombre(producto.getMarca().getNombre());
            marcaDTO.setLogo(producto.getMarca().getLogo());
            dto.setMarca(marcaDTO);
        }
        
        return dto;
    }
}