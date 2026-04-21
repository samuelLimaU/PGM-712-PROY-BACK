package com.multimedia.spring.multimed.infraestructura.jpa;

import jakarta.persistence.*;

@Entity
@Table(name = "cliente_info")
public class EntityClienteInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private EntityUsuario usuario;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(nullable = false, length = 20)
    private String telefono;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String direccion;

    @Column(length = 100)
    private String ciudad;

    @Column(columnDefinition = "TEXT")
    private String referencia;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public EntityUsuario getUsuario() { return usuario; }
    public void setUsuario(EntityUsuario usuario) { this.usuario = usuario; }

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
