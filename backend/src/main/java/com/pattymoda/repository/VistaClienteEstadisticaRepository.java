package com.pattymoda.repository;

import com.pattymoda.dto.VistaClienteEstadisticaDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface VistaClienteEstadisticaRepository extends Repository<VistaClienteEstadisticaDTO, Long> {
    @Query(value = "SELECT c.id as id, c.codigo_cliente as codigoCliente, c.nombre as nombre, c.apellido as apellido, c.numero_documento as numeroDocumento, c.tipo_documento as tipoDocumento, cc.tipo_cliente as tipoCliente, cc.total_compras as totalCompras, cc.cantidad_compras as cantidadCompras, cc.ultima_compra as ultimaCompra, cc.limite_credito as limiteCredito, cc.descuento_personalizado as descuentoPersonalizado, pl.puntos_disponibles as puntosDisponibles, pl.nivel_cliente as nivelCliente, c.activo as activo, CASE WHEN cc.total_compras >= 5000 THEN 'VIP' WHEN cc.total_compras >= 2000 THEN 'PREMIUM' WHEN cc.total_compras >= 500 THEN 'REGULAR' ELSE 'NUEVO' END as categoriaCliente, coalesce(cc.total_compras / nullif(cc.cantidad_compras,0),0) as ticketPromedio FROM clientes c left join clientes_comercial cc on c.id = cc.cliente_id left join programa_lealtad pl on c.id = pl.cliente_id WHERE (:nombre IS NULL OR c.nombre LIKE %:nombre%) AND (:documento IS NULL OR c.numero_documento LIKE %:documento%) AND (:tipoCliente IS NULL OR cc.tipo_cliente = :tipoCliente)",
           countQuery = "SELECT count(*) FROM clientes c left join clientes_comercial cc on c.id = cc.cliente_id left join programa_lealtad pl on c.id = pl.cliente_id WHERE (:nombre IS NULL OR c.nombre LIKE %:nombre%) AND (:documento IS NULL OR c.numero_documento LIKE %:documento%) AND (:tipoCliente IS NULL OR cc.tipo_cliente = :tipoCliente)",
           nativeQuery = true)
    Page<VistaClienteEstadisticaDTO> findAllByFilters(@Param("nombre") String nombre, @Param("documento") String documento, @Param("tipoCliente") String tipoCliente, Pageable pageable);
} 