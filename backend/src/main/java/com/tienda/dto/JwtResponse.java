package com.tienda.dto;

import com.tienda.entity.Usuario;

public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String nombre;
    private String email;
    private Usuario.Rol rol;
    
    // Constructors
    public JwtResponse() {}
    
    public JwtResponse(String token, Long id, String nombre, String email, Usuario.Rol rol) {
        this.token = token;
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.rol = rol;
    }
    
    // Getters and Setters
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public Usuario.Rol getRol() { return rol; }
    public void setRol(Usuario.Rol rol) { this.rol = rol; }
}