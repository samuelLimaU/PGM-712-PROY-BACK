package com.multimedia.spring.multimed.dominio.models;

public class UsuarioRol {

    private Long id;
    private Long usuarioId;
    private Long rolId;

    public UsuarioRol() {
    }

    public UsuarioRol(Long id, Long usuarioId, Long rolId) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.rolId = rolId;
    }

    public Long getId() {
        return id;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public Long getRolId() {
        return rolId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public void setRolId(Long rolId) {
        this.rolId = rolId;
    }
}