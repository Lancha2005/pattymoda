package com.pattymoda.service;

import com.pattymoda.entity.Venta;
import com.pattymoda.repository.VentaRepository;
import com.pattymoda.repository.DetalleVentaRepository;
import com.pattymoda.repository.ClienteRepository;
import com.pattymoda.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReporteService {

    @Autowired
    private VentaRepository ventaRepository;
    
    @Autowired
    private DetalleVentaRepository detalleVentaRepository;
    
    @Autowired
    private ClienteRepository clienteRepository;
    
    @Autowired
    private ProductoRepository productoRepository;

    public Map<String, Object> getReporteDashboard() {
        Map<String, Object> reporte = new HashMap<>();
        
        LocalDateTime inicioMes = LocalDate.now().withDayOfMonth(1).atStartOfDay();
        LocalDateTime finMes = LocalDate.now().plusDays(1).atStartOfDay();
        
        // Ventas del mes
        BigDecimal ventasMes = ventaRepository.sumTotalVentasPagadas(inicioMes, finMes);
        long cantidadVentasMes = ventaRepository.countVentasPagadas(inicioMes, finMes);
        
        // Estadísticas generales
        long totalClientes = clienteRepository.countClientesActivos();
        long totalProductos = productoRepository.countProductosActivos();
        
        reporte.put("ventasMes", ventasMes != null ? ventasMes : BigDecimal.ZERO);
        reporte.put("cantidadVentasMes", cantidadVentasMes);
        reporte.put("totalClientes", totalClientes);
        reporte.put("totalProductos", totalProductos);
        reporte.put("ticketPromedio", cantidadVentasMes > 0 ? 
            (ventasMes != null ? ventasMes.divide(BigDecimal.valueOf(cantidadVentasMes)) : BigDecimal.ZERO) : 
            BigDecimal.ZERO);
        
        return reporte;
    }

    public Map<String, Object> getReporteVentasPorPeriodo(LocalDateTime inicio, LocalDateTime fin) {
        Map<String, Object> reporte = new HashMap<>();
        
        BigDecimal totalVentas = ventaRepository.sumTotalVentasPagadas(inicio, fin);
        long cantidadVentas = ventaRepository.countVentasPagadas(inicio, fin);
        
        reporte.put("totalVentas", totalVentas != null ? totalVentas : BigDecimal.ZERO);
        reporte.put("cantidadVentas", cantidadVentas);
        reporte.put("periodo", Map.of("inicio", inicio, "fin", fin));
        
        return reporte;
    }

    public List<Map<String, Object>> getProductosMasVendidos(int limite) {
        // Implementar query para productos más vendidos
        // Esta funcionalidad requeriría una query específica en el repository
        return List.of();
    }

    public List<Map<String, Object>> getClientesTopCompradores(int limite) {
        // Implementar query para mejores clientes
        return List.of();
    }

    public Map<String, Object> getReporteInventario() {
        Map<String, Object> reporte = new HashMap<>();
        
        List<com.pattymoda.entity.Producto> stockBajo = productoRepository.findProductosStockBajo();
        long productosActivos = productoRepository.countProductosActivos();
        
        reporte.put("productosStockBajo", stockBajo.size());
        reporte.put("totalProductosActivos", productosActivos);
        reporte.put("productosStockBajoDetalle", stockBajo);
        
        return reporte;
    }
}