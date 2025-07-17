package com.tienda.controller;

import com.tienda.dto.PasswordResetRequest;
import com.tienda.dto.PasswordResetConfirmRequest;
import com.tienda.service.PasswordResetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Recuperación de Contraseña", description = "Endpoints para recuperación de contraseña")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
public class PasswordResetController {
    
    @Autowired
    private PasswordResetService passwordResetService;
    
    @PostMapping("/forgot-password")
    @Operation(summary = "Solicitar recuperación de contraseña", description = "Envía un email con enlace para resetear contraseña")
    public ResponseEntity<Map<String, Object>> forgotPassword(@Valid @RequestBody PasswordResetRequest request) {
        try {
            passwordResetService.requestPasswordReset(request.getEmail());
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Si el email existe, se ha enviado un enlace de recuperación");
            response.put("status", 200);
            response.put("timestamp", java.time.LocalDateTime.now());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", e.getMessage());
            errorResponse.put("status", 400);
            errorResponse.put("timestamp", java.time.LocalDateTime.now());
            
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    
    @PostMapping("/reset-password")
    @Operation(summary = "Resetear contraseña", description = "Cambia la contraseña usando el token de recuperación")
    public ResponseEntity<Map<String, Object>> resetPassword(@Valid @RequestBody PasswordResetConfirmRequest request) {
        try {
            passwordResetService.resetPassword(request.getToken(), request.getNewPassword(), request.getConfirmPassword());
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Contraseña cambiada exitosamente");
            response.put("status", 200);
            response.put("timestamp", java.time.LocalDateTime.now());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", e.getMessage());
            errorResponse.put("status", 400);
            errorResponse.put("timestamp", java.time.LocalDateTime.now());
            
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    
    @GetMapping("/validate-reset-token")
    @Operation(summary = "Validar token de recuperación", description = "Verifica si un token de recuperación es válido")
    public ResponseEntity<Map<String, Object>> validateResetToken(@RequestParam String token) {
        try {
            boolean isValid = passwordResetService.validateToken(token);
            
            Map<String, Object> response = new HashMap<>();
            response.put("valid", isValid);
            response.put("message", isValid ? "Token válido" : "Token inválido o expirado");
            response.put("status", 200);
            response.put("timestamp", java.time.LocalDateTime.now());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("valid", false);
            errorResponse.put("message", "Error al validar token");
            errorResponse.put("status", 400);
            errorResponse.put("timestamp", java.time.LocalDateTime.now());
            
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
}