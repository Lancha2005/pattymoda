package com.pattymoda.repository;

import com.pattymoda.entity.ComentarioTarea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComentarioTareaRepository extends BaseRepository<ComentarioTarea, Long> {
    List<ComentarioTarea> findByTareaId(Long tareaId);
    List<ComentarioTarea> findByUsuarioId(Long usuarioId);
} 