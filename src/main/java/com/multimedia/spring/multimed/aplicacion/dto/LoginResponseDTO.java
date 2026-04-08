package com.multimedia.spring.multimed.aplicacion.dto;

import java.util.List;

public class LoginResponseDTO {
    private String token;
    private String email;
    private String nombre;
    private List<String> roles;

    public LoginResponseDTO(String token, String email, String nombre, List<String> roles) {
        this.token = token;
        this.email = email;
        this.nombre = nombre;
        this.roles = roles;
    }

    public String getToken() {
        return token;
    }

    public String getEmail() {
        return email;
    }

    public String getNombre() {
        return nombre;
    }

    public List<String> getRoles() {
        return roles;
    }
}
