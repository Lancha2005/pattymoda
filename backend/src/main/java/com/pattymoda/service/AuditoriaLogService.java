package com.pattymoda.service;

import com.pattymoda.entity.AuditoriaLog;
import com.pattymoda.repository.AuditoriaLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AuditoriaLogService extends BaseService<AuditoriaLog, Long> {
    @Autowired
    public AuditoriaLogService(AuditoriaLogRepository auditoriaLogRepository) {
        super(auditoriaLogRepository);
        this.auditoriaLogRepository = auditoriaLogRepository;
    }

    private final AuditoriaLogRepository auditoriaLogRepository;

    public List<AuditoriaLog> findByTabla(String tabla) {
        return auditoriaLogRepository.findByTabla(tabla);
    }

    public List<AuditoriaLog> findByUsuarioId(Long usuarioId) {
        return auditoriaLogRepository.findByUsuarioId(usuarioId);
    }

    public List<AuditoriaLog> findByAccion(AuditoriaLog.Accion accion) {
        return auditoriaLogRepository.findByAccion(accion);
    }
} 