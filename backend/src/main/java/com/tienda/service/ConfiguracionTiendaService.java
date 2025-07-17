package com.tienda.service;

import com.tienda.entity.ConfiguracionTienda;
import com.tienda.repository.ConfiguracionTiendaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class ConfiguracionTiendaService {
    
    @Autowired
    private ConfiguracionTiendaRepository configuracionTiendaRepository;
    
    public Optional<ConfiguracionTienda> obtenerConfiguracion() {
        return configuracionTiendaRepository.findAll().stream().findFirst();
    }
    
    public ConfiguracionTienda crearOActualizarConfiguracion(ConfiguracionTienda configuracion) {
        Optional<ConfiguracionTienda> existente = obtenerConfiguracion();
        
        if (existente.isPresent()) {
            ConfiguracionTienda config = existente.get();
            config.setNombre(configuracion.getNombre());
            config.setDireccion(configuracion.getDireccion());
            config.setTelefono(configuracion.getTelefono());
            config.setEmail(configuracion.getEmail());
            config.setLogo(configuracion.getLogo());
            config.setSitioWeb(configuracion.getSitioWeb());
            return configuracionTiendaRepository.save(config);
        } else {
            return configuracionTiendaRepository.save(configuracion);
        }
    }
}