package com.multimedia.spring.multimed.infraestructura.jpa;

import jakarta.persistence.*;

@Entity
@Table(name = "promocion_productos",
       uniqueConstraints = {
           @UniqueConstraint(columnNames = {"promocion_id", "producto_id"})
       })
public class EntityPromocionProducto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "promocion_id", nullable = false)
    private Long promocionId;

    @Column(name = "producto_id", nullable = false)
    private Long productoId;

    public EntityPromocionProducto() {}

    public EntityPromocionProducto(Long promocionId, Long productoId) {
        this.promocionId = promocionId;
        this.productoId = productoId;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getPromocionId() { return promocionId; }
    public void setPromocionId(Long promocionId) { this.promocionId = promocionId; }

    public Long getProductoId() { return productoId; }
    public void setProductoId(Long productoId) { this.productoId = productoId; }
}
