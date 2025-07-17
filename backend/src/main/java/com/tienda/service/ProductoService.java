package com.tienda.service;

import com.tienda.entity.Producto;
import com.tienda.entity.ProductoTalla;
import com.tienda.entity.ProductoColor;
import com.tienda.entity.ProductoImagen;
import com.tienda.repository.ProductoRepository;
import com.tienda.repository.ProductoTallaRepository;
import com.tienda.repository.ProductoColorRepository;
import com.tienda.repository.ProductoImagenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {
    
    @Autowired
    private ProductoRepository productoRepository;
    
    @Autowired
    private ProductoTallaRepository productoTallaRepository;
    
    @Autowired
    private ProductoColorRepository productoColorRepository;
    
    @Autowired
    private ProductoImagenRepository productoImagenRepository;
    
    public List<Producto> obtenerTodosLosProductos() {
        return productoRepository.findAll();
    }
    
    public List<Producto> obtenerProductosActivos() {
        return productoRepository.findByActivoTrue();
    }
    
    public Optional<Producto> obtenerProductoPorId(Long id) {
        return productoRepository.findById(id);
    }
    
    public Optional<Producto> obtenerProductoPorSku(String sku) {
        return productoRepository.findBySku(sku);
    }
    
    public List<Producto> obtenerProductosPorCategoria(Long categoriaId) {
        return productoRepository.findByCategoriaId(categoriaId);
    }
    
    public List<Producto> buscarProductos(String query) {
        return productoRepository.findByNombreContainingIgnoreCaseOrSkuContainingIgnoreCase(query, query);
    }
    
    public List<Producto> obtenerProductosPorMarca(String marca) {
        return productoRepository.findByMarca(marca);
    }
    
    @Transactional
    public Producto crearProducto(Producto producto) {
        // Generar SKU si no existe
        if (producto.getSku() == null || producto.getSku().isEmpty()) {
            producto.setSku(generarSku(producto));
        }
        
        // Validar SKU único
        if (productoRepository.findBySku(producto.getSku()).isPresent()) {
            throw new RuntimeException("Ya existe un producto con el SKU: " + producto.getSku());
        }
        
        return productoRepository.save(producto);
    }
    
    @Transactional
    public Producto actualizarProducto(Long id, Producto productoActualizado) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        
        // Validar SKU único si se cambió
        if (!producto.getSku().equals(productoActualizado.getSku())) {
            if (productoRepository.findBySku(productoActualizado.getSku()).isPresent()) {
                throw new RuntimeException("Ya existe un producto con el SKU: " + productoActualizado.getSku());
            }
        }
        
        producto.setNombre(productoActualizado.getNombre());
        producto.setSku(productoActualizado.getSku());
        producto.setDescripcion(productoActualizado.getDescripcion());
        producto.setMarca(productoActualizado.getMarca());
        producto.setPrecio(productoActualizado.getPrecio());
        producto.setCosto(productoActualizado.getCosto());
        producto.setStock(productoActualizado.getStock());
        producto.setStockMinimo(productoActualizado.getStockMinimo());
        producto.setCategoria(productoActualizado.getCategoria());
        producto.setImagen(productoActualizado.getImagen());
        
        return productoRepository.save(producto);
    }
    
    public void eliminarProducto(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        producto.setActivo(false);
        productoRepository.save(producto);
    }
    
    public List<Producto> obtenerProductosConStockBajo() {
        return productoRepository.findProductosConStockBajo();
    }
    
    public List<Producto> obtenerProductosConStockBajo(Integer stockMinimo) {
        return productoRepository.findProductosConStockBajo(stockMinimo);
    }
    
    public void actualizarStock(Long id, Integer nuevaCantidad) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        producto.setStock(nuevaCantidad);
        productoRepository.save(producto);
    }
    
    // Métodos para tallas
    public List<ProductoTalla> obtenerTallasProducto(Long productoId) {
        return productoTallaRepository.findByProductoId(productoId);
    }
    
    @Transactional
    public void actualizarTallasProducto(Long productoId, List<ProductoTalla> tallas) {
        productoTallaRepository.deleteByProductoId(productoId);
        for (ProductoTalla talla : tallas) {
            talla.setProducto(productoRepository.findById(productoId).orElse(null));
            productoTallaRepository.save(talla);
        }
    }
    
    // Métodos para colores
    public List<ProductoColor> obtenerColoresProducto(Long productoId) {
        return productoColorRepository.findByProductoId(productoId);
    }
    
    @Transactional
    public void actualizarColoresProducto(Long productoId, List<ProductoColor> colores) {
        productoColorRepository.deleteByProductoId(productoId);
        for (ProductoColor color : colores) {
            color.setProducto(productoRepository.findById(productoId).orElse(null));
            productoColorRepository.save(color);
        }
    }
    
    // Métodos para imágenes
    public List<ProductoImagen> obtenerImagenesProducto(Long productoId) {
        return productoImagenRepository.findByProductoId(productoId);
    }
    
    @Transactional
    public void actualizarImagenesProducto(Long productoId, List<ProductoImagen> imagenes) {
        productoImagenRepository.deleteByProductoId(productoId);
        for (ProductoImagen imagen : imagenes) {
            imagen.setProducto(productoRepository.findById(productoId).orElse(null));
            productoImagenRepository.save(imagen);
        }
    }
    
    private String generarSku(Producto producto) {
        String prefix = producto.getCategoria().getNombre().substring(0, 3).toUpperCase();
        long count = productoRepository.count() + 1;
        return prefix + String.format("%03d", count);
    }
}