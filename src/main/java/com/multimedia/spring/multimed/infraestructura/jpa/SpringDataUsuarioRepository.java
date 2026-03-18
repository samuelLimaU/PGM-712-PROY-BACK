package com.multimedia.spring.multimed.infraestructura.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SpringDataUsuarioRepository extends JpaRepository<EntityUsuario, Long> {

    Optional<EntityUsuario> findByEmail(String email);
}