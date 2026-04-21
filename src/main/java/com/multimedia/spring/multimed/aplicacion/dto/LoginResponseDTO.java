package com.multimedia.spring.multimed.aplicacion.dto;

import java.util.List;

public class LoginResponseDTO {
    private Long id;
    private String token;
    private String email;
    private String nombre;
    private String apellido;
    private String telefono;
    private List<String> roles;

    public LoginResponseDTO(Long id, String token, String email, String nombre, String apellido, String telefono, List<String> roles) {
        this.id = id;
        this.token = token;
        this.email = email;
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.roles = roles;
    }

    public Long getId() { return id; }
    public String getToken() { return token; }
    public String getEmail() { return email; }
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public String getTelefono() { return telefono; }
    public List<String> getRoles() { return roles; }
}
