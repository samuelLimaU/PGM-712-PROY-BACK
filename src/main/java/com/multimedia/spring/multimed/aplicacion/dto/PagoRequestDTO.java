package com.multimedia.spring.multimed.aplicacion.dto;

import com.multimedia.spring.multimed.dominio.models.MetodoPago;

public class PagoRequestDTO {
    private MetodoPago metodo;
    private String notas;

    public MetodoPago getMetodo() { return metodo; }
    public void setMetodo(MetodoPago metodo) { this.metodo = metodo; }

    public String getNotas() { return notas; }
    public void setNotas(String notas) { this.notas = notas; }
}
