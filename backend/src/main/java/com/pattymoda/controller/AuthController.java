package com.pattymoda.controller;

import com.pattymoda.entity.Usuario;
import com.pattymoda.security.JwtTokenProvider;
import com.pattymoda.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
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

    @Operation(summary = "Iniciar sesión")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtTokenProvider.generateToken(authentication);

            // Obtener información del usuario
            Usuario usuario = usuarioService.findByEmail(loginRequest.getEmail())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            // Actualizar último acceso
            usuarioService.actualizarUltimoAcceso(usuario.getId());

            Map<String, Object> response = new HashMap<>();
            response.put("token", jwt);
            response.put("usuario", usuario);
            response.put("tipo", "Bearer");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // Incrementar intentos fallidos
            usuarioService.findByEmail(loginRequest.getEmail())
                    .ifPresent(usuario -> usuarioService.incrementarIntentosFallidos(usuario.getId()));

            Map<String, String> error = new HashMap<>();
            error.put("mensaje", "Credenciales inválidas");
            return ResponseEntity.badRequest().body(error);
        }
    }

    @Operation(summary = "Registrar nuevo usuario")
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Usuario usuario) {
        try {
            Usuario nuevoUsuario = usuarioService.crearUsuario(usuario);
            return ResponseEntity.status(201).body(nuevoUsuario);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @Operation(summary = "Validar token")
    @PostMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestBody TokenRequest tokenRequest) {
        boolean isValid = jwtTokenProvider.validateToken(tokenRequest.getToken());
        
        Map<String, Object> response = new HashMap<>();
        response.put("valid", isValid);
        
        if (isValid) {
            String email = jwtTokenProvider.getEmailFromToken(tokenRequest.getToken());
            response.put("email", email);
        }
        
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Renovar token")
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody TokenRequest tokenRequest) {
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