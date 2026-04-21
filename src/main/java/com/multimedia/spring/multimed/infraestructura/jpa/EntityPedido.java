package com.multimedia.spring.multimed.infraestructura.jpa;

import com.multimedia.spring.multimed.dominio.models.EstadoPedido;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "pedidos")
public class EntityPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cliente_id", nullable = false)
    private EntityClienteInfo cliente;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<EntityPedidoDetalle> detalles;

    @Column(nullable = false, updatable = false)
    private LocalDateTime fecha;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoPedido estado;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal total;

    @Column(columnDefinition = "TEXT")
    private String notas;

    @PrePersist
    protected void onCreate() {
        if (fecha == null) {
            fecha = LocalDateTime.now();
        }
        if (estado == null) {
            estado = EstadoPedido.PENDIENTE;
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public EntityClienteInfo getCliente() { return cliente; }
    public void setCliente(EntityClienteInfo cliente) { this.cliente = cliente; }

    public List<EntityPedidoDetalle> getDetalles() { return detalles; }
    public void setDetalles(List<EntityPedidoDetalle> detalles) { this.detalles = detalles; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

    public EstadoPedido getEstado() { return estado; }
    public void setEstado(EstadoPedido estado) { this.estado = estado; }

    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }

    public String getNotas() { return notas; }
    public void setNotas(String notas) { this.notas = notas; }
}
