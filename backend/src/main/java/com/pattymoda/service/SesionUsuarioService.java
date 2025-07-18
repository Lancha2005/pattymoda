package com.pattymoda.service;

import com.pattymoda.entity.SesionUsuario;
import com.pattymoda.repository.SesionUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SesionUsuarioService extends BaseService<SesionUsuario, Long> {
    @Autowired
    public SesionUsuarioService(SesionUsuarioRepository sesionUsuarioRepository) {
        super(sesionUsuarioRepository);
        this.sesionUsuarioRepository = sesionUsuarioRepository;
    }

    private final SesionUsuarioRepository sesionUsuarioRepository;

    public List<SesionUsuario> findByUsuarioId(Long usuarioId) {
        return sesionUsuarioRepository.findByUsuarioId(usuarioId);
    }

    public Optional<SesionUsuario> findByTokenSesion(String tokenSesion) {
        return sesionUsuarioRepository.findByTokenSesion(tokenSesion);
    }
} 