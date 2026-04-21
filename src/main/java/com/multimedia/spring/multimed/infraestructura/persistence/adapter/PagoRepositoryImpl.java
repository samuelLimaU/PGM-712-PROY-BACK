package com.multimedia.spring.multimed.infraestructura.persistence.adapter;

import com.multimedia.spring.multimed.dominio.models.Pago;
import com.multimedia.spring.multimed.dominio.repository.PagoRepository;
import com.multimedia.spring.multimed.infraestructura.jpa.EntityPago;
import com.multimedia.spring.multimed.infraestructura.jpa.EntityPedido;
import com.multimedia.spring.multimed.infraestructura.jpa.SpringDataPagoRepository;
import com.multimedia.spring.multimed.infraestructura.jpa.SpringDataPedidoRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PagoRepositoryImpl implements PagoRepository {

    private final SpringDataPagoRepository springDataRepo;
    private final SpringDataPedidoRepository pedidoRepo;

    public PagoRepositoryImpl(SpringDataPagoRepository springDataRepo, SpringDataPedidoRepository pedidoRepo) {
        this.springDataRepo = springDataRepo;
        this.pedidoRepo = pedidoRepo;
    }

    @Override
    public Pago save(Pago pago) {
        EntityPago entity = toEntity(pago);
        EntityPago saved = springDataRepo.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<Pago> findByPedidoId(Long pedidoId) {
        // En un caso real añadiríamos findByPedidoId a SpringDataPagoRepository
        return springDataRepo.findAll().stream()
                .filter(p -> p.getPedido().getId().equals(pedidoId))
                .findFirst()
                .map(this::toDomain);
    }

    private EntityPago toEntity(Pago domain) {
        EntityPago entity = new EntityPago();
        entity.setId(domain.getId());
        entity.setMetodo(domain.getMetodo());
        entity.setEstado(domain.getEstado());
        entity.setMonto(domain.getMonto());
        entity.setFecha(domain.getFecha());
        entity.setFechaPago(domain.getFechaPago());
        entity.setNotas(domain.getNotas());
        
        if (domain.getPedidoId() != null) {
            EntityPedido pedido = pedidoRepo.findById(domain.getPedidoId()).orElse(null);
            entity.setPedido(pedido);
        }
        
        return entity;
    }

    private Pago toDomain(EntityPago entity) {
        return new Pago(
            entity.getId(),
            entity.getPedido() != null ? entity.getPedido().getId() : null,
            entity.getMetodo(),
            entity.getEstado(),
            entity.getMonto(),
            entity.getFecha(),
            entity.getFechaPago(),
            entity.getNotas()
        );
    }
}
