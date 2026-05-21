package com.multimedia.spring.multimed.dominio.repository;

import com.multimedia.spring.multimed.dominio.models.Producto;

import java.util.List;
import java.util.Optional;

public interface ProductoRepository {

    Producto guardar(Producto producto);

    List<Producto> listar();

    List<Producto> listarPaginado(int page, int size);

    Optional<Producto> buscarPorId(Long id);

    void eliminar(Long id);

    List<Producto> listarActivos();

    List<Producto> listarActivosPaginados(int page, int size);

    long contarTotal();

    long contarActivos();

    boolean existePorNombre(String nombre);
}
