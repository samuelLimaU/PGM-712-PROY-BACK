package com.multimedia.spring.multimed.infraestructura.persistence.adapter;

import com.multimedia.spring.multimed.dominio.models.UsuarioRol;
import com.multimedia.spring.multimed.dominio.repository.UsuarioRolRepository;
import com.multimedia.spring.multimed.infraestructura.jpa.EntityUsuarioRol;
import com.multimedia.spring.multimed.infraestructura.jpa.SpringDataUsuarioRolRepository;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UsuarioRolRepositoryImpl implements UsuarioRolRepository {

    private final SpringDataUsuarioRolRepository jpaRepository;

    public UsuarioRolRepositoryImpl(SpringDataUsuarioRolRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public UsuarioRol guardar(UsuarioRol entity) {
        EntityUsuarioRol saved = jpaRepository.save(toEntity(entity));
        return toDomain(saved);
    }

    @Override
    public List<UsuarioRol> listar() {
        return jpaRepository.findAll()
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public Optional<UsuarioRol> buscarPorId(Long id) {
        return jpaRepository.findById(id)
                .map(this::toDomain);
    }

    @Override
    public void eliminar(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public Optional<UsuarioRol> buscarPorUsuarioIdYRolId(Long usuarioId, Long rolId) {
        return jpaRepository.findByUsuarioIdAndRolId(usuarioId, rolId)
                .map(this::toDomain);
    }

    @Override
    public List<UsuarioRol> listarPorUsuarioId(Long usuarioId) {
        return jpaRepository.findByUsuarioId(usuarioId)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    // 🔁 MAPPERS

    private UsuarioRol toDomain(EntityUsuarioRol entity) {
        return new UsuarioRol(
                entity.getId(),
                entity.getUsuarioId(),
                entity.getRolId()
        );
    }

    private EntityUsuarioRol toEntity(UsuarioRol domain) {
        return new EntityUsuarioRol(
                domain.getId(),
                domain.getUsuarioId(),
                domain.getRolId()
        );
    }
}