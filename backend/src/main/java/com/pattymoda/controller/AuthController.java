package com.pattymoda.controller;

import com.pattymoda.entity.Usuario;
import com.pattymoda.security.JwtTokenProvider;
import com.pattymoda.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticación", description = "API para autenticación de usuarios")
@CrossOrigin(origins = "*")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UsuarioService usuarioService;

    public AuthController(AuthenticationManager authenticationManager,
                         JwtTokenProvider jwtTokenProvider,
                         UsuarioService usuarioService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.usuarioService = usuarioService;
    }

    @Operation(summary = "Iniciar sesión", description = "Autentica un usuario y devuelve un token JWT")
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            logger.info("Intento de login para usuario: {}", loginRequest.getEmail());

            // Verificar si el usuario existe
            Usuario usuario = usuarioService.findByEmail(loginRequest.getEmail())
                    .orElseThrow(() -> new BadCredentialsException("Usuario no encontrado"));

            // Verificar si el usuario está activo
            if (!usuario.getActivo()) {
                throw new BadCredentialsException("Usuario inactivo");
            }

            // Verificar si el usuario está bloqueado
            if (usuario.getBloqueadoHasta() != null && 
                usuario.getBloqueadoHasta().isAfter(java.time.LocalDateTime.now())) {
                throw new BadCredentialsException("Usuario bloqueado temporalmente");
            }

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtTokenProvider.generateToken(authentication);

            // Actualizar último acceso y resetear intentos fallidos
            usuarioService.actualizarUltimoAcceso(usuario.getId());
            usuarioService.resetearIntentosFallidos(usuario.getId());

            // Preparar respuesta
            Map<String, Object> response = new HashMap<>();
            response.put("token", jwt);
            response.put("tipo", "Bearer");
            response.put("usuario", Map.of(
                "id", usuario.getId(),
                "nombre", usuario.getNombre(),
                "apellido", usuario.getApellido() != null ? usuario.getApellido() : "",
                "email", usuario.getEmail(),
                "rol", Map.of(
                    "id", usuario.getRol().getId(),
                    "codigo", usuario.getRol().getCodigo(),
                    "nombre", usuario.getRol().getNombre()
                )
            ));

            logger.info("Login exitoso para usuario: {}", loginRequest.getEmail());
            return ResponseEntity.ok(response);

        } catch (BadCredentialsException e) {
            logger.warn("Credenciales inválidas para usuario: {}", loginRequest.getEmail());
            
            // Incrementar intentos fallidos si el usuario existe
            usuarioService.findByEmail(loginRequest.getEmail())
                    .ifPresent(usuario -> usuarioService.incrementarIntentosFallidos(usuario.getId()));

            Map<String, String> error = new HashMap<>();
            error.put("mensaje", "Credenciales inválidas");
            return ResponseEntity.badRequest().body(error);
            
        } catch (Exception e) {
            logger.error("Error durante el login: ", e);
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", "Error interno del servidor");
            return ResponseEntity.internalServerError().body(error);
        }
    }

    @Operation(summary = "Registrar nuevo usuario", description = "Registra un nuevo usuario en el sistema")
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody Usuario usuario) {
        try {
            logger.info("Intento de registro para usuario: {}", usuario.getEmail());
            
            Usuario nuevoUsuario = usuarioService.crearUsuario(usuario);
            
            // Remover información sensible de la respuesta
            nuevoUsuario.setPassword(null);
            
            logger.info("Usuario registrado exitosamente: {}", usuario.getEmail());
            return ResponseEntity.status(201).body(nuevoUsuario);
            
        } catch (RuntimeException e) {
            logger.warn("Error en registro: {}", e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @Operation(summary = "Validar token", description = "Valida si un token JWT es válido")
    @PostMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestBody TokenRequest tokenRequest) {
        try {
            boolean isValid = jwtTokenProvider.validateToken(tokenRequest.getToken());
            
            Map<String, Object> response = new HashMap<>();
            response.put("valid", isValid);
            
            if (isValid) {
                String email = jwtTokenProvider.getEmailFromToken(tokenRequest.getToken());
                response.put("email", email);
                
                // Obtener información del usuario
                usuarioService.findByEmail(email).ifPresent(usuario -> {
                    response.put("usuario", Map.of(
                        "id", usuario.getId(),
                        "nombre", usuario.getNombre(),
                        "email", usuario.getEmail(),
                        "rol", usuario.getRol().getCodigo()
                    ));
                });
            }
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error validando token: ", e);
            Map<String, Object> response = new HashMap<>();
            response.put("valid", false);
            return ResponseEntity.ok(response);
        }
    }

    @Operation(summary = "Renovar token", description = "Renueva un token JWT válido")
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody TokenRequest tokenRequest) {
        try {
            if (jwtTokenProvider.validateToken(tokenRequest.getToken())) {
                String email = jwtTokenProvider.getEmailFromToken(tokenRequest.getToken());
                String newToken = jwtTokenProvider.generateToken(email);
                
                Map<String, Object> response = new HashMap<>();
                response.put("token", newToken);
                response.put("tipo", "Bearer");
                
                return ResponseEntity.ok(response);
            } else {
                Map<String, String> error = new HashMap<>();
                error.put("mensaje", "Token inválido");
                return ResponseEntity.badRequest().body(error);
            }
        } catch (Exception e) {
            logger.error("Error renovando token: ", e);
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", "Error interno del servidor");
            return ResponseEntity.internalServerError().body(error);
        }
    }

    @Operation(summary = "Cerrar sesión", description = "Cierra la sesión del usuario")
    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        SecurityContextHolder.clearContext();
        Map<String, String> response = new HashMap<>();
        response.put("mensaje", "Sesión cerrada exitosamente");
        return ResponseEntity.ok(response);
    }

    // Clases internas para las solicitudes
    public static class LoginRequest {
        private String email;
        private String password;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    public static class TokenRequest {
        private String token;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
}