package com.multimedia.spring.multimed.infraestructura.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

public interface SpringDataPromocionProductoRepository extends JpaRepository<EntityPromocionProducto, Long> {
    List<EntityPromocionProducto> findByPromocionId(Long promocionId);

    List<EntityPromocionProducto> findByProductoId(Long productoId);

    @Modifying
    @Transactional
    void deleteByPromocionId(Long promocionId);
}