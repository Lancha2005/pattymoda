package com.tienda.service;

import com.tienda.entity.Producto;
import com.tienda.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.*;

@Service
public class DashboardService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();

        // Estadísticas de productos
        long totalProducts = productoRepository.count();
        long activeProducts = productoRepository.findByActivoTrue().size();
        List<Producto> lowStockProducts = productoRepository.findProductosConStockBajo();

        // Estadísticas de clientes
        long totalCustomers = clienteRepository.count();
        long activeCustomers = clienteRepository.findByActivoTrue().size();

        // Estadísticas de ventas
        LocalDateTime startOfMonth = LocalDate.now().withDayOfMonth(1).atStartOfDay();
        LocalDateTime endOfMonth = LocalDate.now().atTime(23, 59, 59);

        BigDecimal monthlyRevenue = ventaRepository.sumVentasByFecha(startOfMonth, endOfMonth);
        Long monthlySales = ventaRepository.countVentasByFecha(startOfMonth, endOfMonth);

        // Ventas de hoy
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().atTime(23, 59, 59);

        BigDecimal dailyRevenue = ventaRepository.sumVentasByFecha(startOfDay, endOfDay);
        Long dailySales = ventaRepository.countVentasByFecha(startOfDay, endOfDay);

        // Estadísticas de usuarios
        long totalUsers = usuarioRepository.count();

        stats.put("totalProducts", totalProducts);
        stats.put("activeProducts", activeProducts);
        stats.put("lowStockProducts", lowStockProducts.size());
        stats.put("totalCustomers", totalCustomers);
        stats.put("activeCustomers", activeCustomers);
        stats.put("monthlyRevenue", monthlyRevenue != null ? monthlyRevenue : BigDecimal.ZERO);
        stats.put("monthlySales", monthlySales != null ? monthlySales : 0);
        stats.put("dailyRevenue", dailyRevenue != null ? dailyRevenue : BigDecimal.ZERO);
        stats.put("dailySales", dailySales != null ? dailySales : 0);
        stats.put("totalUsers", totalUsers);

        return stats;
    }

    public Map<String, Object> getRecentActivity() {
        Map<String, Object> activity = new HashMap<>();
        List<Map<String, Object>> activities = new ArrayList<>();

        // Productos con stock bajo
        List<Producto> lowStockProducts = productoRepository.findProductosConStockBajo();
        if (!lowStockProducts.isEmpty()) {
            Map<String, Object> stockActivity = new HashMap<>();
            stockActivity.put("type", "stock");
            stockActivity.put("message", lowStockProducts.size() + " productos con stock bajo");
            stockActivity.put("time", "Ahora");
            stockActivity.put("priority", "high");
            activities.add(stockActivity);
        }

        // Ventas recientes (últimas 24 horas)
        LocalDateTime yesterday = LocalDateTime.now().minusDays(1);
        Long recentSales = ventaRepository.countVentasByFecha(yesterday, LocalDateTime.now());
        if (recentSales > 0) {
            Map<String, Object> salesActivity = new HashMap<>();
            salesActivity.put("type", "sale");
            salesActivity.put("message", recentSales + " ventas en las últimas 24 horas");
            salesActivity.put("time", "24h");
            salesActivity.put("priority", "medium");
            activities.add(salesActivity);
        }

        // Nuevos clientes (última semana)
        LocalDateTime lastWeek = LocalDateTime.now().minusDays(7);
        long newCustomers = clienteRepository.findAll().stream()
                .filter(c -> c.getFechaCreacion().isAfter(lastWeek))
                .count();

        if (newCustomers > 0) {
            Map<String, Object> customerActivity = new HashMap<>();
            customerActivity.put("type", "customer");
            customerActivity.put("message", newCustomers + " nuevos clientes esta semana");
            customerActivity.put("time", "7d");
            customerActivity.put("priority", "low");
            activities.add(customerActivity);
        }

        activity.put("activities", activities);
        activity.put("lastUpdate", LocalDateTime.now());

        return activity;
    }
}