package com.multimedia.spring.multimed.dominio.repository;

import com.multimedia.spring.multimed.dominio.models.Pago;
import java.util.Optional;

public interface PagoRepository {
    Pago save(Pago pago);
    Optional<Pago> findByPedidoId(Long pedidoId);
}
