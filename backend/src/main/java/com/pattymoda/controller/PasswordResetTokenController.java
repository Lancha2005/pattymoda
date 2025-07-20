package com.pattymoda.controller;

import com.pattymoda.entity.PasswordResetToken;
import com.pattymoda.service.PasswordResetTokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/password-reset-tokens")
@Tag(name = "PasswordResetTokens", description = "API para gestión de tokens de recuperación de contraseña")
public class PasswordResetTokenController extends BaseController<PasswordResetToken, Long> {
    @Autowired
    public PasswordResetTokenController(PasswordResetTokenService passwordResetTokenService) {
        super(passwordResetTokenService);
        this.passwordResetTokenService = passwordResetTokenService;
    }

    private final PasswordResetTokenService passwordResetTokenService;

    @GetMapping("/token/{token}")
    @Operation(summary = "Buscar token de recuperación", description = "Busca un token de recuperación por su valor")
    public ResponseEntity<PasswordResetToken> getByToken(@PathVariable String token) {
        return passwordResetTokenService.findByToken(token)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
} 