package com.multimedia.spring.multimed.infraestructura.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SpringDataUsuarioRolRepository 
        extends JpaRepository<EntityUsuarioRol, Long> {

    Optional<EntityUsuarioRol> findByUsuarioIdAndRolId(Long usuarioId, Long rolId);

    List<EntityUsuarioRol> findByUsuarioId(Long usuarioId);
}