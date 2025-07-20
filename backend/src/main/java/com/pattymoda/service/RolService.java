package com.pattymoda.service;

import com.pattymoda.entity.Rol;
import com.pattymoda.repository.RolRepository;
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
public class RolService extends BaseService<Rol, Long> {

    private final RolRepository rolRepository;

    @Autowired
    public RolService(RolRepository rolRepository) {
        super(rolRepository);
        this.rolRepository = rolRepository;
    }

    @Cacheable("roles")
    public Optional<Rol> findByCodigo(String codigo) {
        return rolRepository.findByCodigo(codigo);
    }

    public boolean existsByCodigo(String codigo) {
        return rolRepository.existsByCodigo(codigo);
    }

    public Page<Rol> findByActivo(Boolean activo, Pageable pageable) {
        return rolRepository.findByActivo(activo, pageable);
    }

    @Cacheable("roles")
    public List<Rol> findByActivoTrueOrderByNombre() {
        return rolRepository.findByActivoTrueOrderByNombre();
    }

    public long countRolesActivos() {
        return rolRepository.countRolesActivos();
    }

    @Override
    public Rol save(Rol rol) {
        // Validaciones específicas para roles
        if (rol.getCodigo() == null || rol.getCodigo().trim().isEmpty()) {
            throw new IllegalArgumentException("El código del rol es obligatorio");
        }

        if (rol.getNombre() == null || rol.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del rol es obligatorio");
        }

        // Verificar si el código ya existe (excepto para actualizaciones)
        if (rol.getId() == null && existsByCodigo(rol.getCodigo())) {
            throw new IllegalArgumentException("Ya existe un rol con el código: " + rol.getCodigo());
        }

        return super.save(rol);
    }

    @Override
    public void deleteById(Long id) {
        Rol rol = findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Rol no encontrado con ID: " + id));

        // Verificar si el rol tiene usuarios asociados
        // Aquí podrías agregar validaciones adicionales

        rol.setActivo(false);
        save(rol);
    }
}