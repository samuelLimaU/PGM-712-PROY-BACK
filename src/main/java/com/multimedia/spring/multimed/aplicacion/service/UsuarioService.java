package com.multimedia.spring.multimed.aplicacion.service;

import com.multimedia.spring.multimed.dominio.models.Usuario;
import com.multimedia.spring.multimed.dominio.repository.UsuarioRepository;
import com.multimedia.spring.multimed.aplicacion.dto.UsuarioRequestDTO;
import com.multimedia.spring.multimed.aplicacion.dto.UsuarioResponseDTO;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    private final UsuarioRepository repository;

    public UsuarioService(UsuarioRepository repository) {
        this.repository = repository;
    }

    private String generarPassword(String apellido, String telefono) {

        if (apellido == null || apellido.isEmpty()) {
            throw new IllegalArgumentException("Apellido es obligatorio para generar password");
        }

        if (telefono == null || telefono.isEmpty()) {
            throw new IllegalArgumentException("Telefono es obligatorio para generar password");
        }

        return apellido.toUpperCase() + telefono;
    }

    // CREATE
    public UsuarioResponseDTO crear(UsuarioRequestDTO dto) {

        validar(dto);

        repository.buscarPorEmail(dto.email).ifPresent(u -> {
            throw new RuntimeException("El email ya existe");
        });

        Usuario usuario = new Usuario(
                null,
                dto.nombre,
                dto.nombre2,
                dto.apellido,
                dto.apellido2,
                dto.email,
                dto.password = generarPassword(dto.apellido, dto.telefono),
                dto.telefono,
                dto.activo != null ? dto.activo : true,
                LocalDateTime.now());

        return mapToResponse(repository.guardar(usuario));
    }

    // GET ALL
    public List<UsuarioResponseDTO> listar() {
        return repository.listar()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // GET BY ID
    public UsuarioResponseDTO obtenerPorId(Long id) {
        Usuario usuario = repository.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return mapToResponse(usuario);
    }

    // UPDATE
    public UsuarioResponseDTO actualizar(Long id, UsuarioRequestDTO dto) {

        validar(dto);

        Usuario existente = repository.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        repository.buscarPorEmail(dto.email).ifPresent(u -> {
            if (!u.getId().equals(id)) {
                throw new RuntimeException("El email ya está en uso");
            }
        });

        existente.setNombre(dto.nombre);
        existente.setNombre2(dto.nombre2);
        existente.setApellido(dto.apellido);
        existente.setApellido2(dto.apellido2);
        existente.setEmail(dto.email);
        existente.setTelefono(dto.telefono);
        existente.setActivo(dto.activo);

        return mapToResponse(repository.guardar(existente));
    }

    // DELETE
    public void eliminar(Long id) {
        repository.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        repository.eliminar(id);
    }

    // VALIDACIONES
    private void validar(UsuarioRequestDTO dto) {

        if (dto.nombre == null || dto.nombre.isEmpty())
            throw new RuntimeException("Nombre es obligatorio");

        if (dto.apellido == null || dto.apellido.isEmpty())
            throw new RuntimeException("Apellido es obligatorio");

        if (dto.email == null || dto.email.isEmpty())
            throw new RuntimeException("Email es obligatorio");
    }

    private UsuarioResponseDTO mapToResponse(Usuario u) {
        UsuarioResponseDTO dto = new UsuarioResponseDTO();
        dto.id = u.getId();
        dto.nombre = u.getNombre();
        dto.nombre2 = u.getNombre2();
        dto.apellido = u.getApellido();
        dto.apellido2 = u.getApellido2();
        dto.email = u.getEmail();
        dto.telefono = u.getTelefono();
        dto.activo = u.getActivo();
        dto.createdAt = u.getCreatedAt();
        return dto;
    }
}