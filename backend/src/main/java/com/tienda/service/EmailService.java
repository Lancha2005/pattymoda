package com.tienda.service;

import org.springframework.stereotype.Service;

@Service
public class EmailService {
    
    public void sendPasswordResetEmail(String email, String nombre, String token) {
        // Simulación de envío de email
        // En producción, aquí integrarías un servicio real como SendGrid, AWS SES, etc.
        
        String resetUrl = "http://localhost:5173/reset-password?token=" + token;
        
        System.out.println("=== EMAIL DE RECUPERACIÓN DE CONTRASEÑA ===");
        System.out.println("Para: " + email);
        System.out.println("Asunto: Recuperación de contraseña - DPattyModa");
        System.out.println("Contenido:");
        System.out.println("Hola " + nombre + ",");
        System.out.println("");
        System.out.println("Has solicitado restablecer tu contraseña en DPattyModa.");
        System.out.println("Haz clic en el siguiente enlace para crear una nueva contraseña:");
        System.out.println("");
        System.out.println(resetUrl);
        System.out.println("");
        System.out.println("Este enlace expirará en 1 hora por seguridad.");
        System.out.println("Si no solicitaste este cambio, puedes ignorar este email.");
        System.out.println("");
        System.out.println("Saludos,");
        System.out.println("Equipo DPattyModa");
        System.out.println("Pampa Hermosa, Loreto - Perú");
        System.out.println("==========================================");
        
        // TODO: Implementar envío real de email
        // Ejemplo con SendGrid:
        // sendGridService.send(email, "Recuperación de contraseña", emailContent);
    }
}