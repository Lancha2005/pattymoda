package com.pattymoda.repository;

import com.pattymoda.dto.VistaProductoCompletoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface VistaProductoCompletoRepository extends Repository<VistaProductoCompletoDTO, Long> {
    @Query(value = "SELECT p.id as id, p.codigo_producto as codigoProducto, p.nombre as nombre, p.sku as sku, p.descripcion as descripcion, p.descripcion_corta as descripcionCorta, c.nombre as categoriaNombre, c.id as categoriaId, m.nombre as marcaNombre, m.id as marcaId, pp.precio_venta as precio, pp.precio_oferta as precioOferta, pp.costo as costo, pp.margen_porcentaje as margenPorcentaje, pi.stock_actual as stock, pi.stock_minimo as stockMinimo, pi.stock_maximo as stockMaximo, pi.stock_disponible as stockDisponible, p.imagen_principal as imagenPrincipal, p.requiere_talla as requiereTalla, p.requiere_color as requiereColor, p.destacado as destacado, p.activo as activo, p.fecha_creacion as fechaCreacion, CASE WHEN pi.stock_actual <= 0 THEN 'SIN_STOCK' WHEN pi.stock_actual <= pi.stock_minimo THEN 'STOCK_BAJO' ELSE 'STOCK_OK' END as estadoStock FROM productos p left join categorias c on p.categoria_id = c.id left join marcas m on p.marca_id = m.id left join productos_precios pp on p.id = pp.producto_id and pp.activo = 1 and (pp.fecha_fin is null or pp.fecha_fin >= curdate()) left join productos_inventario pi on p.id = pi.producto_id WHERE (:nombre IS NULL OR p.nombre LIKE %:nombre%) AND (:categoriaId IS NULL OR c.id = :categoriaId) AND (:marcaId IS NULL OR m.id = :marcaId)",
           countQuery = "SELECT count(*) FROM productos p left join categorias c on p.categoria_id = c.id left join marcas m on p.marca_id = m.id left join productos_precios pp on p.id = pp.producto_id and pp.activo = 1 and (pp.fecha_fin is null or pp.fecha_fin >= curdate()) left join productos_inventario pi on p.id = pi.producto_id WHERE (:nombre IS NULL OR p.nombre LIKE %:nombre%) AND (:categoriaId IS NULL OR c.id = :categoriaId) AND (:marcaId IS NULL OR m.id = :marcaId)",
           nativeQuery = true)
    Page<VistaProductoCompletoDTO> findAllByFilters(@Param("nombre") String nombre, @Param("categoriaId") Long categoriaId, @Param("marcaId") Long marcaId, Pageable pageable);
} 