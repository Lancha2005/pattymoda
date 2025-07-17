package com.tienda.service;

import com.tienda.entity.PasswordResetToken;
import com.tienda.entity.Usuario;
import com.tienda.repository.PasswordResetTokenRepository;
import com.tienda.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class PasswordResetService {
    
    @Autowired
    private PasswordResetTokenRepository tokenRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private EmailService emailService;
    
    @Transactional
    public void requestPasswordReset(String email) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);
        
        if (!usuarioOpt.isPresent()) {
            // Por seguridad, no revelamos si el email existe o no
            return;
        }
        
        Usuario usuario = usuarioOpt.get();
        
        // Verificar límite de intentos (opcional)
        if (usuario.getIntentosRecuperacion() != null && usuario.getIntentosRecuperacion() >= 5) {
            LocalDateTime ultimoIntento = usuario.getUltimoIntentoRecuperacion();
            if (ultimoIntento != null && ultimoIntento.isAfter(LocalDateTime.now().minusHours(1))) {
                throw new RuntimeException("Demasiados intentos de recuperación. Intenta más tarde.");
            } else {
                // Resetear contador después de 1 hora
                usuario.setIntentosRecuperacion(0);
            }
        }
        
        // Verificar si ya existe un token válido
        if (tokenRepository.existsByUsuarioAndUsadoFalseAndFechaExpiracionAfter(usuario, LocalDateTime.now())) {
            throw new RuntimeException("Ya se ha enviado un enlace de recuperación. Revisa tu email.");
        }
        
        // Invalidar tokens anteriores del usuario
        tokenRepository.invalidateAllUserTokens(usuario);
        
        // Generar nuevo token
        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken(usuario, token);
        tokenRepository.save(resetToken);
        
        // Actualizar contador de intentos
        usuario.setIntentosRecuperacion((usuario.getIntentosRecuperacion() != null ? usuario.getIntentosRecuperacion() : 0) + 1);
        usuario.setUltimoIntentoRecuperacion(LocalDateTime.now());
        usuarioRepository.save(usuario);
        
        // Enviar email (simulado)
        emailService.sendPasswordResetEmail(usuario.getEmail(), usuario.getNombre(), token);
    }
    
    @Transactional
    public void resetPassword(String token, String newPassword, String confirmPassword) {
        if (!newPassword.equals(confirmPassword)) {
            throw new RuntimeException("Las contraseñas no coinciden");
        }
        
        Optional<PasswordResetToken> tokenOpt = tokenRepository.findByToken(token);
        
        if (!tokenOpt.isPresent()) {
            throw new RuntimeException("Token inválido");
        }
        
        PasswordResetToken resetToken = tokenOpt.get();
        
        if (!resetToken.isValid()) {
            throw new RuntimeException("Token expirado o ya utilizado");
        }
        
        // Cambiar contraseña
        Usuario usuario = resetToken.getUsuario();
        usuario.setPassword(passwordEncoder.encode(newPassword));
        
        // Resetear contador de intentos
        usuario.setIntentosRecuperacion(0);
        usuario.setUltimoIntentoRecuperacion(null);
        
        usuarioRepository.save(usuario);
        
        // Marcar token como usado
        resetToken.setUsado(true);
        tokenRepository.save(resetToken);
        
        // Invalidar todos los tokens del usuario
        tokenRepository.invalidateAllUserTokens(usuario);
    }
    
    public boolean validateToken(String token) {
        Optional<PasswordResetToken> tokenOpt = tokenRepository.findByToken(token);
        return tokenOpt.isPresent() && tokenOpt.get().isValid();
    }
    
    @Transactional
    public void cleanupExpiredTokens() {
        tokenRepository.deleteExpiredTokens(LocalDateTime.now());
    }
}