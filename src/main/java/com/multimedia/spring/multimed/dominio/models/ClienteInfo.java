package com.multimedia.spring.multimed.dominio.models;

public class ClienteInfo {
    private Long id;
    private Long usuarioId;
    private String nombre;
    private String telefono;
    private String direccion;
    private String ciudad;
    private String referencia;

    public ClienteInfo() {}

    public ClienteInfo(Long id, Long usuarioId, String nombre, String telefono, String direccion, String ciudad, String referencia) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.nombre = nombre;
        this.telefono = telefono;
        this.direccion = direccion;
        this.ciudad = ciudad;
        this.referencia = referencia;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getCiudad() { return ciudad; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }

    public String getReferencia() { return referencia; }
    public void setReferencia(String referencia) { this.referencia = referencia; }
}
