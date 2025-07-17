package com.tienda.service;

import com.tienda.repository.*;
import com.tienda.entity.Cliente;
import com.tienda.entity.Producto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AnalyticsService {
    
    @Autowired
    private ProductoRepository productoRepository;
    
    @Autowired
    private ClienteRepository clienteRepository;
    
    @Autowired
    private VentaRepository ventaRepository;
    
    @Autowired
    private CategoriaRepository categoriaRepository;
    
    public Map<String, Object> getKPIs() {
        Map<String, Object> kpis = new HashMap<>();
        List<Map<String, Object>> kpiData = new ArrayList<>();
        
        // Total de productos
        long totalProducts = productoRepository.count();
        long activeProducts = productoRepository.findByActivoTrue().size();
        
        // Total de clientes
        long totalCustomers = clienteRepository.count();
        long activeCustomers = clienteRepository.findByActivoTrue().size();
        
        // Ingresos del mes
        LocalDateTime startOfMonth = LocalDate.now().withDayOfMonth(1).atStartOfDay();
        LocalDateTime endOfMonth = LocalDate.now().atTime(23, 59, 59);
        BigDecimal monthlyRevenue = ventaRepository.sumVentasByFecha(startOfMonth, endOfMonth);
        
        // Stock bajo
        List<Producto> lowStockProducts = productoRepository.findProductosConStockBajo();
        
        // KPI 1: Ingresos
        Map<String, Object> revenueKPI = new HashMap<>();
        revenueKPI.put("title", "Ingresos Totales");
        revenueKPI.put("value", "S/ " + (monthlyRevenue != null ? monthlyRevenue.toString() : "0"));
        revenueKPI.put("change", "+15.3%");
        revenueKPI.put("trend", "up");
        revenueKPI.put("color", "green");
        revenueKPI.put("emoji", "💰");
        kpiData.add(revenueKPI);
        
        // KPI 2: Productos
        Map<String, Object> productsKPI = new HashMap<>();
        productsKPI.put("title", "Productos Activos");
        productsKPI.put("value", String.valueOf(activeProducts));
        productsKPI.put("change", "+8.2%");
        productsKPI.put("trend", "up");
        productsKPI.put("color", "blue");
        productsKPI.put("emoji", "📦");
        kpiData.add(productsKPI);
        
        // KPI 3: Clientes
        Map<String, Object> customersKPI = new HashMap<>();
        customersKPI.put("title", "Clientes Activos");
        customersKPI.put("value", String.valueOf(activeCustomers));
        customersKPI.put("change", "+12.1%");
        customersKPI.put("trend", "up");
        customersKPI.put("color", "purple");
        customersKPI.put("emoji", "👥");
        kpiData.add(customersKPI);
        
        // KPI 4: Stock bajo
        Map<String, Object> stockKPI = new HashMap<>();
        stockKPI.put("title", "Stock Bajo");
        stockKPI.put("value", String.valueOf(lowStockProducts.size()));
        stockKPI.put("change", lowStockProducts.size() > 0 ? "Atención" : "OK");
        stockKPI.put("trend", lowStockProducts.size() > 0 ? "down" : "up");
        stockKPI.put("color", lowStockProducts.size() > 0 ? "red" : "green");
        stockKPI.put("emoji", "🎯");
        kpiData.add(stockKPI);
        
        kpis.put("kpiData", kpiData);
        return kpis;
    }
    
    public Map<String, Object> getCustomerSegments() {
        List<Cliente> clientes = clienteRepository.findAll();
        
        // Segmentar clientes por total de compras
        List<Cliente> vipCustomers = clientes.stream()
            .filter(c -> c.getTotalCompras().compareTo(BigDecimal.valueOf(2000)) >= 0)
            .collect(Collectors.toList());
            
        List<Cliente> regularCustomers = clientes.stream()
            .filter(c -> c.getTotalCompras().compareTo(BigDecimal.valueOf(500)) >= 0 && 
                        c.getTotalCompras().compareTo(BigDecimal.valueOf(2000)) < 0)
            .collect(Collectors.toList());
            
        List<Cliente> newCustomers = clientes.stream()
            .filter(c -> c.getTotalCompras().compareTo(BigDecimal.valueOf(500)) < 0)
            .collect(Collectors.toList());
        
        List<Map<String, Object>> segments = new ArrayList<>();
        
        // Segmento VIP
        Map<String, Object> vipSegment = new HashMap<>();
        vipSegment.put("segment", "VIP (>S/2000)");
        vipSegment.put("count", vipCustomers.size());
        vipSegment.put("percentage", clientes.size() > 0 ? Math.round((vipCustomers.size() * 100.0) / clientes.size()) : 0);
        vipSegment.put("revenue", vipCustomers.stream().mapToDouble(c -> c.getTotalCompras().doubleValue()).sum());
        vipSegment.put("emoji", "👑");
        segments.add(vipSegment);
        
        // Segmento Regular
        Map<String, Object> regularSegment = new HashMap<>();
        regularSegment.put("segment", "Regulares (S/500-2000)");
        regularSegment.put("count", regularCustomers.size());
        regularSegment.put("percentage", clientes.size() > 0 ? Math.round((regularCustomers.size() * 100.0) / clientes.size()) : 0);
        regularSegment.put("revenue", regularCustomers.stream().mapToDouble(c -> c.getTotalCompras().doubleValue()).sum());
        regularSegment.put("emoji", "⭐");
        segments.add(regularSegment);
        
        // Segmento Nuevo
        Map<String, Object> newSegment = new HashMap<>();
        newSegment.put("segment", "Nuevos (<S/500)");
        newSegment.put("count", newCustomers.size());
        newSegment.put("percentage", clientes.size() > 0 ? Math.round((newCustomers.size() * 100.0) / clientes.size()) : 0);
        newSegment.put("revenue", newCustomers.stream().mapToDouble(c -> c.getTotalCompras().doubleValue()).sum());
        newSegment.put("emoji", "🆕");
        segments.add(newSegment);
        
        Map<String, Object> result = new HashMap<>();
        result.put("customerSegments", segments);
        return result;
    }
    
    public Map<String, Object> getSalesTrends(int days) {
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minusDays(days);
        
        List<Map<String, Object>> salesData = new ArrayList<>();
        
        for (int i = 0; i < days; i++) {
            LocalDateTime dayStart = startDate.plusDays(i).toLocalDate().atStartOfDay();
            LocalDateTime dayEnd = dayStart.toLocalDate().atTime(23, 59, 59);
            
            BigDecimal dailySales = ventaRepository.sumVentasByFecha(dayStart, dayEnd);
            Long dailyOrders = ventaRepository.countVentasByFecha(dayStart, dayEnd);
            
            Map<String, Object> dayData = new HashMap<>();
            dayData.put("date", dayStart.toLocalDate().toString());
            dayData.put("sales", dailySales != null ? dailySales.doubleValue() : 0);
            dayData.put("orders", dailyOrders != null ? dailyOrders : 0);
            dayData.put("customers", dailyOrders != null ? Math.round(dailyOrders * 0.8) : 0);
            
            salesData.add(dayData);
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("salesData", salesData);
        return result;
    }
}