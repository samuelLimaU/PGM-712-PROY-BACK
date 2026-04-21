package com.multimedia.spring.multimed.infraestructura.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataPagoRepository extends JpaRepository<EntityPago, Long> {
}
