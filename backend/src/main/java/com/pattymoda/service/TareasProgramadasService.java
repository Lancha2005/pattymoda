package com.pattymoda.service;

import com.pattymoda.entity.Producto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TareasProgramadasService {

    private static final Logger logger = LoggerFactory.getLogger(TareasProgramadasService.class);

    @Autowired
    private InventarioService inventarioService;
    
    @Autowired
    private NotificacionService notificacionService;

    // Verificar stock bajo cada hora
    @Scheduled(fixedRate = 3600000) // 1 hora
    public void verificarStockBajo() {
        logger.info("Ejecutando verificación de stock bajo...");
        
        try {
            List<Producto> productosStockBajo = inventarioService.getProductosStockBajo();
            
            if (!productosStockBajo.isEmpty()) {
                logger.warn("Se encontraron {} productos con stock bajo", productosStockBajo.size());
                notificacionService.enviarAlertaStockBajo(productosStockBajo);
            }
        } catch (Exception e) {
            logger.error("Error en verificación de stock bajo: ", e);
        }
    }

    // Limpiar tokens expirados cada día a las 2 AM
    @Scheduled(cron = "0 0 2 * * *")
    public void limpiarTokensExpirados() {
        logger.info("Ejecutando limpieza de tokens expirados...");
        
        try {
            // Implementar limpieza de tokens JWT expirados
            // passwordResetTokenService.limpiarTokensExpirados();
            // sesionUsuarioService.limpiarSesionesExpiradas();
        } catch (Exception e) {
            logger.error("Error en limpieza de tokens: ", e);
        }
    }

    // Generar reportes automáticos cada lunes a las 8 AM
    @Scheduled(cron = "0 0 8 * * MON")
    public void generarReporteSemanal() {
        logger.info("Generando reporte semanal automático...");
        
        try {
            // Implementar generación de reportes automáticos
            // reporteService.generarReporteSemanal();
        } catch (Exception e) {
            logger.error("Error generando reporte semanal: ", e);
        }
    }

    // Actualizar niveles de clientes cada día a medianoche
    @Scheduled(cron = "0 0 0 * * *")
    public void actualizarNivelesClientes() {
        logger.info("Actualizando niveles de clientes...");
        
        try {
            // Implementar actualización de niveles de clientes basado en compras
            // clienteService.actualizarNivelesClientes();
        } catch (Exception e) {
            logger.error("Error actualizando niveles de clientes: ", e);
        }
    }
}