package com.pattymoda.repository;

import com.pattymoda.entity.ClientePreferencia;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ClientePreferenciaRepository extends BaseRepository<ClientePreferencia, Long> {
    List<ClientePreferencia> findByClienteId(Long clienteId);
} 