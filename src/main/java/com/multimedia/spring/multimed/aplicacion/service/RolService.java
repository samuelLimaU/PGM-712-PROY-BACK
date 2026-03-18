package com.multimedia.spring.multimed.aplicacion.service;

import com.multimedia.spring.multimed.aplicacion.dto.RolRequestDTO;
import com.multimedia.spring.multimed.aplicacion.dto.RolResponseDTO;
import com.multimedia.spring.multimed.dominio.models.Rol;
import com.multimedia.spring.multimed.dominio.repository.RolRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RolService {

    private final RolRepository rolRepository;

    public RolService(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    public RolResponseDTO crear(RolRequestDTO dto) {
        if (dto.nombre == null || dto.nombre.isEmpty()) {
            throw new IllegalArgumentException("El nombre del rol es obligatorio");
        }

        rolRepository.buscarPorNombre(dto.nombre)
                .ifPresent(r -> {
                    throw new IllegalArgumentException("El rol ya existe");
                });

        Rol rol = new Rol();
        rol.setNombre(dto.nombre);

        Rol guardado = rolRepository.guardar(rol);

        RolResponseDTO response = new RolResponseDTO();
        response.id = guardado.getId();
        response.nombre = guardado.getNombre();

        return response;
    }

    public List<RolResponseDTO> listar() {
        return rolRepository.listar().stream().map(r -> {
            RolResponseDTO dto = new RolResponseDTO();
            dto.id = r.getId();
            dto.nombre = r.getNombre();
            return dto;
        }).collect(Collectors.toList());
    }

    public RolResponseDTO obtenerPorId(Long id) {
        Rol rol = rolRepository.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Rol no encontrado"));

        RolResponseDTO dto = new RolResponseDTO();
        dto.id = rol.getId();
        dto.nombre = rol.getNombre();

        return dto;
    }

    public void eliminar(Long id) {
        Rol rol = rolRepository.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Rol no encontrado"));

        rolRepository.eliminar(rol.getId());
    }

    public RolResponseDTO actualizar(Long id, RolRequestDTO dto) {

        // Verificar que exista
        Rol rol = rolRepository.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Rol no encontrado"));

        // Validar campo obligatorio
        if (dto.nombre == null || dto.nombre.isEmpty()) {
            throw new IllegalArgumentException("El nombre del rol es obligatorio");
        }

        // Validar duplicado (pero permitir el mismo nombre si no cambia)
        rolRepository.buscarPorNombre(dto.nombre)
                .ifPresent(r -> {
                    if (!r.getId().equals(id)) {
                        throw new IllegalArgumentException("El rol ya existe");
                    }
                });

        // Actualizar datos
        rol.setNombre(dto.nombre);

        Rol actualizado = rolRepository.guardar(rol);

        // Mapear respuesta
        RolResponseDTO response = new RolResponseDTO();
        response.id = actualizado.getId();
        response.nombre = actualizado.getNombre();

        return response;
    }
}