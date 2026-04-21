package com.multimedia.spring.multimed.dominio.repository;

import com.multimedia.spring.multimed.dominio.models.ClienteInfo;
import java.util.Optional;

public interface ClienteInfoRepository {
    ClienteInfo save(ClienteInfo clienteInfo);
    Optional<ClienteInfo> findById(Long id);
}
