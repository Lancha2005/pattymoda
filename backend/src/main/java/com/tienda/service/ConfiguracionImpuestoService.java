package com.tienda.service;

import com.tienda.entity.ConfiguracionImpuesto;
import com.tienda.repository.ConfiguracionImpuestoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ConfiguracionImpuestoService {
    
    @Autowired
    private ConfiguracionImpuestoRepository configuracionImpuestoRepository;
    
    public List<ConfiguracionImpuesto> obtenerTodosLosImpuestos() {
        return configuracionImpuestoRepository.findAll();
    }
    
    public List<ConfiguracionImpuesto> obtenerImpuestosActivos() {
        return configuracionImpuestoRepository.findByActivoTrue();
    }
    
    public Optional<ConfiguracionImpuesto> obtenerImpuestoPorId(Long id) {
        return configuracionImpuestoRepository.findById(id);
    }
    
    public Optional<ConfiguracionImpuesto> obtenerImpuestoPorDefecto() {
        return configuracionImpuestoRepository.findImpuestoPorDefecto();
    }
    
    public Optional<ConfiguracionImpuesto> obtenerIGV() {
        return configuracionImpuestoRepository.findIGVActivo();
    }
    
    public BigDecimal calcularImpuesto(BigDecimal monto) {
        Optional<ConfiguracionImpuesto> impuesto = obtenerImpuestoPorDefecto();
        if (impuesto.isPresent() && impuesto.get().getActivo()) {
            return monto.multiply(impuesto.get().getPorcentaje().divide(BigDecimal.valueOf(100)));
        }
        return BigDecimal.ZERO;
    }
    
    public BigDecimal calcularIGV(BigDecimal monto) {
        Optional<ConfiguracionImpuesto> igv = obtenerIGV();
        if (igv.isPresent() && igv.get().getActivo()) {
            return monto.multiply(igv.get().getPorcentaje().divide(BigDecimal.valueOf(100)));
        }
        return BigDecimal.ZERO;
    }
    
    @Transactional
    public ConfiguracionImpuesto crearImpuesto(ConfiguracionImpuesto impuesto) {
        // Si se marca como por defecto, desmarcar otros
        if (impuesto.getAplicarPorDefecto()) {
            configuracionImpuestoRepository.findAll().forEach(i -> {
                i.setAplicarPorDefecto(false);
                configuracionImpuestoRepository.save(i);
            });
        }
        return configuracionImpuestoRepository.save(impuesto);
    }
    
    @Transactional
    public ConfiguracionImpuesto actualizarImpuesto(Long id, ConfiguracionImpuesto impuestoActualizado) {
        ConfiguracionImpuesto impuesto = configuracionImpuestoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Configuración de impuesto no encontrada"));
        
        // Si se marca como por defecto, desmarcar otros
        if (impuestoActualizado.getAplicarPorDefecto() && !impuesto.getAplicarPorDefecto()) {
            configuracionImpuestoRepository.findAll().forEach(i -> {
                if (!i.getId().equals(id)) {
                    i.setAplicarPorDefecto(false);
                    configuracionImpuestoRepository.save(i);
                }
            });
        }
        
        impuesto.setNombre(impuestoActualizado.getNombre());
        impuesto.setPorcentaje(impuestoActualizado.getPorcentaje());
        impuesto.setActivo(impuestoActualizado.getActivo());
        impuesto.setAplicarPorDefecto(impuestoActualizado.getAplicarPorDefecto());
        impuesto.setDescripcion(impuestoActualizado.getDescripcion());
        
        return configuracionImpuestoRepository.save(impuesto);
    }
    
    @Transactional
    public void eliminarImpuesto(Long id) {
        ConfiguracionImpuesto impuesto = configuracionImpuestoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Configuración de impuesto no encontrada"));
        impuesto.setActivo(false);
        configuracionImpuestoRepository.save(impuesto);
    }
    
    @Transactional
    public void activarDesactivarIGV(Boolean activar) {
        Optional<ConfiguracionImpuesto> igv = obtenerIGV();
        if (igv.isPresent()) {
            igv.get().setActivo(activar);
            configuracionImpuestoRepository.save(igv.get());
        }
    }
    
    @Transactional
    public void actualizarPorcentajeIGV(BigDecimal nuevoPorcentaje) {
        Optional<ConfiguracionImpuesto> igv = obtenerIGV();
        if (igv.isPresent()) {
            igv.get().setPorcentaje(nuevoPorcentaje);
            configuracionImpuestoRepository.save(igv.get());
        } else {
            // Crear IGV si no existe
            ConfiguracionImpuesto nuevoIGV = new ConfiguracionImpuesto();
            nuevoIGV.setNombre("IGV");
            nuevoIGV.setPorcentaje(nuevoPorcentaje);
            nuevoIGV.setActivo(true);
            nuevoIGV.setAplicarPorDefecto(true);
            nuevoIGV.setDescripcion("Impuesto General a las Ventas");
            configuracionImpuestoRepository.save(nuevoIGV);
        }
    }
}