package com.multimedia.spring.multimed.dominio.models;

import java.time.LocalDateTime;

public class Usuario {

    private Long id;
    private String nombre;
    private String nombre2;
    private String apellido;
    private String apellido2;
    private String email;
    private String password;
    private String telefono;
    private Boolean activo;
    private LocalDateTime createdAt;

    // Constructor vacío
    public Usuario() {
    }

    // Constructor completo
    public Usuario(Long id, String nombre, String nombre2, String apellido, String apellido2,
                   String email, String password, String telefono, Boolean activo, LocalDateTime createdAt) {
        this.id = id;
        this.nombre = nombre;
        this.nombre2 = nombre2;
        this.apellido = apellido;
        this.apellido2 = apellido2;
        this.email = email;
        this.password = password;
        this.telefono = telefono;
        this.activo = activo;
        this.createdAt = createdAt;
    }

    // Getters y Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre2() {
        return nombre2;
    }

    public void setNombre2(String nombre2) {
        this.nombre2 = nombre2;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getApellido2() {
        return apellido2;
    }

    public void setApellido2(String apellido2) {
        this.apellido2 = apellido2;
    }

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

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}