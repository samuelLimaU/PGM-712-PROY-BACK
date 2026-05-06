package com.multimedia.spring.multimed.aplicacion.service;

import com.multimedia.spring.multimed.aplicacion.dto.ProductoRequestDTO;
import com.multimedia.spring.multimed.aplicacion.dto.ProductoResponseDTO;
import com.multimedia.spring.multimed.dominio.models.Producto;
import com.multimedia.spring.multimed.dominio.repository.ProductoRepository;
import com.multimedia.spring.multimed.infraestructura.jpa.SpringDataPedidoDetalleRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final SpringDataPedidoDetalleRepository pedidoDetalleRepository;

    public ProductoService(ProductoRepository productoRepository, SpringDataPedidoDetalleRepository pedidoDetalleRepository) {
        this.productoRepository = productoRepository;
        this.pedidoDetalleRepository = pedidoDetalleRepository;
    }

    // Crear
    public ProductoResponseDTO crear(ProductoRequestDTO dto, String imagenUrl) {
        validarCamposObligatorios(dto);
        validarNombreUnico(dto.nombre);

        Producto producto = mapearDtoADominio(dto);
        producto.setImagenUrl(imagenUrl);
        producto.setActivo(dto.stock > 0); // ← basado en stock
        producto.setCreatedAt(LocalDateTime.now());

        Producto guardado = productoRepository.guardar(producto);
        return mapearDominioADto(guardado);
    }

    // Listar todos
    public List<ProductoResponseDTO> listar() {
        return productoRepository.listar()
                .stream()
                .map(this::mapearDominioADto)
                .collect(Collectors.toList());
    }

    // Buscar por id
    public ProductoResponseDTO buscarPorId(Long id) {
        Producto producto = productoRepository.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Producto no encontrado con id: " + id));
        return mapearDominioADto(producto);
    }

    // Eliminar
    public void eliminar(Long id) {
        if (pedidoDetalleRepository.existsByProductoId(id)) {
            throw new IllegalArgumentException("No se puede eliminar: El producto ya está registrado en uno o más pedidos.");
        }

        productoRepository.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Producto no encontrado con id: " + id));
        productoRepository.eliminar(id);
    }

    // Editar
    public ProductoResponseDTO actualizar(Long id, ProductoRequestDTO dto, String nuevaImagenUrl) {
        Producto producto = productoRepository.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Producto no encontrado con id: " + id));

        // Si el producto está en un pedido, restringimos solo el precio
        if (pedidoDetalleRepository.existsByProductoId(id)) {
            if (producto.getPrecio().compareTo(dto.precio) != 0) {
                throw new IllegalArgumentException("No se puede cambiar el precio: El producto ya tiene pedidos asociados.");
            }
        }

        producto.setNombre(dto.nombre);
        producto.setDescripcion(dto.descripcion);
        producto.setPrecio(dto.precio);
        producto.setStock(dto.stock);
        producto.setActivo(dto.stock > 0); // ← basado en stock

        if (nuevaImagenUrl != null) {
            producto.setImagenUrl(nuevaImagenUrl);
        }

        Producto guardado = productoRepository.guardar(producto);
        return mapearDominioADto(guardado);
    }

    // Validaciones
    private void validarCamposObligatorios(ProductoRequestDTO dto) {
        if (dto.nombre == null || dto.nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del producto es obligatorio.");
        }
        if (dto.precio == null) {
            throw new IllegalArgumentException("El precio del producto es obligatorio.");
        }
        if (dto.precio.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo.");
        }
        if (dto.stock == null) {
            throw new IllegalArgumentException("El stock del producto es obligatorio.");
        }
        if (dto.stock < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo.");
        }
    }

    private void validarNombreUnico(String nombre) {
        if (productoRepository.existePorNombre(nombre)) {
            throw new IllegalArgumentException(
                    "Ya existe un producto con el nombre: " + nombre);
        }
    }

    // Mapeos
    private Producto mapearDtoADominio(ProductoRequestDTO dto) {
        Producto producto = new Producto();
        producto.setNombre(dto.nombre);
        producto.setDescripcion(dto.descripcion);
        producto.setPrecio(dto.precio);
        producto.setStock(dto.stock);
        return producto;
    }

    private ProductoResponseDTO mapearDominioADto(Producto producto) {
        ProductoResponseDTO dto = new ProductoResponseDTO();
        dto.id = producto.getId();
        dto.nombre = producto.getNombre();
        dto.descripcion = producto.getDescripcion();
        dto.precio = producto.getPrecio();
        dto.stock = producto.getStock();
        dto.imagenUrl = producto.getImagenUrl();
        dto.activo = producto.getActivo();
        dto.createdAt = producto.getCreatedAt();
        return dto;
    }
}
