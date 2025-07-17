package com.tienda.config;

import com.tienda.security.AuthTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import com.tienda.service.UsuarioService;
import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    @org.springframework.context.annotation.Lazy
    private UsuarioService usuarioService;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(usuarioService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                        
                        // Super Admin - acceso total
                        .requestMatchers("/api/**").hasRole("SUPER_ADMIN")
                        
                        // Admin - acceso casi total excepto configuración crítica
                        .requestMatchers("/api/productos/**").hasAnyRole("SUPER_ADMIN", "ADMIN", "MANAGER", "VENDEDOR", "INVENTARIO")
                        .requestMatchers("/api/categorias/**").hasAnyRole("SUPER_ADMIN", "ADMIN", "MANAGER", "INVENTARIO")
                        .requestMatchers("/api/clientes/**").hasAnyRole("SUPER_ADMIN", "ADMIN", "MANAGER", "VENDEDOR", "CAJERO")
                        .requestMatchers("/api/ventas/**").hasAnyRole("SUPER_ADMIN", "ADMIN", "MANAGER", "VENDEDOR", "CAJERO")
                        .requestMatchers("/api/reportes/**").hasAnyRole("SUPER_ADMIN", "ADMIN", "MANAGER", "INVENTARIO")
                        .requestMatchers("/api/usuarios/**").hasAnyRole("SUPER_ADMIN", "ADMIN")
                        .requestMatchers("/api/configuracion/**").hasAnyRole("SUPER_ADMIN", "ADMIN")
                        .requestMatchers("/api/dashboard/**").hasAnyRole("SUPER_ADMIN", "ADMIN", "MANAGER", "VENDEDOR", "CAJERO", "INVENTARIO")
                        .requestMatchers("/api/analytics/**").hasAnyRole("SUPER_ADMIN", "ADMIN", "MANAGER")
                        .requestMatchers("/api/impuestos/**").hasAnyRole("SUPER_ADMIN", "ADMIN", "MANAGER")
                        
                        .anyRequest().authenticated());

        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(
                Arrays.asList("http://localhost:3000", "http://localhost:5173", "http://127.0.0.1:5500"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}