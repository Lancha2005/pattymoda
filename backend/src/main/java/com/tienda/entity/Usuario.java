package com.tienda.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "usuarios")
public class Usuario implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 100)
    private String nombre;
    
    @Column(nullable = false, unique = true, length = 100)
    private String email;
    
    @Column(nullable = false)
    private String password;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Rol rol;
    
    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;
    
    @Column(name = "activo")
    private Boolean activo = true;
    
    @Column(name = "intentos_recuperacion")
    private Integer intentosRecuperacion = 0;
    
    @Column(name = "ultimo_intento_recuperacion")
    private LocalDateTime ultimoIntentoRecuperacion;
    
    @Column(name = "foto_perfil")
    private String fotoPerfil;
    
    // Constructors
    public Usuario() {
        this.fechaCreacion = LocalDateTime.now();
    }
    
    public Usuario(String nombre, String email, String password, Rol rol) {
        this();
        this.nombre = nombre;
        this.email = email;
        this.password = password;
        this.rol = rol;
    }
    
    // UserDetails methods
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + rol.name()));
    }
    
    @Override
    public String getUsername() {
        return email;
    }
    
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    
    @Override
    public boolean isEnabled() {
        return activo;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public Rol getRol() { return rol; }
    public void setRol(Rol rol) { this.rol = rol; }
    
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    
    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }
    
    public Integer getIntentosRecuperacion() { return intentosRecuperacion; }
    public void setIntentosRecuperacion(Integer intentosRecuperacion) { this.intentosRecuperacion = intentosRecuperacion; }
    
    public LocalDateTime getUltimoIntentoRecuperacion() { return ultimoIntentoRecuperacion; }
    public void setUltimoIntentoRecuperacion(LocalDateTime ultimoIntentoRecuperacion) { this.ultimoIntentoRecuperacion = ultimoIntentoRecuperacion; }
    
    public String getFotoPerfil() { return fotoPerfil; }
    public void setFotoPerfil(String fotoPerfil) { this.fotoPerfil = fotoPerfil; }
    
    public enum Rol {
        SUPER_ADMIN("Super Administrador", new String[]{"*"}),
        ADMIN("Administrador", new String[]{"dashboard", "products", "customers", "sales", "categories", "reports", "analytics", "settings", "users"}),
        MANAGER("Gerente", new String[]{"dashboard", "products", "customers", "sales", "categories", "reports", "analytics"}),
        VENDEDOR("Vendedor", new String[]{"dashboard", "products", "customers", "sales", "new-sale"}),
        CAJERO("Cajero", new String[]{"dashboard", "sales", "new-sale", "customers"}),
        INVENTARIO("Encargado de Inventario", new String[]{"dashboard", "products", "categories", "reports"});
        
        private final String displayName;
        private final String[] permissions;
        
        Rol(String displayName, String[] permissions) {
            this.displayName = displayName;
            this.permissions = permissions;
        }
        
        public String getDisplayName() { return displayName; }
        public String[] getPermissions() { return permissions; }
        
        public boolean hasPermission(String permission) {
            if (permissions[0].equals("*")) return true;
            for (String p : permissions) {
                if (p.equals(permission)) return true;
            }
            return false;
        }
    }
}