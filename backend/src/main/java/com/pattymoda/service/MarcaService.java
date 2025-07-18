package com.pattymoda.service;

import com.pattymoda.entity.Marca;
import com.pattymoda.repository.MarcaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MarcaService extends BaseService<Marca, Long> {

    private final MarcaRepository marcaRepository;

    @Autowired
    public MarcaService(MarcaRepository marcaRepository) {
        super(marcaRepository);
        this.marcaRepository = marcaRepository;
    }

    @Cacheable("marcas")
    public Optional<Marca> findByNombre(String nombre) {
        return marcaRepository.findByNombre(nombre);
    }

    public boolean existsByNombre(String nombre) {
        return marcaRepository.existsByNombre(nombre);
    }

    public Page<Marca> findByActivo(Boolean activo, Pageable pageable) {
        return marcaRepository.findByActivo(activo, pageable);
    }

    public Page<Marca> buscarMarcas(String busqueda, Pageable pageable) {
        return marcaRepository.buscarMarcas(busqueda, pageable);
    }

    public long countMarcasActivas() {
        return marcaRepository.countMarcasActivas();
    }

    @Cacheable("marcas")
    public List<Marca> findByActivoTrueOrderByNombre() {
        return marcaRepository.findByActivoTrueOrderByNombre();
    }

    @Override
    public Marca save(Marca marca) {
        // Validaciones específicas para marcas
        if (marca.getNombre() == null || marca.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la marca es obligatorio");
        }

        // Verificar si el nombre ya existe (excepto para actualizaciones)
        if (marca.getId() == null && existsByNombre(marca.getNombre())) {
            throw new IllegalArgumentException("Ya existe una marca con el nombre: " + marca.getNombre());
        }

        return super.save(marca);
    }

    @Override
    public void deleteById(Long id) {
        Marca marca = findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Marca no encontrada con ID: " + id));

        // Verificar si la marca tiene productos asociados
        // Aquí podrías agregar validaciones adicionales

        marca.setActivo(false);
        save(marca);
    }
}