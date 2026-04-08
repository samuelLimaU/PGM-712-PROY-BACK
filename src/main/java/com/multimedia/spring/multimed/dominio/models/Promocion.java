package com.multimedia.spring.multimed.dominio.models;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class Promocion {

    private Long id;
    private String titulo;
    private String descripcion;
    private String imagenUrl;
    private String tipo; // BANNER, DESCUENTO_PORCENTAJE, DESCUENTO_FIJO
    private BigDecimal valor;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Boolean activo;
    private List<Long> productoIds; // IDs de productos asociados

    public Promocion() {}

    public Promocion(Long id, String titulo, String descripcion, String imagenUrl,
                     String tipo, BigDecimal valor, LocalDate fechaInicio,
                     LocalDate fechaFin, Boolean activo, List<Long> productoIds) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.imagenUrl = imagenUrl;
        this.tipo = tipo;
        this.valor = valor;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.activo = activo;
        this.productoIds = productoIds;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getImagenUrl() { return imagenUrl; }
    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }

    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }

    public LocalDate getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }

    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }

    public List<Long> getProductoIds() { return productoIds; }
    public void setProductoIds(List<Long> productoIds) { this.productoIds = productoIds; }
}
