package com.pattymoda.service;

import com.pattymoda.entity.ComentarioTarea;
import com.pattymoda.repository.ComentarioTareaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ComentarioTareaService extends BaseService<ComentarioTarea, Long> {
    @Autowired
    public ComentarioTareaService(ComentarioTareaRepository comentarioTareaRepository) {
        super(comentarioTareaRepository);
        this.comentarioTareaRepository = comentarioTareaRepository;
    }

    private final ComentarioTareaRepository comentarioTareaRepository;

    public List<ComentarioTarea> findByTareaId(Long tareaId) {
        return comentarioTareaRepository.findByTareaId(tareaId);
    }

    public List<ComentarioTarea> findByUsuarioId(Long usuarioId) {
        return comentarioTareaRepository.findByUsuarioId(usuarioId);
    }
} 