package com.pattymoda.service;

import com.pattymoda.dto.VistaClienteEstadisticaDTO;
import com.pattymoda.repository.VistaClienteEstadisticaRepository;
import com.pattymoda.repository.VistaClienteEstadisticaProjection;
import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class VistaClienteEstadisticaService {
    @Autowired
    private VistaClienteEstadisticaRepository repository;

    public Page<VistaClienteEstadisticaDTO> findAllByFilters(String nombre, String documento, String tipoCliente,
            Pageable pageable) {
        Page<VistaClienteEstadisticaProjection> page = repository.findAllByFilters(nombre, documento, tipoCliente,
                pageable);
        return page.map(p -> {
            VistaClienteEstadisticaDTO dto = new VistaClienteEstadisticaDTO();
            dto.setId(p.getId());
            dto.setCodigoCliente(p.getCodigoCliente());
            dto.setNombre(p.getNombre());
            dto.setApellido(p.getApellido());
            dto.setNumeroDocumento(p.getNumeroDocumento());
            dto.setTipoDocumento(p.getTipoDocumento());
            dto.setTipoCliente(p.getTipoCliente());
            dto.setTotalCompras(p.getTotalCompras() != null ? BigDecimal.valueOf(p.getTotalCompras()) : null);
            dto.setCantidadCompras(p.getCantidadCompras());
            dto.setUltimaCompra(p.getUltimaCompra());
            dto.setLimiteCredito(p.getLimiteCredito() != null ? BigDecimal.valueOf(p.getLimiteCredito()) : null);
            dto.setDescuentoPersonalizado(
                    p.getDescuentoPersonalizado() != null ? BigDecimal.valueOf(p.getDescuentoPersonalizado()) : null);
            dto.setPuntosDisponibles(p.getPuntosDisponibles());
            dto.setNivelCliente(p.getNivelCliente());
            dto.setActivo(p.getActivo());
            dto.setCategoriaCliente(p.getCategoriaCliente());
            dto.setTicketPromedio(p.getTicketPromedio() != null ? BigDecimal.valueOf(p.getTicketPromedio()) : null);
            return dto;
        });
    }
}