package com.multimedia.spring.multimed.infraestructura.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface SpringDataRolRepository extends JpaRepository<EntityRol, Long> {
    Optional<EntityRol> findByNombre(String nombre);
}