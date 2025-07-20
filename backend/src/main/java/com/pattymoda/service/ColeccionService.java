package com.pattymoda.service;

import com.pattymoda.entity.Coleccion;
import com.pattymoda.repository.ColeccionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ColeccionService extends BaseService<Coleccion, Long> {

    private final ColeccionRepository coleccionRepository;

    @Autowired
    public ColeccionService(ColeccionRepository coleccionRepository) {
        super(coleccionRepository);
        this.coleccionRepository = coleccionRepository;
    }

    public Optional<Coleccion> findByCodigo(String codigo) {
        return coleccionRepository.findByCodigo(codigo);
    }

    public Optional<Coleccion> findBySlug(String slug) {
        return coleccionRepository.findBySlug(slug);
    }

    @Cacheable("colecciones")
    public Page<Coleccion> getColeccionesActivas(Pageable pageable) {
        return coleccionRepository.findByActivoTrueOrderByOrdenVisualizacionAsc(pageable);
    }

    @Cacheable("colecciones")
    public List<Coleccion> getColeccionesDestacadas() {
        return coleccionRepository.findByDestacadoTrueAndActivoTrueOrderByOrdenVisualizacionAsc();
    }

    public List<Coleccion> getColeccionesPorTemporada(Long temporadaId) {
        return coleccionRepository.findByTemporadaActiva(temporadaId);
    }

    public List<Coleccion> getColeccionesDisponibles() {
        return coleccionRepository.findColeccionesDisponibles(LocalDate.now());
    }

    public List<Coleccion> getColeccionesPorTipo(Coleccion.TipoColeccion tipo) {
        return coleccionRepository.findByTipoColeccionAndActivoTrue(tipo);
    }

    public Page<Coleccion> buscarColecciones(String busqueda, Pageable pageable) {
        return coleccionRepository.buscarColecciones(busqueda, pageable);
    }

    @Override
    public Coleccion save(Coleccion coleccion) {
        if (coleccion.getCodigo() == null || coleccion.getCodigo().trim().isEmpty()) {
            throw new IllegalArgumentException("El código de colección es obligatorio");
        }
        if (coleccion.getNombre() == null || coleccion.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de colección es obligatorio");
        }
        if (coleccion.getTemporada() == null) {
            throw new IllegalArgumentException("La temporada es obligatoria");
        }

        // Generar slug automáticamente si no se proporciona
        if (coleccion.getSlug() == null || coleccion.getSlug().trim().isEmpty()) {
            coleccion.setSlug(generarSlug(coleccion.getNombre()));
        }

        if (coleccion.getId() == null && coleccionRepository.existsByCodigo(coleccion.getCodigo())) {
            throw new IllegalArgumentException("Ya existe una colección con ese código");
        }
        if (coleccion.getId() == null && coleccionRepository.existsBySlug(coleccion.getSlug())) {
            throw new IllegalArgumentException("Ya existe una colección con ese slug");
        }

        return super.save(coleccion);
    }

    private String generarSlug(String nombre) {
        return nombre.toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-")
                .replaceAll("-+", "-")
                .trim();
    }
}