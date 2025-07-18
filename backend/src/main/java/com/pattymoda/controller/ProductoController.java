package com.pattymoda.controller;

import com.pattymoda.dto.request.ProductoCreateDTO;
import com.pattymoda.dto.request.ProductoUpdateDTO;
import com.pattymoda.dto.response.ProductoResponseDTO;
import com.pattymoda.entity.Producto;
import com.pattymoda.mapper.ProductoMapper;
import com.pattymoda.service.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/productos")
@Tag(name = "Productos", description = "API para gestión de productos")
@CrossOrigin(origins = "*")
@Validated
public class ProductoController extends BaseController<Producto, Long> {

    private static final Logger logger = LoggerFactory.getLogger(ProductoController.class);

    private final ProductoService productoService;
    private final ProductoMapper productoMapper;

    public ProductoController(ProductoService productoService, ProductoMapper productoMapper) {
        super(productoService);
        this.productoService = productoService;
        this.productoMapper = productoMapper;
    }

    @Operation(summary = "Obtener todos los productos", description = "Retorna una lista de todos los productos del sistema")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de productos obtenida exitosamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<List<ProductoResponseDTO>> getAllProductos() {
        logger.info("Obteniendo todos los productos");
        List<Producto> productos = productoService.findAll();
        List<ProductoResponseDTO> productosDTO = productos.stream()
                .map(productoMapper::toResponseDTO)
                .toList();
        return ResponseEntity.ok(productosDTO);
    }

