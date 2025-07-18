package com.pattymoda.service;

import com.pattymoda.entity.Talla;
import com.pattymoda.repository.TallaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TallaService extends BaseService<Talla, Long> {
    @Autowired
    public TallaService(TallaRepository tallaRepository) {
        super(tallaRepository);
        this.tallaRepository = tallaRepository;
    }

    private final TallaRepository tallaRepository;

    public Optional<Talla> findByCodigo(String codigo) {
        return tallaRepository.findByCodigo(codigo);
    }

    public List<Talla> findAllActivas() {
        return tallaRepository.findByActivoTrueOrderByOrdenVisualizacionAsc();
    }

    public List<Talla> findByCategoria(Talla.CategoriaTalla categoriaTalla) {
        return tallaRepository.findByCategoriaTallaAndActivoTrueOrderByOrdenVisualizacionAsc(categoriaTalla);
    }

    public List<Talla> findByCategorias(List<Talla.CategoriaTalla> categorias) {
        return tallaRepository.findByCategoriasAndActivo(categorias);
    }

    public boolean existsByCodigo(String codigo) {
        return tallaRepository.existsByCodigo(codigo);
    }

    public long countActivas() {
        return tallaRepository.countActivas();
    }

    @Override
    public Talla save(Talla talla) {
        // Validaciones específicas para tallas
        if (talla.getCodigo() == null || talla.getCodigo().trim().isEmpty()) {
            throw new IllegalArgumentException("El código de talla es obligatorio");
        }

        if (talla.getNombre() == null || talla.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de talla es obligatorio");
        }

        if (talla.getCategoriaTalla() == null) {
            throw new IllegalArgumentException("La categoría de talla es obligatoria");
        }

        // Verificar si el código ya existe (excepto para actualizaciones)
        if (talla.getId() == null && existsByCodigo(talla.getCodigo())) {
            throw new IllegalArgumentException("Ya existe una talla con el código: " + talla.getCodigo());
        }

        return super.save(talla);
    }

    @Override
    public void deleteById(Long id) {
        Talla talla = findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Talla no encontrada con ID: " + id));

        // Verificar si la talla está siendo utilizada en productos
        // Aquí podrías agregar validaciones adicionales

        talla.setActivo(false);
        save(talla);
    }
} 