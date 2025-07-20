package com.pattymoda.service;

import com.pattymoda.entity.PasswordResetToken;
import com.pattymoda.repository.PasswordResetTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class PasswordResetTokenService extends BaseService<PasswordResetToken, Long> {
    @Autowired
    public PasswordResetTokenService(PasswordResetTokenRepository passwordResetTokenRepository) {
        super(passwordResetTokenRepository);
        this.passwordResetTokenRepository = passwordResetTokenRepository;
    }

    private final PasswordResetTokenRepository passwordResetTokenRepository;

    public Optional<PasswordResetToken> findByToken(String token) {
        return passwordResetTokenRepository.findByToken(token);
    }

    public boolean existsByToken(String token) {
        return passwordResetTokenRepository.existsByToken(token);
    }
} 