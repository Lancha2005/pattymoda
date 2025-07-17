package com.tienda.service;

import com.tienda.dto.ReporteVentas;
import com.tienda.repository.DetalleVentaRepository;
import com.tienda.repository.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Service
public class ReporteService {
    
    @Autowired
    private VentaRepository ventaRepository;
    
    @Autowired
    private DetalleVentaRepository detalleVentaRepository;
    
    public ReporteVentas obtenerReporteVentasPorFecha(LocalDate fechaInicio, LocalDate fechaFin) {
        LocalDateTime inicio = fechaInicio.atStartOfDay();
        LocalDateTime fin = fechaFin.atTime(23, 59, 59);
        
        BigDecimal totalVentas = ventaRepository.sumVentasByFecha(inicio, fin);
        Long cantidadVentas = ventaRepository.countVentasByFecha(inicio, fin);
        
        return new ReporteVentas(fechaInicio, totalVentas != null ? totalVentas : BigDecimal.ZERO, cantidadVentas);
    }
    
    public Map<String, Object> obtenerProductosMasVendidos() {
        List<Object[]> productos = detalleVentaRepository.findProductosMasVendidos();
        Map<String, Object> resultado = new HashMap<>();
        resultado.put("productos", productos);
        return resultado;
    }
    
    public Map<String, Object> obtenerEstadisticasGenerales() {
        LocalDateTime inicioMes = LocalDate.now().withDayOfMonth(1).atStartOfDay();
        LocalDateTime finMes = LocalDate.now().atTime(23, 59, 59);
        
        BigDecimal ventasDelMes = ventaRepository.sumVentasByFecha(inicioMes, finMes);
        Long cantidadVentasDelMes = ventaRepository.countVentasByFecha(inicioMes, finMes);
        
        Map<String, Object> estadisticas = new HashMap<>();
        estadisticas.put("ventasDelMes", ventasDelMes != null ? ventasDelMes : BigDecimal.ZERO);
        estadisticas.put("cantidadVentasDelMes", cantidadVentasDelMes);
        
        return estadisticas;
    }
}