package com.pattymoda.controller;

import com.pattymoda.entity.ComentarioTarea;
import com.pattymoda.service.ComentarioTareaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comentarios-tarea")
@Tag(name = "ComentariosTarea", description = "API para gestión de comentarios de tareas de seguimiento")
public class ComentarioTareaController extends BaseController<ComentarioTarea, Long> {
    @Autowired
    public ComentarioTareaController(ComentarioTareaService comentarioTareaService) {
        super(comentarioTareaService);
        this.comentarioTareaService = comentarioTareaService;
    }

    private final ComentarioTareaService comentarioTareaService;

    @GetMapping("/tarea/{tareaId}")
    @Operation(summary = "Buscar comentarios por tarea", description = "Busca todos los comentarios de una tarea")
    public ResponseEntity<List<ComentarioTarea>> getByTarea(@PathVariable Long tareaId) {
        return ResponseEntity.ok(comentarioTareaService.findByTareaId(tareaId));
    }

    @GetMapping("/usuario/{usuarioId}")
    @Operation(summary = "Buscar comentarios por usuario", description = "Busca todos los comentarios hechos por un usuario")
    public ResponseEntity<List<ComentarioTarea>> getByUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(comentarioTareaService.findByUsuarioId(usuarioId));
    }
} 