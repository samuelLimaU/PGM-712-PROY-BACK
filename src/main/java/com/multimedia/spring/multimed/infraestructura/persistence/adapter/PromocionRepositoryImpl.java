package com.multimedia.spring.multimed.infraestructura.persistence.adapter;

import com.multimedia.spring.multimed.dominio.models.Promocion;
import com.multimedia.spring.multimed.dominio.repository.PromocionRepository;
import com.multimedia.spring.multimed.infraestructura.jpa.EntityPromocion;
import com.multimedia.spring.multimed.infraestructura.jpa.EntityPromocionProducto;
import com.multimedia.spring.multimed.infraestructura.jpa.SpringDataPromocionProductoRepository;
import com.multimedia.spring.multimed.infraestructura.jpa.SpringDataPromocionRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class PromocionRepositoryImpl implements PromocionRepository {

    private final SpringDataPromocionRepository springRepo;
    private final SpringDataPromocionProductoRepository relacionRepo;

    public PromocionRepositoryImpl(SpringDataPromocionRepository springRepo, 
                                 SpringDataPromocionProductoRepository relacionRepo) {
        this.springRepo = springRepo;
        this.relacionRepo = relacionRepo;
    }

    // ──────────────────────────────────────────────
    //  CRUD base
    // ──────────────────────────────────────────────
    @Override
    @Transactional
    public Promocion guardar(Promocion promocion) {
        EntityPromocion entity = mapearDominioAEntity(promocion);
        EntityPromocion guardada = springRepo.save(entity);
        
        // Actualizar relaciones de productos
        relacionRepo.deleteByPromocionId(guardada.getId());
        relacionRepo.flush(); // Forzar borrado inmediato

        if (promocion.getProductoIds() != null) {
            List<EntityPromocionProducto> relaciones = promocion.getProductoIds().stream()
                .map(productoId -> new EntityPromocionProducto(guardada.getId(), productoId))
                .collect(Collectors.toList());
            relacionRepo.saveAll(relaciones);
        }
        
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
    @Transactional
    public void eliminar(Long id) {
        relacionRepo.deleteByPromocionId(id);
        springRepo.deleteById(id);
    }

    // ──────────────────────────────────────────────
    //  Gestión de productos asociados
    // ──────────────────────────────────────────────
    @Override
    @Transactional
    public void agregarProducto(Long promocionId, Long productoId) {
        if (relacionRepo.findByPromocionId(promocionId).stream()
                .noneMatch(r -> r.getProductoId().equals(productoId))) {
            relacionRepo.save(new EntityPromocionProducto(promocionId, productoId));
        }
    }

    @Override
    @Transactional
    public void removerProducto(Long promocionId, Long productoId) {
        relacionRepo.findByPromocionId(promocionId).stream()
                .filter(r -> r.getProductoId().equals(productoId))
                .forEach(relacionRepo::delete);
    }

    @Override
    public List<Long> listarProductoIds(Long promocionId) {
        return relacionRepo.findByPromocionId(promocionId)
                .stream()
                .map(EntityPromocionProducto::getProductoId)
                .collect(Collectors.toList());
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
        
        // Cargar los IDs de productos asociados desde la tabla intermedia
        List<Long> productoIds = relacionRepo.findByPromocionId(e.getId())
                .stream()
                .map(EntityPromocionProducto::getProductoId)
                .collect(Collectors.toList());
        p.setProductoIds(productoIds);
        
        return p;
    }
}
