package com.pattymoda.repository;

import com.pattymoda.entity.PasswordResetToken;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PasswordResetTokenRepository extends BaseRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByToken(String token);
    boolean existsByToken(String token);
} 