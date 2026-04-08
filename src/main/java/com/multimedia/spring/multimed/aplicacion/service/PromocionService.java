package com.multimedia.spring.multimed.aplicacion.service;

import com.multimedia.spring.multimed.aplicacion.dto.PromocionRequestDTO;
import com.multimedia.spring.multimed.aplicacion.dto.PromocionResponseDTO;
import com.multimedia.spring.multimed.dominio.models.Promocion;
import com.multimedia.spring.multimed.dominio.repository.PromocionRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PromocionService {

    private static final List<String> TIPOS_VALIDOS = Arrays.asList(
            "BANNER", "DESCUENTO_PORCENTAJE", "DESCUENTO_FIJO"
    );

    private final PromocionRepository promocionRepository;

    public PromocionService(PromocionRepository promocionRepository) {
        this.promocionRepository = promocionRepository;
    }

    // ──────────────────────────────────────────────
    //  Crear
    // ──────────────────────────────────────────────
    public PromocionResponseDTO crear(PromocionRequestDTO dto) {
        validarRequest(dto);

        Promocion promocion = mapearDtoADominio(dto);
        promocion.setActivo(dto.activo != null ? dto.activo : true);

        Promocion guardada = promocionRepository.guardar(promocion);
        return mapearDominioADto(guardada);
    }

    // ──────────────────────────────────────────────
    //  Listar todas
    // ──────────────────────────────────────────────
    public List<PromocionResponseDTO> listar() {
        return promocionRepository.listar()
                .stream()
                .map(this::mapearDominioADto)
                .collect(Collectors.toList());
    }

    // ──────────────────────────────────────────────
    //  Listar solo activas
    // ──────────────────────────────────────────────
    public List<PromocionResponseDTO> listarActivas() {
        return promocionRepository.listarActivas()
                .stream()
                .map(this::mapearDominioADto)
                .collect(Collectors.toList());
    }

    // ──────────────────────────────────────────────
    //  Buscar por ID
    // ──────────────────────────────────────────────
    public PromocionResponseDTO buscarPorId(Long id) {
        Promocion promocion = promocionRepository.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Promoción no encontrada con id: " + id));
        return mapearDominioADto(promocion);
    }

    // ──────────────────────────────────────────────
    //  Actualizar
    // ──────────────────────────────────────────────
    public PromocionResponseDTO actualizar(Long id, PromocionRequestDTO dto) {
        Promocion existente = promocionRepository.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Promoción no encontrada con id: " + id));

        validarRequest(dto);

        existente.setTitulo(dto.titulo);
        existente.setDescripcion(dto.descripcion);
        existente.setImagenUrl(dto.imagenUrl);
        existente.setTipo(dto.tipo);
        existente.setValor(dto.valor);
        existente.setFechaInicio(dto.fechaInicio);
        existente.setFechaFin(dto.fechaFin);
        if (dto.activo != null) existente.setActivo(dto.activo);
        existente.setProductoIds(dto.productoIds);

        Promocion actualizada = promocionRepository.guardar(existente);
        return mapearDominioADto(actualizada);
    }

    // ──────────────────────────────────────────────
    //  Eliminar
    // ──────────────────────────────────────────────
    public void eliminar(Long id) {
        promocionRepository.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Promoción no encontrada con id: " + id));
        promocionRepository.eliminar(id);
    }

    // ──────────────────────────────────────────────
    //  Validaciones
    // ──────────────────────────────────────────────
    private void validarRequest(PromocionRequestDTO dto) {
        if (dto.titulo == null || dto.titulo.isBlank()) {
            throw new IllegalArgumentException("El título es obligatorio.");
        }
        if (dto.titulo.length() > 150) {
            throw new IllegalArgumentException("El título no puede superar 150 caracteres.");
        }
        if (dto.tipo != null && !TIPOS_VALIDOS.contains(dto.tipo)) {
            throw new IllegalArgumentException(
                    "Tipo inválido. Valores permitidos: " + TIPOS_VALIDOS);
        }
        // Si el tipo exige un valor numérico, validamos que venga
        if (dto.tipo != null &&
                (dto.tipo.equals("DESCUENTO_PORCENTAJE") || dto.tipo.equals("DESCUENTO_FIJO"))
                && dto.valor == null) {
            throw new IllegalArgumentException(
                    "El campo 'valor' es obligatorio para tipo " + dto.tipo);
        }
        // Validar rango de fechas
        if (dto.fechaInicio != null && dto.fechaFin != null &&
                dto.fechaFin.isBefore(dto.fechaInicio)) {
            throw new IllegalArgumentException(
                    "La fecha de fin no puede ser anterior a la fecha de inicio.");
        }
    }

    // ──────────────────────────────────────────────
    //  Mapeos
    // ──────────────────────────────────────────────
    private Promocion mapearDtoADominio(PromocionRequestDTO dto) {
        Promocion p = new Promocion();
        p.setTitulo(dto.titulo);
        p.setDescripcion(dto.descripcion);
        p.setImagenUrl(dto.imagenUrl);
        p.setTipo(dto.tipo != null ? dto.tipo : "BANNER");
        p.setValor(dto.valor);
        p.setFechaInicio(dto.fechaInicio);
        p.setFechaFin(dto.fechaFin);
        p.setActivo(dto.activo != null ? dto.activo : true);
        p.setProductoIds(dto.productoIds);
        return p;
    }

    private PromocionResponseDTO mapearDominioADto(Promocion p) {
        PromocionResponseDTO dto = new PromocionResponseDTO();
        dto.id = p.getId();
        dto.titulo = p.getTitulo();
        dto.descripcion = p.getDescripcion();
        dto.imagenUrl = p.getImagenUrl();
        dto.tipo = p.getTipo();
        dto.valor = p.getValor();
        dto.fechaInicio = p.getFechaInicio();
        dto.fechaFin = p.getFechaFin();
        dto.activo = p.getActivo();
        dto.productoIds = p.getProductoIds();
        return dto;
    }
}
