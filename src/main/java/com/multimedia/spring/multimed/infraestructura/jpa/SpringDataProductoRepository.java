package com.multimedia.spring.multimed.infraestructura.jpa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SpringDataProductoRepository extends JpaRepository<EntityProducto, Long> {

    Optional<EntityProducto> findByNombreIgnoreCase(String nombre);

    boolean existsByNombreIgnoreCase(String nombre);

    List<EntityProducto> findByActivoTrue(Sort sort);

    Page<EntityProducto> findByActivoTrue(Pageable pageable);

    long countByActivoTrue();
}
