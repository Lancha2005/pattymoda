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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private CategoriaRepository categoriaRepository;

    @Mock
    private MarcaRepository marcaRepository;

    @Mock
    private ProductoMapper productoMapper;

    @InjectMocks
    private ProductoService productoService;

    private ProductoCreateDTO productoCreateDTO;
    private ProductoUpdateDTO productoUpdateDTO;
    private Producto producto;
    private Categoria categoria;
    private Marca marca;
    private ProductoResponseDTO productoResponseDTO;

    @BeforeEach
    void setUp() {
        // Setup test data
        productoCreateDTO = new ProductoCreateDTO();
        productoCreateDTO.setCodigoProducto("PROD001");
        productoCreateDTO.setNombre("Producto Test");
        productoCreateDTO.setSku("SKU001");
        productoCreateDTO.setCategoriaId(1L);
        productoCreateDTO.setMarcaId(1L);

        productoUpdateDTO = new ProductoUpdateDTO();
        productoUpdateDTO.setNombre("Producto Actualizado");
        productoUpdateDTO.setCategoriaId(1L);

        categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNombre("Categoria Test");

        marca = new Marca();
        marca.setId(1L);
        marca.setNombre("Marca Test");

        producto = new Producto();
        producto.setId(1L);
        producto.setCodigoProducto("PROD001");
        producto.setNombre("Producto Test");
        producto.setSku("SKU001");
        producto.setCategoria(categoria);
        producto.setMarca(marca);

        productoResponseDTO = new ProductoResponseDTO();
        productoResponseDTO.setId(1L);
        productoResponseDTO.setNombre("Producto Test");
    }

    @Test
    void crearProducto_Success() {
        // Given
        when(productoRepository.existsByCodigoProducto(anyString())).thenReturn(false);
        when(productoRepository.existsBySku(anyString())).thenReturn(false);
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));
        when(marcaRepository.findById(1L)).thenReturn(Optional.of(marca));
        when(productoMapper.toEntity(productoCreateDTO)).thenReturn(producto);
        when(productoRepository.save(any(Producto.class))).thenReturn(producto);
        when(productoMapper.toResponseDTO(producto)).thenReturn(productoResponseDTO);

        // When
        ProductoResponseDTO result = productoService.crearProducto(productoCreateDTO);

        // Then
        assertNotNull(result);
        assertEquals("Producto Test", result.getNombre());
        verify(productoRepository).save(any(Producto.class));
    }

    @Test
    void crearProducto_CodigoProductoExiste_ThrowsBusinessException() {
        // Given
        when(productoRepository.existsByCodigoProducto("PROD001")).thenReturn(true);

        // When & Then
        BusinessException exception = assertThrows(BusinessException.class, 
            () -> productoService.crearProducto(productoCreateDTO));
        
        assertEquals("El código de producto ya está registrado: PROD001", exception.getMessage());
        verify(productoRepository, never()).save(any());
    }

    @Test
    void crearProducto_SkuExiste_ThrowsBusinessException() {
        // Given
        when(productoRepository.existsByCodigoProducto(anyString())).thenReturn(false);
        when(productoRepository.existsBySku("SKU001")).thenReturn(true);

        // When & Then
        BusinessException exception = assertThrows(BusinessException.class, 
            () -> productoService.crearProducto(productoCreateDTO));
        
        assertEquals("El SKU ya está registrado: SKU001", exception.getMessage());
        verify(productoRepository, never()).save(any());
    }

    @Test
    void crearProducto_CategoriaNoExiste_ThrowsEntityNotFoundException() {
        // Given
        when(productoRepository.existsByCodigoProducto(anyString())).thenReturn(false);
        when(productoRepository.existsBySku(anyString())).thenReturn(false);
        when(categoriaRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, 
            () -> productoService.crearProducto(productoCreateDTO));
        
        assertEquals("Categoría no encontrada con ID: 1", exception.getMessage());
        verify(productoRepository, never()).save(any());
    }

    @Test
    void actualizarProducto_Success() {
        // Given
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));
        when(productoRepository.save(any(Producto.class))).thenReturn(producto);
        when(productoMapper.toResponseDTO(producto)).thenReturn(productoResponseDTO);

        // When
        ProductoResponseDTO result = productoService.actualizarProducto(1L, productoUpdateDTO);

        // Then
        assertNotNull(result);
        verify(productoMapper).updateEntity(producto, productoUpdateDTO);
        verify(productoRepository).save(producto);
    }

    @Test
    void actualizarProducto_ProductoNoExiste_ThrowsEntityNotFoundException() {
        // Given
        when(productoRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, 
            () -> productoService.actualizarProducto(1L, productoUpdateDTO));
        
        assertEquals("Producto no encontrado con ID: 1", exception.getMessage());
        verify(productoRepository, never()).save(any());
    }

    @Test
    void cambiarEstado_Success() {
        // Given
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        when(productoRepository.save(any(Producto.class))).thenReturn(producto);

        // When
        productoService.cambiarEstado(1L, false);

        // Then
        assertFalse(producto.getActivo());
        verify(productoRepository).save(producto);
    }

    @Test
    void cambiarDestacado_Success() {
        // Given
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        when(productoRepository.save(any(Producto.class))).thenReturn(producto);

        // When
        productoService.cambiarDestacado(1L, true);

        // Then
        assertTrue(producto.getDestacado());
        verify(productoRepository).save(producto);
    }

    @Test
    void findByCodigoProducto_Success() {
        // Given
        when(productoRepository.findByCodigoProducto("PROD001")).thenReturn(Optional.of(producto));

        // When
        Optional<Producto> result = productoService.findByCodigoProducto("PROD001");

        // Then
        assertTrue(result.isPresent());
        assertEquals("PROD001", result.get().getCodigoProducto());
    }

    @Test
    void existsByCodigoProducto_ReturnsTrue() {
        // Given
        when(productoRepository.existsByCodigoProducto("PROD001")).thenReturn(true);

        // When
        boolean result = productoService.existsByCodigoProducto("PROD001");

        // Then
        assertTrue(result);
    }

    @Test
    void existsBySku_ReturnsFalse() {
        // Given
        when(productoRepository.existsBySku("SKU001")).thenReturn(false);

        // When
        boolean result = productoService.existsBySku("SKU001");

        // Then
        assertFalse(result);
    }
}