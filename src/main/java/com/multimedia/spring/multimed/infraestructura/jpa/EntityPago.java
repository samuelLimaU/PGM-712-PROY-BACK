package com.multimedia.spring.multimed.infraestructura.jpa;

import com.multimedia.spring.multimed.dominio.models.EstadoPago;
import com.multimedia.spring.multimed.dominio.models.MetodoPago;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pagos")
public class EntityPago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id", nullable = false, unique = true)
    private EntityPedido pedido;

    @Enumerated(EnumType.STRING)
    @Column
    private MetodoPago metodo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoPago estado;

    @Column(precision = 10, scale = 2)
    private BigDecimal monto;

    @Column(nullable = false, updatable = false)
    private LocalDateTime fecha;

    @Column(name = "fecha_pago")
    private LocalDateTime fechaPago;

    @Column(columnDefinition = "TEXT")
    private String notas;

    @PrePersist
    protected void onCreate() {
        if (fecha == null) {
            fecha = LocalDateTime.now();
        }
        if (estado == null) {
            estado = EstadoPago.PENDIENTE;
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public EntityPedido getPedido() { return pedido; }
    public void setPedido(EntityPedido pedido) { this.pedido = pedido; }

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
