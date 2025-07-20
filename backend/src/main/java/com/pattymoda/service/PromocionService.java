package com.pattymoda.service;

import com.pattymoda.entity.Promocion;
import com.pattymoda.repository.PromocionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PromocionService extends BaseService<Promocion, Long> {
    @Autowired
    public PromocionService(PromocionRepository promocionRepository) {
        super(promocionRepository);
        this.promocionRepository = promocionRepository;
    }

    private final PromocionRepository promocionRepository;

    public Optional<Promocion> findByCodigo(String codigo) {
        return promocionRepository.findByCodigo(codigo);
    }

    public List<Promocion> findActivas() {
        return promocionRepository.findByActivoTrue();
    }

    public boolean existsByCodigo(String codigo) {
        return promocionRepository.existsByCodigo(codigo);
    }

    @Override
    public Promocion save(Promocion promocion) {
        if (promocion.getCodigo() == null || promocion.getCodigo().trim().isEmpty()) {
            throw new IllegalArgumentException("El código de promoción es obligatorio");
        }
        if (promocion.getNombre() == null || promocion.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la promoción es obligatorio");
        }
        if (promocion.getTipoPromocion() == null) {
            throw new IllegalArgumentException("El tipo de promoción es obligatorio");
        }
        if (promocion.getFechaInicio() == null || promocion.getFechaFin() == null) {
            throw new IllegalArgumentException("Las fechas de inicio y fin son obligatorias");
        }
        if (promocion.getId() == null && existsByCodigo(promocion.getCodigo())) {
            throw new IllegalArgumentException("Ya existe una promoción con ese código");
        }
        return super.save(promocion);
    }
} 