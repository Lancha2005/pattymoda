package com.pattymoda.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pattymoda.dto.request.ProductoCreateDTO;
import com.pattymoda.dto.request.ProductoUpdateDTO;
import com.pattymoda.dto.response.ProductoResponseDTO;
import com.pattymoda.entity.Producto;
import com.pattymoda.mapper.ProductoMapper;
import com.pattymoda.service.ProductoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductoController.class)
class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductoService productoService;

    @MockBean
    private ProductoMapper productoMapper;

    @Autowired
    private ObjectMapper objectMapper;

    private ProductoResponseDTO productoResponseDTO;
    private ProductoCreateDTO productoCreateDTO;
    private ProductoUpdateDTO productoUpdateDTO;
    private Producto producto;

    @BeforeEach
    void setUp() {
        productoResponseDTO = new ProductoResponseDTO();
        productoResponseDTO.setId(1L);
        productoResponseDTO.setCodigoProducto("PROD001");
        productoResponseDTO.setNombre("Producto Test");
        productoResponseDTO.setSku("SKU001");

        productoCreateDTO = new ProductoCreateDTO();
        productoCreateDTO.setCodigoProducto("PROD001");
        productoCreateDTO.setNombre("Producto Test");
        productoCreateDTO.setSku("SKU001");
        productoCreateDTO.setCategoriaId(1L);

        productoUpdateDTO = new ProductoUpdateDTO();
        productoUpdateDTO.setNombre("Producto Actualizado");
        productoUpdateDTO.setCategoriaId(1L);

        producto = new Producto();
        producto.setId(1L);
        producto.setCodigoProducto("PROD001");
        producto.setNombre("Producto Test");
    }

    @Test
    @WithMockUser
    void getAllProductos_Success() throws Exception {
        // Given
        List<Producto> productos = Arrays.asList(producto);
        List<ProductoResponseDTO> productosDTO = Arrays.asList(productoResponseDTO);
        
        when(productoService.findAll()).thenReturn(productos);
        when(productoMapper.toResponseDTO(any(Producto.class))).thenReturn(productoResponseDTO);

        // When & Then
        mockMvc.perform(get("/productos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].nombre").value("Producto Test"));
    }

    @Test
    @WithMockUser
    void getProductosPaginados_Success() throws Exception {
        // Given
        Page<Producto> productos = new PageImpl<>(Arrays.asList(producto), PageRequest.of(0, 10), 1);
        Page<ProductoResponseDTO> productosDTO = new PageImpl<>(Arrays.asList(productoResponseDTO), PageRequest.of(0, 10), 1);
        
        when(productoService.findAll(any(PageRequest.class))).thenReturn(productos);

        // When & Then
        mockMvc.perform(get("/productos/page")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    @WithMockUser
    void getProductoById_Success() throws Exception {
        // Given
        when(productoService.findById(1L)).thenReturn(Optional.of(producto));
        when(productoMapper.toResponseDTO(producto)).thenReturn(productoResponseDTO);

        // When & Then
        mockMvc.perform(get("/productos/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpected(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nombre").value("Producto Test"));
    }

    @Test
    @WithMockUser
    void getProductoById_NotFound() throws Exception {
        // Given
        when(productoService.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/productos/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void createProducto_Success() throws Exception {
        // Given
        when(productoService.crearProducto(any(ProductoCreateDTO.class))).thenReturn(productoResponseDTO);

        // When & Then
        mockMvc.perform(post("/productos")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productoCreateDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nombre").value("Producto Test"));
    }

    @Test
    @WithMockUser
    void createProducto_ValidationError() throws Exception {
        // Given
        ProductoCreateDTO invalidDTO = new ProductoCreateDTO();
        // No se setean campos obligatorios

        // When & Then
        mockMvc.perform(post("/productos")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void updateProducto_Success() throws Exception {
        // Given
        when(productoService.actualizarProducto(eq(1L), any(ProductoUpdateDTO.class))).thenReturn(productoResponseDTO);

        // When & Then
        mockMvc.perform(put("/productos/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productoUpdateDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    @WithMockUser
    void getProductoByCodigo_Success() throws Exception {
        // Given
        when(productoService.findByCodigoProducto("PROD001")).thenReturn(Optional.of(producto));
        when(productoMapper.toResponseDTO(producto)).thenReturn(productoResponseDTO);

        // When & Then
        mockMvc.perform(get("/productos/codigo/PROD001"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.codigoProducto").value("PROD001"));
    }

    @Test
    @WithMockUser
    void cambiarEstado_Success() throws Exception {
        // When & Then
        mockMvc.perform(patch("/productos/1/estado")
                .with(csrf())
                .param("activo", "false"))
                .andExpect(status().isOk());
    }
}