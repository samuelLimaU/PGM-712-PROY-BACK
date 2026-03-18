package com.multimedia.spring.multimed.infraestructura.jpa;

import jakarta.persistence.*;

@Entity
@Table(name = "usuario_roles",
       uniqueConstraints = {
           @UniqueConstraint(columnNames = {"usuario_id", "rol_id"})
       })
public class EntityUsuarioRol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usu_rol_id")
    private Long id;

    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    @Column(name = "rol_id", nullable = false)
    private Long rolId;

    public EntityUsuarioRol() {}

    public EntityUsuarioRol(Long id, Long usuarioId, Long rolId) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.rolId = rolId;
    }

    public Long getId() { return id; }
    public Long getUsuarioId() { return usuarioId; }
    public Long getRolId() { return rolId; }

    public void setId(Long id) { this.id = id; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
    public void setRolId(Long rolId) { this.rolId = rolId; }
}