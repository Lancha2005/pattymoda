package com.pattymoda.repository;

import com.pattymoda.dto.VistaVentaCompletaDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface VistaVentaCompletaRepository extends Repository<VistaVentaCompletaDTO, Long> {
    @Query(value = "SELECT v.id as id, v.numero_venta as numeroVenta, v.fecha as fecha, v.subtotal as subtotal, v.total as total, v.estado as estado, vf.tipo_comprobante as tipoComprobante, vf.serie_comprobante as serieComprobante, vf.numero_comprobante as numeroComprobante, cv.nombre as canalVenta, concat(c.nombre,' ',coalesce(c.apellido,'')) as clienteNombre, c.numero_documento as clienteDocumento, concat(u.nombre,' ',coalesce(u.apellido,'')) as vendedorNombre, v.cantidad_items as cantidadItems, v.comision_vendedor as comisionVendedor, ve.estado_entrega as estadoEntrega, ve.fecha_entrega_real as fechaEntregaReal FROM ventas v left join clientes c on v.cliente_id = c.id left join usuarios u on v.vendedor_id = u.id left join canales_venta cv on v.canal_venta_id = cv.id left join ventas_facturacion vf on v.id = vf.venta_id left join ventas_entrega ve on v.id = ve.venta_id WHERE (:numeroVenta IS NULL OR v.numero_venta LIKE %:numeroVenta%) AND (:clienteNombre IS NULL OR c.nombre LIKE %:clienteNombre%) AND (:estado IS NULL OR v.estado = :estado)",
           countQuery = "SELECT count(*) FROM ventas v left join clientes c on v.cliente_id = c.id left join usuarios u on v.vendedor_id = u.id left join canales_venta cv on v.canal_venta_id = cv.id left join ventas_facturacion vf on v.id = vf.venta_id left join ventas_entrega ve on v.id = ve.venta_id WHERE (:numeroVenta IS NULL OR v.numero_venta LIKE %:numeroVenta%) AND (:clienteNombre IS NULL OR c.nombre LIKE %:clienteNombre%) AND (:estado IS NULL OR v.estado = :estado)",
           nativeQuery = true)
    Page<VistaVentaCompletaDTO> findAllByFilters(@Param("numeroVenta") String numeroVenta, @Param("clienteNombre") String clienteNombre, @Param("estado") String estado, Pageable pageable);
} 