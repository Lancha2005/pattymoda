package com.pattymoda.repository;

import com.pattymoda.entity.SesionUsuario;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SesionUsuarioRepository extends BaseRepository<SesionUsuario, Long> {
    List<SesionUsuario> findByUsuarioId(Long usuarioId);
    Optional<SesionUsuario> findByTokenSesion(String tokenSesion);
} 