package com.pattymoda.repository;

import com.pattymoda.entity.ProgramaLealtad;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProgramaLealtadRepository extends BaseRepository<ProgramaLealtad, Long> {
    Optional<ProgramaLealtad> findByClienteId(Long clienteId);
} 