package com.pattymoda.service;

import com.pattymoda.entity.ProgramaLealtad;
import com.pattymoda.repository.ProgramaLealtadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class ProgramaLealtadService extends BaseService<ProgramaLealtad, Long> {
    @Autowired
    public ProgramaLealtadService(ProgramaLealtadRepository programaLealtadRepository) {
        super(programaLealtadRepository);
        this.programaLealtadRepository = programaLealtadRepository;
    }

    private final ProgramaLealtadRepository programaLealtadRepository;

    public Optional<ProgramaLealtad> findByClienteId(Long clienteId) {
        return programaLealtadRepository.findByClienteId(clienteId);
    }
} 