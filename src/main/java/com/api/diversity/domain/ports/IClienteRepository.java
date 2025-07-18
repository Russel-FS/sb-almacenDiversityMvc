package com.api.diversity.domain.ports;

import java.util.List;
import java.util.Optional;

import com.api.diversity.domain.model.ClienteEntity;
import com.api.diversity.domain.enums.EstadoCliente;
import com.api.diversity.domain.enums.TipoCliente;

public interface IClienteRepository {

    ClienteEntity save(ClienteEntity cliente);

    Optional<ClienteEntity> findById(Long id);

    List<ClienteEntity> findAll();

    List<ClienteEntity> findByEstado(EstadoCliente estado);

    Optional<ClienteEntity> findByDni(String dni);

    Optional<ClienteEntity> findByEmail(String email);

    List<ClienteEntity> findByTipoCliente(TipoCliente tipoCliente);

    List<ClienteEntity> findByNombreCompletoContainingIgnoreCase(String nombre);

    List<ClienteEntity> findTop10ByOrderByFechaCreacionDesc();

    boolean existsById(Long id);

    boolean existsByDni(String dni);

    boolean existsByEmail(String email);

    void deleteById(Long id);

    Long countByEstado(EstadoCliente estado);

    Long countByTipoCliente(TipoCliente tipoCliente);

    Long count();
}
