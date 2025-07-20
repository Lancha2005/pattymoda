package com.pattymoda.service;

import com.pattymoda.entity.ProductoTallaColor;
import com.pattymoda.repository.ProductoTallaColorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductoTallaColorService extends BaseService<ProductoTallaColor, Long> {

    private final ProductoTallaColorRepository repository;

    @Autowired
    public ProductoTallaColorService(ProductoTallaColorRepository repository) {
        super(repository);
        this.repository = repository;
    }

    @Cacheable(value = "inventario", key = "'producto-' + #productoId")
    public List<ProductoTallaColor> getCombinacionesByProducto(Long productoId) {
        return repository.findByProductoIdAndActivoTrue(productoId);
    }

    public Optional<ProductoTallaColor> getCombinacionEspecifica(Long productoId, Long tallaId, Long colorId) {
        return repository.findByProductoIdAndTallaIdAndColorId(productoId, tallaId, colorId);
    }

    public List<ProductoTallaColor> getCombinacionesDisponibles(Long productoId) {
        return repository.findDisponiblesByProducto(productoId);
    }

    public List<ProductoTallaColor> getCombinacionesStockBajo() {
        return repository.findStockBajo();
    }

    public List<ProductoTallaColor> getCombinacionesSinStock() {
        return repository.findSinStock();
    }

    public Integer getStockTotalProducto(Long productoId) {
        Integer stock = repository.sumStockByProducto(productoId);
        return stock != null ? stock : 0;
    }

    public long countCombinacionesDisponibles(Long productoId) {
        return repository.countCombinacionesDisponibles(productoId);
    }

    @CacheEvict(value = "inventario", key = "'producto-' + #combinacion.producto.id")
    public ProductoTallaColor actualizarStock(ProductoTallaColor combinacion, Integer nuevoStock) {
        combinacion.setStockActual(nuevoStock);
        combinacion.setStockDisponible(nuevoStock - (combinacion.getStockReservado() != null ? combinacion.getStockReservado() : 0));
        return save(combinacion);
    }

    @CacheEvict(value = "inventario", key = "'producto-' + #combinacion.producto.id")
    public ProductoTallaColor reservarStock(ProductoTallaColor combinacion, Integer cantidad) {
        if (combinacion.getStockDisponible() < cantidad) {
            throw new RuntimeException("Stock insuficiente para reservar");
        }
        
        Integer stockReservadoActual = combinacion.getStockReservado() != null ? combinacion.getStockReservado() : 0;
        combinacion.setStockReservado(stockReservadoActual + cantidad);
        combinacion.setStockDisponible(combinacion.getStockActual() - combinacion.getStockReservado());
        
        return save(combinacion);
    }

    @CacheEvict(value = "inventario", key = "'producto-' + #combinacion.producto.id")
    public ProductoTallaColor liberarStock(ProductoTallaColor combinacion, Integer cantidad) {
        Integer stockReservadoActual = combinacion.getStockReservado() != null ? combinacion.getStockReservado() : 0;
        Integer nuevoStockReservado = Math.max(0, stockReservadoActual - cantidad);
        
        combinacion.setStockReservado(nuevoStockReservado);
        combinacion.setStockDisponible(combinacion.getStockActual() - nuevoStockReservado);
        
        return save(combinacion);
    }

    @Override
    public ProductoTallaColor save(ProductoTallaColor combinacion) {
        if (combinacion.getProducto() == null) {
            throw new IllegalArgumentException("El producto es obligatorio");
        }
        if (combinacion.getTalla() == null) {
            throw new IllegalArgumentException("La talla es obligatoria");
        }
        if (combinacion.getColor() == null) {
            throw new IllegalArgumentException("El color es obligatorio");
        }

        // Generar SKU de combinación automáticamente si no se proporciona
        if (combinacion.getSkuCombinacion() == null || combinacion.getSkuCombinacion().trim().isEmpty()) {
            combinacion.setSkuCombinacion(generarSkuCombinacion(combinacion));
        }

        // Verificar unicidad de SKU
        if (combinacion.getId() == null && repository.existsBySkuCombinacion(combinacion.getSkuCombinacion())) {
            throw new IllegalArgumentException("Ya existe una combinación con ese SKU");
        }

        return super.save(combinacion);
    }

    private String generarSkuCombinacion(ProductoTallaColor combinacion) {
        return String.format("%s-%s-%s", 
            combinacion.getProducto().getSku(),
            combinacion.getTalla().getCodigo(),
            combinacion.getColor().getCodigo());
    }
}