package com.pattymoda.service;

import com.pattymoda.dto.request.ProductoCreateDTO;
import com.pattymoda.dto.request.ProductoUpdateDTO;
import com.pattymoda.dto.response.ProductoResponseDTO;
import com.pattymoda.entity.Categoria;
import com.pattymoda.entity.Marca;
import com.pattymoda.entity.Producto;
import com.pattymoda.exception.BusinessException;
import com.pattymoda.mapper.ProductoMapper;
import com.pattymoda.repository.CategoriaRepository;
import com.pattymoda.repository.MarcaRepository;
import com.pattymoda.repository.ProductoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductoService extends BaseService<Producto, Long> {

    private static final Logger logger = LoggerFactory.getLogger(ProductoService.class);

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;
    private final MarcaRepository marcaRepository;
    private final ProductoMapper productoMapper;

    public ProductoService(ProductoRepository productoRepository, 
                          CategoriaRepository categoriaRepository,
                          MarcaRepository marcaRepository,
                          ProductoMapper productoMapper) {
        super(productoRepository);
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
        this.marcaRepository = marcaRepository;
        this.productoMapper = productoMapper;
    }

    public Optional<Producto> findByCodigoProducto(String codigoProducto) {
        return productoRepository.findByCodigoProducto(codigoProducto);
    }

    public Optional<Producto> findBySku(String sku) {
        return productoRepository.findBySku(sku);
    }

    public Optional<Producto> findByCodigoBarras(String codigoBarras) {
        return productoRepository.findByCodigoBarras(codigoBarras);
    }

    public boolean existsByCodigoProducto(String codigoProducto) {
        return productoRepository.existsByCodigoProducto(codigoProducto);
    }

    public boolean existsBySku(String sku) {
        return productoRepository.existsBySku(sku);
    }

    public boolean existsByCodigoBarras(String codigoBarras) {
        return productoRepository.existsByCodigoBarras(codigoBarras);
    }

    public Page<Producto> findByActivo(Boolean activo, Pageable pageable) {
        return productoRepository.findByActivo(activo, pageable);
    }

    public Page<Producto> findByCategoriaId(Long categoriaId, Pageable pageable) {
        return productoRepository.findByCategoriaId(categoriaId, pageable);
    }

    public Page<Producto> findByMarcaId(Long marcaId, Pageable pageable) {
        return productoRepository.findByMarcaId(marcaId, pageable);
    }

    public Page<Producto> findByDestacado(Boolean destacado, Pageable pageable) {
        return productoRepository.findByDestacado(destacado, pageable);
    }

    public Page<Producto> findByNuevo(Boolean nuevo, Pageable pageable) {
        return productoRepository.findByNuevo(nuevo, pageable);
    }

    public Page<Producto> buscarProductos(String busqueda, Pageable pageable) {
        return productoRepository.buscarProductos(busqueda, pageable);
    }

    public List<Producto> findDestacadosByCategoria(Long categoriaId) {
        return productoRepository.findDestacadosByCategoria(categoriaId);
    }

    public List<Producto> findProductosStockBajo() {
        return productoRepository.findProductosStockBajo();
    }

    public long countProductosActivos() {
        return productoRepository.countProductosActivos();
    }

    public long countProductosDestacados() {
        return productoRepository.countProductosDestacados();
    }

    public ProductoResponseDTO crearProducto(ProductoCreateDTO productoDTO) {
        logger.info("Creando producto: {}", productoDTO.getNombre());
        
        // Validar que el código de producto no exista
        if (existsByCodigoProducto(productoDTO.getCodigoProducto())) {
            throw new BusinessException("El código de producto ya está registrado: " + productoDTO.getCodigoProducto());
        }

        // Validar que el SKU no exista
        if (existsBySku(productoDTO.getSku())) {
            throw new BusinessException("El SKU ya está registrado: " + productoDTO.getSku());
        }

        // Validar que el código de barras no exista
        if (productoDTO.getCodigoBarras() != null && existsByCodigoBarras(productoDTO.getCodigoBarras())) {
            throw new BusinessException("El código de barras ya está registrado: " + productoDTO.getCodigoBarras());
        }

        // Convertir DTO a entidad
        Producto producto = productoMapper.toEntity(productoDTO);
        
        // Establecer relaciones
        Categoria categoria = categoriaRepository.findById(productoDTO.getCategoriaId())
                .orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada con ID: " + productoDTO.getCategoriaId()));
        producto.setCategoria(categoria);
        
        if (productoDTO.getMarcaId() != null) {
            Marca marca = marcaRepository.findById(productoDTO.getMarcaId())
                    .orElseThrow(() -> new EntityNotFoundException("Marca no encontrada con ID: " + productoDTO.getMarcaId()));
            producto.setMarca(marca);
        }
        
        Producto productoGuardado = save(producto);
        logger.info("Producto creado exitosamente con ID: {}", productoGuardado.getId());
        
        return productoMapper.toResponseDTO(productoGuardado);
    }

    public ProductoResponseDTO actualizarProducto(Long id, ProductoUpdateDTO productoDTO) {
        logger.info("Actualizando producto ID: {}", id);
        
        Producto productoExistente = findById(id)

                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con ID: " + id));

        // Actualizar campos usando el mapper
        productoMapper.updateEntity(productoExistente, productoDTO);
        
        // Establecer relaciones
        Categoria categoria = categoriaRepository.findById(productoDTO.getCategoriaId())
                .orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada con ID: " + productoDTO.getCategoriaId()));
        productoExistente.setCategoria(categoria);
        
        if (productoDTO.getMarcaId() != null) {
            Marca marca = marcaRepository.findById(productoDTO.getMarcaId())
                    .orElseThrow(() -> new EntityNotFoundException("Marca no encontrada con ID: " + productoDTO.getMarcaId()));
            productoExistente.setMarca(marca);
        } else {
            productoExistente.setMarca(null);
        }
        
        Producto productoActualizado = save(productoExistente);
        logger.info("Producto actualizado exitosamente ID: {}", id);
        
        return productoMapper.toResponseDTO(productoActualizado);
    }

    public void cambiarEstado(Long id, Boolean activo) {
        logger.info("Cambiando estado del producto ID: {} a {}", id, activo);
        Producto producto = findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con ID: " + id));
        producto.setActivo(activo);
        save(producto);
    }

    public void cambiarDestacado(Long id, Boolean destacado) {
        logger.info("Cambiando destacado del producto ID: {} a {}", id, destacado);
        Producto producto = findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con ID: " + id));
        producto.setDestacado(destacado);
        save(producto);
    }

    public void cambiarNuevo(Long id, Boolean nuevo) {
        logger.info("Cambiando nuevo del producto ID: {} a {}", id, nuevo);
        Producto producto = findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con ID: " + id));
        producto.setNuevo(nuevo);
        save(producto);
    }
} 