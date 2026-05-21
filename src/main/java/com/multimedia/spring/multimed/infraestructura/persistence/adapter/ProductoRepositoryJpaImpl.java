package com.multimedia.spring.multimed.infraestructura.persistence.adapter;

import com.multimedia.spring.multimed.dominio.models.Producto;
import com.multimedia.spring.multimed.dominio.repository.ProductoRepository;
import com.multimedia.spring.multimed.infraestructura.jpa.EntityProducto;
import com.multimedia.spring.multimed.infraestructura.jpa.SpringDataProductoRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ProductoRepositoryJpaImpl implements ProductoRepository {

    private final SpringDataProductoRepository springDataRepo;

    public ProductoRepositoryJpaImpl(SpringDataProductoRepository springDataRepo) {
        this.springDataRepo = springDataRepo;
    }

    @Override
    public Producto guardar(Producto producto) {
        EntityProducto entity = mapearDominioAEntity(producto);
        EntityProducto guardada = springDataRepo.save(entity);
        return mapearEntityADominio(guardada);
    }

    @Override
    public List<Producto> listar() {
        return springDataRepo.findAll(Sort.by(Sort.Direction.DESC, "id"))
                .stream()
                .map(this::mapearEntityADominio)
                .collect(Collectors.toList());
    }

    @Override
    public List<Producto> listarPaginado(int page, int size) {
        return springDataRepo.findAll(PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id")))
                .getContent()
                .stream()
                .map(this::mapearEntityADominio)
                .collect(Collectors.toList());
    }

    @Override
    public long contarTotal() {
        return springDataRepo.count();
    }

    @Override
    public Optional<Producto> buscarPorId(Long id) {
        return springDataRepo.findById(id)
                .map(this::mapearEntityADominio);
    }

    @Override
    public void eliminar(Long id) {
        springDataRepo.deleteById(id);
    }

    @Override
    public List<Producto> listarActivos() {
        return springDataRepo.findByActivoTrue(Sort.by(Sort.Direction.DESC, "id"))
                .stream()
                .map(this::mapearEntityADominio)
                .collect(Collectors.toList());
    }

    @Override
    public List<Producto> listarActivosPaginados(int page, int size) {
        return springDataRepo.findByActivoTrue(PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id")))
                .getContent()
                .stream()
                .map(this::mapearEntityADominio)
                .collect(Collectors.toList());
    }

    @Override
    public long contarActivos() {
        return springDataRepo.countByActivoTrue();
    }

    @Override
    public boolean existePorNombre(String nombre) {
        return springDataRepo.existsByNombreIgnoreCase(nombre);
    }

    // ──────────────────────────────────────────────
    // Mapeos Entity JPA ↔ Dominio
    // ──────────────────────────────────────────────
    private EntityProducto mapearDominioAEntity(Producto producto) {
        EntityProducto entity = new EntityProducto();
        entity.setId(producto.getId());
        entity.setNombre(producto.getNombre());
        entity.setDescripcion(producto.getDescripcion());
        entity.setPrecio(producto.getPrecio());
        entity.setStock(producto.getStock());
        entity.setImagenUrl(producto.getImagenUrl());
        entity.setActivo(producto.getActivo());
        entity.setCreatedAt(producto.getCreatedAt());
        return entity;
    }

    private Producto mapearEntityADominio(EntityProducto entity) {
        return new Producto(
                entity.getId(),
                entity.getNombre(),
                entity.getDescripcion(),
                entity.getPrecio(),
                entity.getStock(),
                entity.getImagenUrl(),
                entity.getActivo(),
                entity.getCreatedAt()
        );
    }
}
