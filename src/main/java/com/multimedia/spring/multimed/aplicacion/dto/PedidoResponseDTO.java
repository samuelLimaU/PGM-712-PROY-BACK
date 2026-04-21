package com.multimedia.spring.multimed.aplicacion.dto;

import com.multimedia.spring.multimed.dominio.models.EstadoPedido;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class PedidoResponseDTO {
    private Long id;
    private String clienteNombre;
    private String clienteTelefono;
    private String direccion;
    private LocalDateTime fecha;
    private EstadoPedido estado;
    private BigDecimal total;
    private String notas;
    private List<DetallePedidoResponseDTO> detalles;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getClienteNombre() { return clienteNombre; }
    public void setClienteNombre(String clienteNombre) { this.clienteNombre = clienteNombre; }

    public String getClienteTelefono() { return clienteTelefono; }
    public void setClienteTelefono(String clienteTelefono) { this.clienteTelefono = clienteTelefono; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

    public EstadoPedido getEstado() { return estado; }
    public void setEstado(EstadoPedido estado) { this.estado = estado; }

    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }

    public String getNotas() { return notas; }
    public void setNotas(String notas) { this.notas = notas; }

    public List<DetallePedidoResponseDTO> getDetalles() { return detalles; }
    public void setDetalles(List<DetallePedidoResponseDTO> detalles) { this.detalles = detalles; }
}
