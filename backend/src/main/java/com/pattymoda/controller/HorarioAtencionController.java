package com.pattymoda.controller;

import com.pattymoda.entity.HorarioAtencion;
import com.pattymoda.service.HorarioAtencionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/horarios-atencion")
@Tag(name = "HorariosAtencion", description = "API para gestión de horarios de atención")
public class HorarioAtencionController extends BaseController<HorarioAtencion, Long> {
    @Autowired
    public HorarioAtencionController(HorarioAtencionService horarioAtencionService) {
        super(horarioAtencionService);
        this.horarioAtencionService = horarioAtencionService;
    }

    private final HorarioAtencionService horarioAtencionService;

    @GetMapping("/dia/{dia}")
    @Operation(summary = "Buscar horario por día", description = "Busca el horario de atención por día de la semana")
    public ResponseEntity<HorarioAtencion> getByDia(@PathVariable HorarioAtencion.DiaSemana dia) {
        Optional<HorarioAtencion> horario = horarioAtencionService.findByDiaSemana(dia);
        return horario.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/activos")
    @Operation(summary = "Obtener horarios activos", description = "Obtiene todos los horarios de atención activos")
    public ResponseEntity<List<HorarioAtencion>> getActivos() {
        return ResponseEntity.ok(horarioAtencionService.findActivos());
    }
} 