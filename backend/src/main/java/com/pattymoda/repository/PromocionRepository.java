package com.pattymoda.repository;

import com.pattymoda.entity.Promocion;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface PromocionRepository extends BaseRepository<Promocion, Long> {
    Optional<Promocion> findByCodigo(String codigo);
    List<Promocion> findByActivoTrue();
    boolean existsByCodigo(String codigo);
} 