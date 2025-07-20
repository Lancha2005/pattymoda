package com.pattymoda.service;

import com.pattymoda.entity.Usuario;
import com.pattymoda.repository.UsuarioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UsuarioService extends BaseService<Usuario, Long> {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        super(usuarioRepository);
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<Usuario> findByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public Optional<Usuario> findByCodigoEmpleado(String codigoEmpleado) {
        return usuarioRepository.findByCodigoEmpleado(codigoEmpleado);
    }

    public boolean existsByEmail(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    public boolean existsByCodigoEmpleado(String codigoEmpleado) {
        return usuarioRepository.existsByCodigoEmpleado(codigoEmpleado);
    }

    public Page<Usuario> findByActivo(Boolean activo, Pageable pageable) {
        return usuarioRepository.findByActivo(activo, pageable);
    }

    public Page<Usuario> findByRolId(Long rolId, Pageable pageable) {
        return usuarioRepository.findByRolId(rolId, pageable);
    }

    public Page<Usuario> buscarUsuariosActivos(String busqueda, Pageable pageable) {
        return usuarioRepository.buscarUsuariosActivos(busqueda, pageable);
    }

    public List<Usuario> findByRolCodigo(String rolCodigo) {
        return usuarioRepository.findByRolCodigo(rolCodigo);
    }

    public long countUsuariosActivos() {
        return usuarioRepository.countUsuariosActivos();
    }

    @Override
    public Usuario save(Usuario usuario) {
        // Encriptar contraseña si es nueva o se está actualizando
        if (usuario.getId() == null || usuario.getPassword() != null) {
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        }
        
        // Actualizar último acceso
        usuario.setUltimoAcceso(LocalDateTime.now());
        
        return usuarioRepository.save(usuario);
    }

    public Usuario crearUsuario(Usuario usuario) {
        // Validar que el email no exista
        if (existsByEmail(usuario.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }

        // Validar que el código de empleado no exista
        if (usuario.getCodigoEmpleado() != null && existsByCodigoEmpleado(usuario.getCodigoEmpleado())) {
            throw new RuntimeException("El código de empleado ya está registrado");
        }

        return save(usuario);
    }

    public Usuario actualizarUsuario(Long id, Usuario usuarioActualizado) {
        Usuario usuarioExistente = findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Validar que el email no esté en uso por otro usuario
        if (!usuarioExistente.getEmail().equals(usuarioActualizado.getEmail()) &&
                existsByEmail(usuarioActualizado.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }

        // Actualizar campos
        usuarioExistente.setNombre(usuarioActualizado.getNombre());
        usuarioExistente.setApellido(usuarioActualizado.getApellido());
        usuarioExistente.setEmail(usuarioActualizado.getEmail());
        usuarioExistente.setTelefono(usuarioActualizado.getTelefono());
        usuarioExistente.setDireccion(usuarioActualizado.getDireccion());
        usuarioExistente.setRol(usuarioActualizado.getRol());
        usuarioExistente.setActivo(usuarioActualizado.getActivo());

        // Actualizar contraseña solo si se proporciona una nueva
        if (usuarioActualizado.getPassword() != null && !usuarioActualizado.getPassword().isEmpty()) {
            usuarioExistente.setPassword(passwordEncoder.encode(usuarioActualizado.getPassword()));
        }

        return save(usuarioExistente);
    }

    public void cambiarEstado(Long id, Boolean activo) {
        Usuario usuario = findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        usuario.setActivo(activo);
        save(usuario);
    }

    public void actualizarUltimoAcceso(Long id) {
        Usuario usuario = findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        usuario.setUltimoAcceso(LocalDateTime.now());
        save(usuario);
    }

    public void incrementarIntentosFallidos(Long id) {
        Usuario usuario = findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        usuario.setIntentosLoginFallidos(usuario.getIntentosLoginFallidos() + 1);
        
        // Bloquear usuario si tiene más de 5 intentos fallidos
        if (usuario.getIntentosLoginFallidos() >= 5) {
            usuario.setBloqueadoHasta(LocalDateTime.now().plusMinutes(30));
        }
        
        save(usuario);
    }

    public void resetearIntentosFallidos(Long id) {
        Usuario usuario = findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        usuario.setIntentosLoginFallidos(0);
        usuario.setBloqueadoHasta(null);
        save(usuario);
    }
} 