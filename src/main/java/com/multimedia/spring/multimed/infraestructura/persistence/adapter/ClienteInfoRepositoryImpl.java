package com.multimedia.spring.multimed.infraestructura.persistence.adapter;

import com.multimedia.spring.multimed.dominio.models.ClienteInfo;
import com.multimedia.spring.multimed.dominio.repository.ClienteInfoRepository;
import com.multimedia.spring.multimed.infraestructura.jpa.EntityClienteInfo;
import com.multimedia.spring.multimed.infraestructura.jpa.EntityUsuario;
import com.multimedia.spring.multimed.infraestructura.jpa.SpringDataClienteInfoRepository;
import com.multimedia.spring.multimed.infraestructura.jpa.SpringDataUsuarioRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ClienteInfoRepositoryImpl implements ClienteInfoRepository {

    private final SpringDataClienteInfoRepository springDataRepo;
    private final SpringDataUsuarioRepository usuarioRepo;

    public ClienteInfoRepositoryImpl(SpringDataClienteInfoRepository springDataRepo, SpringDataUsuarioRepository usuarioRepo) {
        this.springDataRepo = springDataRepo;
        this.usuarioRepo = usuarioRepo;
    }

    @Override
    public ClienteInfo save(ClienteInfo clienteInfo) {
        EntityClienteInfo entity = toEntity(clienteInfo);
        EntityClienteInfo saved = springDataRepo.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<ClienteInfo> findById(Long id) {
        return springDataRepo.findById(id).map(this::toDomain);
    }

    private EntityClienteInfo toEntity(ClienteInfo domain) {
        EntityClienteInfo entity = new EntityClienteInfo();
        entity.setId(domain.getId());
        entity.setNombre(domain.getNombre());
        entity.setTelefono(domain.getTelefono());
        entity.setDireccion(domain.getDireccion());
        entity.setCiudad(domain.getCiudad());
        entity.setReferencia(domain.getReferencia());
        
        if (domain.getUsuarioId() != null) {
            EntityUsuario user = usuarioRepo.findById(domain.getUsuarioId()).orElse(null);
            entity.setUsuario(user);
        }
        
        return entity;
    }

    private ClienteInfo toDomain(EntityClienteInfo entity) {
        return new ClienteInfo(
            entity.getId(),
            entity.getUsuario() != null ? entity.getUsuario().getId() : null,
            entity.getNombre(),
            entity.getTelefono(),
            entity.getDireccion(),
            entity.getCiudad(),
            entity.getReferencia()
        );
    }
}
