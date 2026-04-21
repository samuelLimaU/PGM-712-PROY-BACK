package com.multimedia.spring.multimed.infraestructura.persistence.adapter;

import com.multimedia.spring.multimed.dominio.models.Pedido;
import com.multimedia.spring.multimed.dominio.repository.PedidoRepository;
import com.multimedia.spring.multimed.infraestructura.jpa.EntityClienteInfo;
import com.multimedia.spring.multimed.infraestructura.jpa.EntityPedido;
import com.multimedia.spring.multimed.infraestructura.jpa.SpringDataClienteInfoRepository;
import com.multimedia.spring.multimed.infraestructura.jpa.SpringDataPedidoRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class PedidoRepositoryImpl implements PedidoRepository {

    private final SpringDataPedidoRepository springDataRepo;
    private final SpringDataClienteInfoRepository clienteRepo;

    public PedidoRepositoryImpl(SpringDataPedidoRepository springDataRepo, SpringDataClienteInfoRepository clienteRepo) {
        this.springDataRepo = springDataRepo;
        this.clienteRepo = clienteRepo;
    }

    @Override
    public Pedido save(Pedido pedido) {
        EntityPedido entity = toEntity(pedido);
        EntityPedido saved = springDataRepo.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<Pedido> findById(Long id) {
        return springDataRepo.findById(id).map(this::toDomain);
    }

    @Override
    public List<Pedido> findAll() {
        return springDataRepo.findAll().stream().map(this::toDomain).collect(Collectors.toList());
    }

    private EntityPedido toEntity(Pedido domain) {
        EntityPedido entity = new EntityPedido();
        entity.setId(domain.getId());
        entity.setFecha(domain.getFecha());
        entity.setEstado(domain.getEstado());
        entity.setTotal(domain.getTotal());
        entity.setNotas(domain.getNotas());
        
        if (domain.getClienteId() != null) {
            EntityClienteInfo cliente = clienteRepo.findById(domain.getClienteId()).orElse(null);
            entity.setCliente(cliente);
        }
        
        return entity;
    }

    private Pedido toDomain(EntityPedido entity) {
        return new Pedido(
            entity.getId(),
            entity.getCliente() != null ? entity.getCliente().getId() : null,
            entity.getFecha(),
            entity.getEstado(),
            entity.getTotal(),
            entity.getNotas()
        );
    }
}
