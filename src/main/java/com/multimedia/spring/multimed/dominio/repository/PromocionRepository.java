package com.multimedia.spring.multimed.dominio.repository;

import com.multimedia.spring.multimed.dominio.models.Promocion;

import java.util.List;
import java.util.Optional;

public interface PromocionRepository {

    Promocion guardar(Promocion promocion);

    List<Promocion> listar();

    List<Promocion> listarActivas();

    Optional<Promocion> buscarPorId(Long id);

    void eliminar(Long id);

    void agregarProducto(Long promocionId, Long productoId);

    void removerProducto(Long promocionId, Long productoId);

    List<Long> listarProductoIds(Long promocionId);
}
