package com.pattymoda.service;

import com.pattymoda.entity.Cliente;
import com.pattymoda.entity.Producto;
import com.pattymoda.entity.Usuario;
import com.pattymoda.entity.Venta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificacionService {

    private static final Logger logger = LoggerFactory.getLogger(NotificacionService.class);

    @Autowired
    private JavaMailSender mailSender;

    @Async
    public void enviarNotificacionVenta(Venta venta) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(venta.getCliente().getEmail());
            message.setSubject("Confirmación de Compra - PattyModa");
            message.setText(construirMensajeVenta(venta));
            
            mailSender.send(message);
            logger.info("Notificación de venta enviada a: {}", venta.getCliente().getEmail());
        } catch (Exception e) {
            logger.error("Error enviando notificación de venta: ", e);
        }
    }

    @Async
    public void enviarAlertaStockBajo(List<Producto> productos) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo("admin@pattymoda.com");
            message.setSubject("Alerta: Productos con Stock Bajo");
            message.setText(construirMensajeStockBajo(productos));
            
            mailSender.send(message);
            logger.info("Alerta de stock bajo enviada para {} productos", productos.size());
        } catch (Exception e) {
            logger.error("Error enviando alerta de stock bajo: ", e);
        }
    }

    @Async
    public void enviarNotificacionPromocion(Cliente cliente, String promocion) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(cliente.getEmail());
            message.setSubject("¡Nueva Promoción Especial para Ti! - PattyModa");
            message.setText(construirMensajePromocion(cliente, promocion));
            
            mailSender.send(message);
            logger.info("Notificación de promoción enviada a: {}", cliente.getEmail());
        } catch (Exception e) {
            logger.error("Error enviando notificación de promoción: ", e);
        }
    }

    @Async
    public void enviarRecordatorioCarritoAbandonado(Cliente cliente) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(cliente.getEmail());
            message.setSubject("¡No olvides tu carrito! - PattyModa");
            message.setText(construirMensajeCarritoAbandonado(cliente));
            
            mailSender.send(message);
            logger.info("Recordatorio de carrito abandonado enviado a: {}", cliente.getEmail());
        } catch (Exception e) {
            logger.error("Error enviando recordatorio de carrito: ", e);
        }
    }

    private String construirMensajeVenta(Venta venta) {
        return String.format(
            "Hola %s,\n\n" +
            "¡Gracias por tu compra en PattyModa!\n\n" +
            "Detalles de tu pedido:\n" +
            "Número de venta: %s\n" +
            "Total: S/ %.2f\n" +
            "Estado: %s\n\n" +
            "Te notificaremos cuando tu pedido esté listo.\n\n" +
            "¡Gracias por elegirnos!\n" +
            "Equipo PattyModa",
            venta.getCliente().getNombre(),
            venta.getNumeroVenta(),
            venta.getTotal(),
            venta.getEstado()
        );
    }

    private String construirMensajeStockBajo(List<Producto> productos) {
        StringBuilder mensaje = new StringBuilder();
        mensaje.append("Los siguientes productos tienen stock bajo:\n\n");
        
        for (Producto producto : productos) {
            mensaje.append(String.format("- %s (Código: %s)\n", 
                producto.getNombre(), producto.getCodigoProducto()));
        }
        
        mensaje.append("\nPor favor, considera realizar un pedido de reposición.");
        return mensaje.toString();
    }

    private String construirMensajePromocion(Cliente cliente, String promocion) {
        return String.format(
            "Hola %s,\n\n" +
            "¡Tenemos una promoción especial para ti!\n\n" +
            "%s\n\n" +
            "Visita nuestra tienda o sitio web para aprovechar esta oferta.\n\n" +
            "¡No te lo pierdas!\n" +
            "Equipo PattyModa",
            cliente.getNombre(),
            promocion
        );
    }

    private String construirMensajeCarritoAbandonado(Cliente cliente) {
        return String.format(
            "Hola %s,\n\n" +
            "Notamos que dejaste algunos productos en tu carrito.\n\n" +
            "¡No pierdas la oportunidad de llevarte esas prendas que te gustaron!\n\n" +
            "Completa tu compra ahora y disfruta de nuestros productos de calidad.\n\n" +
            "¡Te esperamos!\n" +
            "Equipo PattyModa",
            cliente.getNombre()
        );
    }
}