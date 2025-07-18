package com.pattymoda.service;

import com.pattymoda.entity.Color;
import com.pattymoda.repository.ColorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ColorService extends BaseService<Color, Long> {
    @Autowired
    public ColorService(ColorRepository colorRepository) {
        super(colorRepository);
        this.colorRepository = colorRepository;
    }

    private final ColorRepository colorRepository;

    public Optional<Color> findByCodigo(String codigo) {
        return colorRepository.findByCodigo(codigo);
    }

    public Optional<Color> findByCodigoHex(String codigoHex) {
        return colorRepository.findByCodigoHex(codigoHex);
    }

    public List<Color> findAllActivos() {
        return colorRepository.findByActivoTrueOrderByOrdenVisualizacionAsc();
    }

    public List<Color> findByFamiliaColor(String familiaColor) {
        return colorRepository.findByFamiliaColorAndActivoTrueOrderByOrdenVisualizacionAsc(familiaColor);
    }

    public List<Color> findByFamilias(List<String> familias) {
        return colorRepository.findByFamiliasAndActivo(familias);
    }

    public boolean existsByCodigo(String codigo) {
        return colorRepository.existsByCodigo(codigo);
    }

    public long countActivos() {
        return colorRepository.countActivos();
    }

    @Override
    public Color save(Color color) {
        // Validaciones específicas para colores
        if (color.getCodigo() == null || color.getCodigo().trim().isEmpty()) {
            throw new IllegalArgumentException("El código de color es obligatorio");
        }

        if (color.getNombre() == null || color.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de color es obligatorio");
        }

        if (color.getCodigoHex() == null || color.getCodigoHex().trim().isEmpty()) {
            throw new IllegalArgumentException("El código hexadecimal es obligatorio");
        }

        // Validar formato de código hexadecimal
        if (!color.getCodigoHex().matches("^#[0-9A-Fa-f]{6}$")) {
            throw new IllegalArgumentException("El código hexadecimal debe tener el formato #RRGGBB");
        }

        // Verificar si el código ya existe (excepto para actualizaciones)
        if (color.getId() == null && existsByCodigo(color.getCodigo())) {
            throw new IllegalArgumentException("Ya existe un color con el código: " + color.getCodigo());
        }

        return super.save(color);
    }

    @Override
    public void deleteById(Long id) {
        Color color = findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Color no encontrado con ID: " + id));

        // Verificar si el color está siendo utilizado en productos
        // Aquí podrías agregar validaciones adicionales

        color.setActivo(false);
        save(color);
    }
} 