    @Operation(summary = "Obtener productos paginados", description = "Retorna una página de productos con paginación")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Página de productos obtenida exitosamente"),
        @ApiResponse(responseCode = "400", description = "Parámetros de paginación inválidos")
    })
    @GetMapping("/page")
    public ResponseEntity<Page<ProductoResponseDTO>> getProductosPaginados(Pageable pageable) {
        logger.info("Obteniendo productos paginados - Página: {}, Tamaño: {}", pageable.getPageNumber(), pageable.getPageSize());
        Page<Producto> productos = productoService.findAll(pageable);
        Page<ProductoResponseDTO> productosDTO = productos.map(productoMapper::toResponseDTO);
        return ResponseEntity.ok(productosDTO);
    }

    @Operation(summary = "Obtener producto por ID", description = "Retorna un producto específico por su ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Producto encontrado"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponseDTO> getProductoById(@PathVariable Long id) {
        logger.info("Obteniendo producto por ID: {}", id);
        return productoService.findById(id)
                .map(productoMapper::toResponseDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Buscar producto por código", description = "Busca un producto por su código único")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Producto encontrado"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @GetMapping("/codigo/{codigoProducto}")
    public ResponseEntity<ProductoResponseDTO> getProductoByCodigo(@PathVariable String codigoProducto) {
        logger.info("Buscando producto por código: {}", codigoProducto);
        return productoService.findByCodigoProducto(codigoProducto)
                .map(productoMapper::toResponseDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Buscar producto por SKU")
    @GetMapping("/sku/{sku}")
    public ResponseEntity<Producto> getProductoBySku(@PathVariable String sku) {
        return productoService.findBySku(sku)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Buscar producto por código de barras")
    @GetMapping("/barras/{codigoBarras}")
    public ResponseEntity<Producto> getProductoByCodigoBarras(@PathVariable String codigoBarras) {
        return productoService.findByCodigoBarras(codigoBarras)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Buscar productos activos")
    @GetMapping("/activos")
    public ResponseEntity<Page<Producto>> getProductosActivos(
            @RequestParam(defaultValue = "true") Boolean activo,
            Pageable pageable) {
        Page<Producto> productos = productoService.findByActivo(activo, pageable);
        return ResponseEntity.ok(productos);
    }

    @Operation(summary = "Buscar productos por categoría")
    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity<Page<Producto>> getProductosByCategoria(
            @PathVariable Long categoriaId,
            Pageable pageable) {
        Page<Producto> productos = productoService.findByCategoriaId(categoriaId, pageable);
        return ResponseEntity.ok(productos);
    }

    @Operation(summary = "Buscar productos por marca")
    @GetMapping("/marca/{marcaId}")
    public ResponseEntity<Page<Producto>> getProductosByMarca(
            @PathVariable Long marcaId,
            Pageable pageable) {
        Page<Producto> productos = productoService.findByMarcaId(marcaId, pageable);
        return ResponseEntity.ok(productos);
    }

    @Operation(summary = "Buscar productos destacados")
    @GetMapping("/destacados")
    public ResponseEntity<Page<Producto>> getProductosDestacados(
            @RequestParam(defaultValue = "true") Boolean destacado,
            Pageable pageable) {
        Page<Producto> productos = productoService.findByDestacado(destacado, pageable);
        return ResponseEntity.ok(productos);
    }

    @Operation(summary = "Buscar productos nuevos")
    @GetMapping("/nuevos")
    public ResponseEntity<Page<Producto>> getProductosNuevos(
            @RequestParam(defaultValue = "true") Boolean nuevo,
            Pageable pageable) {
        Page<Producto> productos = productoService.findByNuevo(nuevo, pageable);
        return ResponseEntity.ok(productos);
    }

    @Operation(summary = "Buscar productos")
    @GetMapping("/buscar")
    public ResponseEntity<Page<Producto>> buscarProductos(
            @RequestParam String busqueda,
            Pageable pageable) {
        Page<Producto> productos = productoService.buscarProductos(busqueda, pageable);
        return ResponseEntity.ok(productos);
    }

    @Operation(summary = "Obtener productos destacados por categoría")
    @GetMapping("/destacados/categoria/{categoriaId}")
    public ResponseEntity<List<Producto>> getDestacadosByCategoria(@PathVariable Long categoriaId) {
        List<Producto> productos = productoService.findDestacadosByCategoria(categoriaId);
        return ResponseEntity.ok(productos);
    }

    @Operation(summary = "Obtener productos con stock bajo")
    @GetMapping("/stock-bajo")
    public ResponseEntity<List<Producto>> getProductosStockBajo() {
        List<Producto> productos = productoService.findProductosStockBajo();
        return ResponseEntity.ok(productos);
    }

    @Operation(summary = "Crear nuevo producto", description = "Crea un nuevo producto en el sistema")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Producto creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "409", description = "Conflicto - Código o SKU ya existe")
    })
    @PostMapping
    public ResponseEntity<ProductoResponseDTO> createProducto(@Valid @RequestBody ProductoCreateDTO productoDTO) {
        logger.info("Creando nuevo producto: {}", productoDTO.getNombre());
        ProductoResponseDTO nuevoProducto = productoService.crearProducto(productoDTO);
        return ResponseEntity.status(201).body(nuevoProducto);
    }

    @Operation(summary = "Actualizar producto", description = "Actualiza un producto existente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Producto actualizado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProductoResponseDTO> updateProducto(@PathVariable Long id, @Valid @RequestBody ProductoUpdateDTO productoDTO) {
        logger.info("Actualizando producto ID: {}", id);
        ProductoResponseDTO productoActualizado = productoService.actualizarProducto(id, productoDTO);
        return ResponseEntity.ok(productoActualizado);
    }

    @Operation(summary = "Cambiar estado de producto")
    @PatchMapping("/{id}/estado")
    public ResponseEntity<Void> cambiarEstado(@PathVariable Long id, @RequestParam Boolean activo) {
        try {
            productoService.cambiarEstado(id, activo);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Cambiar destacado de producto")
    @PatchMapping("/{id}/destacado")
    public ResponseEntity<Void> cambiarDestacado(@PathVariable Long id, @RequestParam Boolean destacado) {
        try {
            productoService.cambiarDestacado(id, destacado);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Cambiar nuevo de producto")
    @PatchMapping("/{id}/nuevo")
    public ResponseEntity<Void> cambiarNuevo(@PathVariable Long id, @RequestParam Boolean nuevo) {
        try {
            productoService.cambiarNuevo(id, nuevo);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Eliminar producto")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProducto(@PathVariable Long id) {
        return super.delete(id);
    }

    @Operation(summary = "Contar productos activos")
    @GetMapping("/count/activos")
    public ResponseEntity<Long> countProductosActivos() {
        long count = productoService.countProductosActivos();
        return ResponseEntity.ok(count);
    }

    @Operation(summary = "Contar productos destacados")
    @GetMapping("/count/destacados")
    public ResponseEntity<Long> countProductosDestacados() {
        long count = productoService.countProductosDestacados();
        return ResponseEntity.ok(count);
    }

    @Operation(summary = "Verificar si existe código de producto")
    @GetMapping("/exists/codigo/{codigoProducto}")
    public ResponseEntity<Boolean> existsByCodigoProducto(@PathVariable String codigoProducto) {
        boolean exists = productoService.existsByCodigoProducto(codigoProducto);
        return ResponseEntity.ok(exists);
    }

    @Operation(summary = "Verificar si existe SKU")
    @GetMapping("/exists/sku/{sku}")
    public ResponseEntity<Boolean> existsBySku(@PathVariable String sku) {
        boolean exists = productoService.existsBySku(sku);
        return ResponseEntity.ok(exists);
    }

    @Operation(summary = "Verificar si existe código de barras")
    @GetMapping("/exists/barras/{codigoBarras}")
    public ResponseEntity<Boolean> existsByCodigoBarras(@PathVariable String codigoBarras) {
        boolean exists = productoService.existsByCodigoBarras(codigoBarras);
        return ResponseEntity.ok(exists);
    }
} 