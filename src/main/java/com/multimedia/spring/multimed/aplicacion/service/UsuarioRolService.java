package com.multimedia.spring.multimed.aplicacion.service;

import com.multimedia.spring.multimed.dominio.models.UsuarioRol;
import com.multimedia.spring.multimed.dominio.repository.UsuarioRolRepository;
import com.multimedia.spring.multimed.aplicacion.dto.UsuarioRolRequestDTO;
import com.multimedia.spring.multimed.aplicacion.dto.UsuarioRolResponseDTO;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioRolService {

    private final UsuarioRolRepository repository;

    public UsuarioRolService(UsuarioRolRepository repository) {
        this.repository = repository;
    }

    // CREATE
    public UsuarioRolResponseDTO crear(UsuarioRolRequestDTO dto) {

        if (dto.usuarioId == null || dto.rolId == null) {
            throw new IllegalArgumentException("usuarioId y rolId son obligatorios");
        }

        // evitar duplicado: si ya existe, lo devolvemos en lugar de fallar
        var existente = repository.buscarPorUsuarioIdYRolId(dto.usuarioId, dto.rolId);
        if (existente.isPresent()) {
            return toResponse(existente.get());
        }

        UsuarioRol entity = new UsuarioRol();
        entity.setUsuarioId(dto.usuarioId);
        entity.setRolId(dto.rolId);

        return toResponse(repository.guardar(entity));
    }

    // GET ALL
    public List<UsuarioRolResponseDTO> listar() {
        return repository.listar().stream()
                .map(this::toResponse)
                .toList();
    }

    // GET BY ID
    public UsuarioRolResponseDTO obtenerPorId(Long id) {
        UsuarioRol entity = repository.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("No encontrado"));

        return toResponse(entity);
    }

    // EXTRA útil en N:M
    public List<UsuarioRolResponseDTO> listarPorUsuario(Long usuarioId) {
        return repository.listarPorUsuarioId(usuarioId).stream()
                .map(this::toResponse)
                .toList();
    }

    // UPDATE (poco común en N:M pero lo dejamos)
    public UsuarioRolResponseDTO actualizar(Long id, UsuarioRolRequestDTO dto) {

        UsuarioRol entity = repository.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("No encontrado"));

        repository.buscarPorUsuarioIdYRolId(dto.usuarioId, dto.rolId)
                .ifPresent(e -> {
                    if (!e.getId().equals(id)) {
                        throw new IllegalArgumentException("Duplicado");
                    }
                });

        entity.setUsuarioId(dto.usuarioId);
        entity.setRolId(dto.rolId);

        return toResponse(repository.guardar(entity));
    }

    // DELETE
    public void eliminar(Long id) {
        UsuarioRol entity = repository.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("No encontrado"));

        repository.eliminar(entity.getId());
    }

    private UsuarioRolResponseDTO toResponse(UsuarioRol entity) {
        UsuarioRolResponseDTO dto = new UsuarioRolResponseDTO();
        dto.id = entity.getId();
        dto.usuarioId = entity.getUsuarioId();
        dto.rolId = entity.getRolId();
        return dto;
    }
}