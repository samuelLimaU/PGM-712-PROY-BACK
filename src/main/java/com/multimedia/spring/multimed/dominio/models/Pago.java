package com.multimedia.spring.multimed.dominio.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Pago {
    private Long id;
    private Long pedidoId;
    private MetodoPago metodo;
    private EstadoPago estado;
    private BigDecimal monto;
    private LocalDateTime fecha;
    private LocalDateTime fechaPago;
    private String notas;

    public Pago() {}

    public Pago(Long id, Long pedidoId, MetodoPago metodo, EstadoPago estado, BigDecimal monto, LocalDateTime fecha, LocalDateTime fechaPago, String notas) {
        this.id = id;
        this.pedidoId = pedidoId;
        this.metodo = metodo;
        this.estado = estado;
        this.monto = monto;
        this.fecha = fecha;
        this.fechaPago = fechaPago;
        this.notas = notas;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getPedidoId() { return pedidoId; }
    public void setPedidoId(Long pedidoId) { this.pedidoId = pedidoId; }

    public MetodoPago getMetodo() { return metodo; }
    public void setMetodo(MetodoPago metodo) { this.metodo = metodo; }

    public EstadoPago getEstado() { return estado; }
    public void setEstado(EstadoPago estado) { this.estado = estado; }

    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

    public LocalDateTime getFechaPago() { return fechaPago; }
    public void setFechaPago(LocalDateTime fechaPago) { this.fechaPago = fechaPago; }

    public String getNotas() { return notas; }
    public void setNotas(String notas) { this.notas = notas; }
}
