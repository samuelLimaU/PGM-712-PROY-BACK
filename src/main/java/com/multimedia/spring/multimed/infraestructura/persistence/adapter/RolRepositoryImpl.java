package com.multimedia.spring.multimed.infraestructura.persistence.adapter;

import com.multimedia.spring.multimed.dominio.models.Rol;
import com.multimedia.spring.multimed.dominio.repository.RolRepository;
import com.multimedia.spring.multimed.infraestructura.jpa.EntityRol;
import com.multimedia.spring.multimed.infraestructura.jpa.SpringDataRolRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class RolRepositoryImpl implements RolRepository {

    private final SpringDataRolRepository jpaRepository;

    public RolRepositoryImpl(SpringDataRolRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    // Convertir Entity a Dominio
    private Rol toDomain(EntityRol entity) {
        return new Rol(entity.getId(), entity.getNombre());
    }

    // Convertir Dominio a Entity
    private EntityRol toEntity(Rol rol) {
        return new EntityRol(rol.getId(), rol.getNombre());
    }

    @Override
    public Rol guardar(Rol rol) {
        EntityRol entity = toEntity(rol);
        EntityRol guardado = jpaRepository.save(entity);
        return toDomain(guardado);
    }

    @Override
    public List<Rol> listar() {
        return jpaRepository.findAll()
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Rol> buscarPorId(Long id) {
        return jpaRepository.findById(id)
                .map(this::toDomain);
    }

    @Override
    public void eliminar(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public Optional<Rol> buscarPorNombre(String nombre) {
        return jpaRepository.findByNombre(nombre)
                .map(this::toDomain);
    }
}