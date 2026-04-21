package com.multimedia.spring.multimed.aplicacion.dto;

import com.multimedia.spring.multimed.dominio.models.EstadoPedido;

public class EstadoPedidoRequestDTO {
    private EstadoPedido estado;

    public EstadoPedido getEstado() { return estado; }
    public void setEstado(EstadoPedido estado) { this.estado = estado; }
}
