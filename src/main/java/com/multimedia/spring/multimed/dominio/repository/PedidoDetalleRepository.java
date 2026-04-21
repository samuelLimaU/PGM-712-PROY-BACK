package com.multimedia.spring.multimed.dominio.repository;

import com.multimedia.spring.multimed.dominio.models.PedidoDetalle;
import java.util.List;

public interface PedidoDetalleRepository {
    PedidoDetalle save(PedidoDetalle detalle);
    List<PedidoDetalle> findByPedidoId(Long pedidoId);
}
