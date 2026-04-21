package com.multimedia.spring.multimed.dominio.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Pedido {
    private Long id;
    private Long clienteId;
    private LocalDateTime fecha;
    private EstadoPedido estado;
    private BigDecimal total;
    private String notas;

    public Pedido() {}

    public Pedido(Long id, Long clienteId, LocalDateTime fecha, EstadoPedido estado, BigDecimal total, String notas) {
        this.id = id;
        this.clienteId = clienteId;
        this.fecha = fecha;
        this.estado = estado;
        this.total = total;
        this.notas = notas;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

    public EstadoPedido getEstado() { return estado; }
    public void setEstado(EstadoPedido estado) { this.estado = estado; }

    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }

    public String getNotas() { return notas; }
    public void setNotas(String notas) { this.notas = notas; }
}
