package com.multimedia.spring.multimed.aplicacion.service;

import com.multimedia.spring.multimed.aplicacion.dto.PromocionRequestDTO;
import com.multimedia.spring.multimed.aplicacion.dto.PromocionResponseDTO;
import com.multimedia.spring.multimed.aplicacion.dto.ProductoResponseDTO;
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
    //  Lógica de Aplicación al Catálogo
    // ──────────────────────────────────────────────
    public List<ProductoResponseDTO> aplicarPromociones(List<ProductoResponseDTO> productos) {
        // 1. Obtener promociones activas y vigentes hoy
        java.time.LocalDate hoy = java.time.LocalDate.now();
        List<PromocionResponseDTO> promocionesVigentes = listarActivas().stream()
                .filter(p -> (p.fechaInicio == null || !p.fechaInicio.isAfter(hoy)) &&
                             (p.fechaFin == null || !p.fechaFin.isBefore(hoy)))
                .collect(Collectors.toList());

        if (promocionesVigentes.isEmpty()) return productos;

        // 2. Para cada producto, buscar si tiene una promoción aplicada
        for (ProductoResponseDTO producto : productos) {
            // Buscamos la primera promoción que contenga este producto o sea global (sin IDs específicos)
            promocionesVigentes.stream()
                .filter(promo -> (promo.productoIds == null || promo.productoIds.isEmpty()) || promo.productoIds.contains(producto.id))
                .findFirst()
                .ifPresent(promo -> {
                    producto.promocionActiva = true;
                    producto.tipoPromocion = promo.tipo;
                    producto.tituloPromocion = promo.titulo;

                    if ("DESCUENTO_PORCENTAJE".equals(promo.tipo) && promo.valor != null) {
                        // precio - (precio * (valor/100))
                        java.math.BigDecimal descuento = producto.precio
                            .multiply(promo.valor)
                            .divide(new java.math.BigDecimal("100"), 2, java.math.RoundingMode.HALF_UP);
                        producto.precioOferta = producto.precio.subtract(descuento);
                    } 
                    else if ("DESCUENTO_FIJO".equals(promo.tipo) && promo.valor != null) {
                        // precio - valor
                        producto.precioOferta = producto.precio.subtract(promo.valor);
                    }
                    
                    // Asegurarse de que el precio de oferta no sea negativo
                    if (producto.precioOferta != null && producto.precioOferta.compareTo(java.math.BigDecimal.ZERO) < 0) {
                        producto.precioOferta = java.math.BigDecimal.ZERO;
                    }
                });
        }

        return productos;
    }

    public java.math.BigDecimal calcularPrecioVenta(com.multimedia.spring.multimed.dominio.models.Producto producto) {
        java.time.LocalDate hoy = java.time.LocalDate.now();
        List<Promocion> promocionesVigentes = promocionRepository.listarActivas().stream()
                .filter(p -> (p.getFechaInicio() == null || !p.getFechaInicio().isAfter(hoy)) &&
                             (p.getFechaFin() == null || !p.getFechaFin().isBefore(hoy)))
                .collect(Collectors.toList());

        if (promocionesVigentes.isEmpty()) return producto.getPrecio();

        for (Promocion promo : promocionesVigentes) {
            // Aplicar si es global (sin IDs) o si contiene el ID del producto
            if ((promo.getProductoIds() == null || promo.getProductoIds().isEmpty()) || promo.getProductoIds().contains(producto.getId())) {
                if ("DESCUENTO_PORCENTAJE".equals(promo.getTipo()) && promo.getValor() != null) {
                    java.math.BigDecimal descuento = producto.getPrecio()
                        .multiply(promo.getValor())
                        .divide(new java.math.BigDecimal("100"), 2, java.math.RoundingMode.HALF_UP);
                    java.math.BigDecimal precioOferta = producto.getPrecio().subtract(descuento);
                    return precioOferta.compareTo(java.math.BigDecimal.ZERO) < 0 ? java.math.BigDecimal.ZERO : precioOferta;
                } 
                else if ("DESCUENTO_FIJO".equals(promo.getTipo()) && promo.getValor() != null) {
                    java.math.BigDecimal precioOferta = producto.getPrecio().subtract(promo.getValor());
                    return precioOferta.compareTo(java.math.BigDecimal.ZERO) < 0 ? java.math.BigDecimal.ZERO : precioOferta;
                }
            }
        }

        return producto.getPrecio();
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
