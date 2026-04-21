package com.multimedia.spring.multimed.infraestructura.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SpringDataPromocionRepository extends JpaRepository<EntityPromocion, Long> {

    // Listar solo las activas
    List<EntityPromocion> findByActivoTrue();

    // Buscar por tipo
    List<EntityPromocion> findByTipo(String tipo);

    // Buscar activas por tipo
    List<EntityPromocion> findByActivoTrueAndTipo(String tipo);

    // Promociones que contienen un producto específico
    @Query("SELECT p FROM EntityPromocion p WHERE p.id IN " +
           "(SELECT ep.promocionId FROM EntityPromocionProducto ep WHERE ep.productoId = :productoId)")
    List<EntityPromocion> findByProductoId(Long productoId);
}
