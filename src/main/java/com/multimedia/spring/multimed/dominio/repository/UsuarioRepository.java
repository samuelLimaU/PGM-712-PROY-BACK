package com.multimedia.spring.multimed.dominio.repository;

import com.multimedia.spring.multimed.dominio.models.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository {

    Usuario guardar(Usuario usuario);

    List<Usuario> listar();

    Optional<Usuario> buscarPorId(Long id);

    void eliminar(Long id);

    Optional<Usuario> buscarPorEmail(String email);
}