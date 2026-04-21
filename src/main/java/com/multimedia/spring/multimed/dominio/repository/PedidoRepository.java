package com.multimedia.spring.multimed.dominio.repository;

import com.multimedia.spring.multimed.dominio.models.Pedido;
import java.util.List;
import java.util.Optional;

public interface PedidoRepository {
    Pedido save(Pedido pedido);
    Optional<Pedido> findById(Long id);
    List<Pedido> findAll();
}
