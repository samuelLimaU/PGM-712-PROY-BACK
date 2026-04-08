package com.multimedia.spring.multimed.infraestructura.persistence.adapter;

import com.multimedia.spring.multimed.dominio.models.Promocion;
import com.multimedia.spring.multimed.dominio.repository.PromocionRepository;
import com.multimedia.spring.multimed.infraestructura.jpa.EntityPromocion;
import com.multimedia.spring.multimed.infraestructura.jpa.SpringDataPromocionRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class PromocionRepositoryImpl implements PromocionRepository {

    private final SpringDataPromocionRepository springRepo;

    public PromocionRepositoryImpl(SpringDataPromocionRepository springRepo) {
        this.springRepo = springRepo;
    }

    // ──────────────────────────────────────────────
    //  CRUD base
    // ──────────────────────────────────────────────
    @Override
    public Promocion guardar(Promocion promocion) {
        EntityPromocion entity = mapearDominioAEntity(promocion);
        EntityPromocion guardada = springRepo.save(entity);
        return mapearEntityADominio(guardada);
    }

    @Override
    public List<Promocion> listar() {
        return springRepo.findAll()
                .stream()
                .map(this::mapearEntityADominio)
                .collect(Collectors.toList());
    }

    @Override
    public List<Promocion> listarActivas() {
        return springRepo.findByActivoTrue()
                .stream()
                .map(this::mapearEntityADominio)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Promocion> buscarPorId(Long id) {
        return springRepo.findById(id)
                .map(this::mapearEntityADominio);
    }

    @Override
    public void eliminar(Long id) {
        springRepo.deleteById(id);
    }

    // ──────────────────────────────────────────────
    //  Gestión de productos asociados
    // ──────────────────────────────────────────────
    @Override
    public void agregarProducto(Long promocionId, Long productoId) {
        springRepo.findById(promocionId).ifPresent(entity -> {
            if (!entity.getProductoIds().contains(productoId)) {
                entity.getProductoIds().add(productoId);
                springRepo.save(entity);
            }
        });
    }

    @Override
    public void removerProducto(Long promocionId, Long productoId) {
        springRepo.findById(promocionId).ifPresent(entity -> {
            entity.getProductoIds().remove(productoId);
            springRepo.save(entity);
        });
    }

    @Override
    public List<Long> listarProductoIds(Long promocionId) {
        return springRepo.findById(promocionId)
                .map(EntityPromocion::getProductoIds)
                .orElse(new ArrayList<>());
    }

    // ──────────────────────────────────────────────
    //  Mapeos Entity ↔ Dominio
    // ──────────────────────────────────────────────
    private EntityPromocion mapearDominioAEntity(Promocion p) {
        EntityPromocion e = new EntityPromocion();
        e.setId(p.getId());
        e.setTitulo(p.getTitulo());
        e.setDescripcion(p.getDescripcion());
        e.setImagenUrl(p.getImagenUrl());
        e.setTipo(p.getTipo());
        e.setValor(p.getValor());
        e.setFechaInicio(p.getFechaInicio());
        e.setFechaFin(p.getFechaFin());
        e.setActivo(p.getActivo());
        e.setProductoIds(p.getProductoIds() != null
                ? new ArrayList<>(p.getProductoIds())
                : new ArrayList<>());
        return e;
    }

    private Promocion mapearEntityADominio(EntityPromocion e) {
        Promocion p = new Promocion();
        p.setId(e.getId());
        p.setTitulo(e.getTitulo());
        p.setDescripcion(e.getDescripcion());
        p.setImagenUrl(e.getImagenUrl());
        p.setTipo(e.getTipo());
        p.setValor(e.getValor());
        p.setFechaInicio(e.getFechaInicio());
        p.setFechaFin(e.getFechaFin());
        p.setActivo(e.getActivo());
        p.setProductoIds(e.getProductoIds() != null
                ? new ArrayList<>(e.getProductoIds())
                : new ArrayList<>());
        return p;
    }
}
