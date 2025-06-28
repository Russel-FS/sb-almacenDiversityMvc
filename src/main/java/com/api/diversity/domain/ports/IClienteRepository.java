package com.api.diversity.domain.ports;

import java.util.List;
import java.util.Optional;

import com.api.diversity.domain.model.ClienteEntity;
import com.api.diversity.domain.enums.EstadoCliente;

public interface IClienteRepository {

    ClienteEntity save(ClienteEntity cliente);

    Optional<ClienteEntity> findById(Long id);

    List<ClienteEntity> findAll();

    List<ClienteEntity> findByEstado(EstadoCliente estado);

    Optional<ClienteEntity> findByDni(String dni);

    List<ClienteEntity> findByNombreContainingIgnoreCase(String nombre);

    boolean existsById(Long id);

    boolean existsByDni(String dni);

    void deleteById(Long id);

    Long countByEstado(EstadoCliente estado);
}