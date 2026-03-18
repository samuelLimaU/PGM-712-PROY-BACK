package com.multimedia.spring.multimed.dominio.repository;

import com.multimedia.spring.multimed.dominio.models.Rol;
import java.util.List;
import java.util.Optional;

public interface RolRepository {
    Rol guardar(Rol rol);
    List<Rol> listar();
    Optional<Rol> buscarPorId(Long id);
    void eliminar(Long id);
    Optional<Rol> buscarPorNombre(String nombre);
}