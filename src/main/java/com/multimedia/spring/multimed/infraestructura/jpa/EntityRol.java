package com.multimedia.spring.multimed.infraestructura.jpa;

import jakarta.persistence.*;

@Entity
@Table(name = "roles")
public class EntityRol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", unique = true, nullable = false, length = 50)
    private String nombre;

    public EntityRol() {
    }

    public EntityRol(Long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}