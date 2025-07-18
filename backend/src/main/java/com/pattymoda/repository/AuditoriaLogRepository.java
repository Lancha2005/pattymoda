package com.pattymoda.repository;

import com.pattymoda.entity.AuditoriaLog;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuditoriaLogRepository extends BaseRepository<AuditoriaLog, Long> {
    List<AuditoriaLog> findByTabla(String tabla);
    List<AuditoriaLog> findByUsuarioId(Long usuarioId);
    List<AuditoriaLog> findByAccion(AuditoriaLog.Accion accion);
} 