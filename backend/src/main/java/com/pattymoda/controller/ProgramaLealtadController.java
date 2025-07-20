package com.pattymoda.controller;

import com.pattymoda.entity.ProgramaLealtad;
import com.pattymoda.service.ProgramaLealtadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/programa-lealtad")
@Tag(name = "ProgramaLealtad", description = "API para gestión del programa de lealtad")
public class ProgramaLealtadController extends BaseController<ProgramaLealtad, Long> {
    @Autowired
    public ProgramaLealtadController(ProgramaLealtadService programaLealtadService) {
        super(programaLealtadService);
        this.programaLealtadService = programaLealtadService;
    }

    private final ProgramaLealtadService programaLealtadService;

    @GetMapping("/cliente/{clienteId}")
    @Operation(summary = "Obtener programa de lealtad por cliente", description = "Obtiene el programa de lealtad de un cliente")
    public ResponseEntity<ProgramaLealtad> getByCliente(@PathVariable Long clienteId) {
        return programaLealtadService.findByClienteId(clienteId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
} 