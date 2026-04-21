package com.multimedia.spring.multimed.infraestructura.persistence.adapter;

import com.multimedia.spring.multimed.dominio.models.PedidoDetalle;
import com.multimedia.spring.multimed.dominio.repository.PedidoDetalleRepository;
import com.multimedia.spring.multimed.infraestructura.jpa.EntityPedido;
import com.multimedia.spring.multimed.infraestructura.jpa.EntityPedidoDetalle;
import com.multimedia.spring.multimed.infraestructura.jpa.EntityProducto;
import com.multimedia.spring.multimed.infraestructura.jpa.SpringDataPedidoDetalleRepository;
import com.multimedia.spring.multimed.infraestructura.jpa.SpringDataPedidoRepository;
import com.multimedia.spring.multimed.infraestructura.jpa.SpringDataProductoRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PedidoDetalleRepositoryImpl implements PedidoDetalleRepository {

    private final SpringDataPedidoDetalleRepository springDataRepo;
    private final SpringDataPedidoRepository pedidoRepo;
    private final SpringDataProductoRepository productoRepo;

    public PedidoDetalleRepositoryImpl(SpringDataPedidoDetalleRepository springDataRepo, SpringDataPedidoRepository pedidoRepo, SpringDataProductoRepository productoRepo) {
        this.springDataRepo = springDataRepo;
        this.pedidoRepo = pedidoRepo;
        this.productoRepo = productoRepo;
    }

    @Override
    public PedidoDetalle save(PedidoDetalle detalle) {
        EntityPedidoDetalle entity = toEntity(detalle);
        EntityPedidoDetalle saved = springDataRepo.save(entity);
        return toDomain(saved);
    }

    @Override
    public List<PedidoDetalle> findByPedidoId(Long pedidoId) {
        // En un caso real añadiríamos findByPedidoId a SpringDataPedidoDetalleRepository
        // Por ahora usamos findAll y filtramos para no complicar el Spring Data Repo
        return springDataRepo.findAll().stream()
                .filter(d -> d.getPedido().getId().equals(pedidoId))
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    private EntityPedidoDetalle toEntity(PedidoDetalle domain) {
        EntityPedidoDetalle entity = new EntityPedidoDetalle();
        entity.setId(domain.getId());
        entity.setCantidad(domain.getCantidad());
        entity.setPrecioUnitario(domain.getPrecioUnitario());
        
        if (domain.getPedidoId() != null) {
            EntityPedido pedido = pedidoRepo.findById(domain.getPedidoId()).orElse(null);
            entity.setPedido(pedido);
        }
        
        if (domain.getProductoId() != null) {
            EntityProducto producto = productoRepo.findById(domain.getProductoId()).orElse(null);
            entity.setProducto(producto);
        }
        
        return entity;
    }

    private PedidoDetalle toDomain(EntityPedidoDetalle entity) {
        return new PedidoDetalle(
            entity.getId(),
            entity.getPedido() != null ? entity.getPedido().getId() : null,
            entity.getProducto() != null ? entity.getProducto().getId() : null,
            entity.getCantidad(),
            entity.getPrecioUnitario()
        );
    }
}
