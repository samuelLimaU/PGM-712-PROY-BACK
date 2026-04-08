package com.multimedia.spring.multimed.dominio.repository;

import com.multimedia.spring.multimed.dominio.models.Producto;

import java.util.List;
import java.util.Optional;

public interface ProductoRepository {

    Producto guardar(Producto producto);

    List<Producto> listar();

    Optional<Producto> buscarPorId(Long id);

    void eliminar(Long id);

    List<Producto> listarActivos();

    boolean existePorNombre(String nombre);
}
