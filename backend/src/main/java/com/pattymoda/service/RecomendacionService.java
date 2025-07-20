package com.pattymoda.service;

import com.pattymoda.entity.Cliente;
import com.pattymoda.entity.Producto;
import com.pattymoda.repository.ProductoRepository;
import com.pattymoda.repository.DetalleVentaRepository;
import com.pattymoda.repository.ClientePreferenciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecomendacionService {

    @Autowired
    private ProductoRepository productoRepository;
    
    @Autowired
    private DetalleVentaRepository detalleVentaRepository;
    
    @Autowired
    private ClientePreferenciaRepository preferenciaRepository;

    public List<Producto> getRecomendacionesParaCliente(Long clienteId, int limite) {
        // Obtener preferencias del cliente
        var preferencias = preferenciaRepository.findByClienteId(clienteId);
        
        if (!preferencias.isEmpty()) {
            var preferencia = preferencias.get(0);
            
            // Recomendar basado en categoría preferida
            if (preferencia.getCategoriaPreferida() != null) {
                return productoRepository.findByCategoriaId(
                    preferencia.getCategoriaPreferida().getId(), 
                    PageRequest.of(0, limite)
                ).getContent();
            }
            
            // Recomendar basado en marca preferida
            if (preferencia.getMarcaPreferida() != null) {
                return productoRepository.findByMarcaId(
                    preferencia.getMarcaPreferida().getId(), 
                    PageRequest.of(0, limite)
                ).getContent();
            }
        }
        
        // Fallback: productos destacados
        return productoRepository.findByDestacado(true, PageRequest.of(0, limite)).getContent();
    }

    public List<Producto> getProductosRelacionados(Long productoId, int limite) {
        Producto producto = productoRepository.findById(productoId)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        
        // Buscar productos de la misma categoría
        return productoRepository.findByCategoriaId(
            producto.getCategoria().getId(), 
            PageRequest.of(0, limite + 1)
        ).getContent()
        .stream()
        .filter(p -> !p.getId().equals(productoId))
        .limit(limite)
        .collect(Collectors.toList());
    }

    public List<Producto> getProductosPopulares(int limite) {
        // Productos más vendidos (requiere implementar query específica)
        return productoRepository.findByDestacado(true, PageRequest.of(0, limite)).getContent();
    }

    public List<Producto> getProductosNuevos(int limite) {
        return productoRepository.findByNuevo(true, PageRequest.of(0, limite)).getContent();
    }

    public List<Producto> getProductosEnOferta(int limite) {
        // Productos con precio de oferta (requiere join con ProductoPrecio)
        return productoRepository.findByDestacado(true, PageRequest.of(0, limite)).getContent();
    }
}