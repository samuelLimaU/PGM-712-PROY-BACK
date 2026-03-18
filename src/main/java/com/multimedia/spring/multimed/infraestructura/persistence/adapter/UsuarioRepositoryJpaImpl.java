package com.multimedia.spring.multimed.infraestructura.persistence.adapter;

import com.multimedia.spring.multimed.dominio.models.Usuario;
import com.multimedia.spring.multimed.dominio.repository.UsuarioRepository;
import com.multimedia.spring.multimed.infraestructura.jpa.EntityUsuario;
import com.multimedia.spring.multimed.infraestructura.jpa.SpringDataUsuarioRepository;

import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class UsuarioRepositoryJpaImpl implements UsuarioRepository {

    private final SpringDataUsuarioRepository jpa;

    public UsuarioRepositoryJpaImpl(SpringDataUsuarioRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public Usuario guardar(Usuario entity) {
        return toDomain(jpa.save(toEntity(entity)));
    }

    @Override
    public List<Usuario> listar() {
        return jpa.findAll().stream().map(this::toDomain).toList();
    }

    @Override
    public Optional<Usuario> buscarPorId(Long id) {
        return jpa.findById(id).map(this::toDomain);
    }

    @Override
    public void eliminar(Long id) {
        jpa.deleteById(id);
    }

    @Override
    public Optional<Usuario> buscarPorEmail(String email) {
        return jpa.findByEmail(email).map(this::toDomain);
    }

    private Usuario toDomain(EntityUsuario e) {
        return new Usuario(
                e.getId(),
                e.getNombre(),
                e.getNombre2(),
                e.getApellido(),
                e.getApellido2(),
                e.getEmail(),
                e.getPassword(),
                e.getTelefono(),
                e.getActivo(),
                e.getCreatedAt()
        );
    }

    private EntityUsuario toEntity(Usuario u) {
        EntityUsuario e = new EntityUsuario();
        e.setId(u.getId());
        e.setNombre(u.getNombre());
        e.setNombre2(u.getNombre2());
        e.setApellido(u.getApellido());
        e.setApellido2(u.getApellido2());
        e.setEmail(u.getEmail());
        e.setPassword(u.getPassword());
        e.setTelefono(u.getTelefono());
        e.setActivo(u.getActivo());
        e.setCreatedAt(u.getCreatedAt());
        return e;
    }
}