package com.tienda.repository;

import com.tienda.entity.PasswordResetToken;
import com.tienda.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    
    Optional<PasswordResetToken> findByToken(String token);
    
    Optional<PasswordResetToken> findByUsuarioAndUsadoFalseAndFechaExpiracionAfter(
        Usuario usuario, LocalDateTime now);
    
    @Modifying
    @Query("DELETE FROM PasswordResetToken p WHERE p.fechaExpiracion < :now")
    void deleteExpiredTokens(@Param("now") LocalDateTime now);
    
    @Modifying
    @Query("UPDATE PasswordResetToken p SET p.usado = true WHERE p.usuario = :usuario")
    void invalidateAllUserTokens(@Param("usuario") Usuario usuario);
    
    boolean existsByUsuarioAndUsadoFalseAndFechaExpiracionAfter(Usuario usuario, LocalDateTime now);
}