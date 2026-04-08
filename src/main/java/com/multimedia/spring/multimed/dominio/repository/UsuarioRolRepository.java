package com.multimedia.spring.multimed.dominio.repository;

import com.multimedia.spring.multimed.dominio.models.UsuarioRol;

import java.util.List;
import java.util.Optional;

public interface UsuarioRolRepository {

    UsuarioRol guardar(UsuarioRol entity);
    List<UsuarioRol> listar();
    Optional<UsuarioRol> buscarPorId(Long id);
    void eliminar(Long id);

    Optional<UsuarioRol> buscarPorUsuarioIdYRolId(Long usuarioId, Long rolId);

    List<UsuarioRol> listarPorUsuarioId(Long usuarioId);
}