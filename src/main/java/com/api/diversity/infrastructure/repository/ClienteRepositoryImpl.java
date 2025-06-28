package com.api.diversity.infrastructure.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.api.diversity.domain.model.ClienteEntity;
import com.api.diversity.domain.ports.IClienteRepository;
import com.api.diversity.domain.enums.EstadoCliente;
import com.api.diversity.domain.enums.TipoCliente;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ClienteRepositoryImpl implements IClienteRepository {

    private final IClienteJpaRepository clienteJpaRepository;

    @Override
    public ClienteEntity save(ClienteEntity cliente) {
        return clienteJpaRepository.save(cliente);
    }

    @Override
    public Optional<ClienteEntity> findById(Long id) {
        return clienteJpaRepository.findById(id);
    }

    @Override
    public List<ClienteEntity> findAll() {
        return clienteJpaRepository.findAll();
    }

    @Override
    public List<ClienteEntity> findByEstado(EstadoCliente estado) {
        return clienteJpaRepository.findByEstado(estado);
    }

    @Override
    public Optional<ClienteEntity> findByDni(String dni) {
        return clienteJpaRepository.findByNumeroDocumento(dni);
    }

    @Override
    public Optional<ClienteEntity> findByEmail(String email) {
        return clienteJpaRepository.findByEmailContainingIgnoreCase(email).stream().findFirst();
    }

    @Override
    public List<ClienteEntity> findByTipoCliente(TipoCliente tipoCliente) {
        return clienteJpaRepository.findByTipoCliente(tipoCliente);
    }

    @Override
    public List<ClienteEntity> findByNombreCompletoContainingIgnoreCase(String nombre) {
        return clienteJpaRepository.findByNombreCompletoContainingIgnoreCase(nombre);
    }

    @Override
    public List<ClienteEntity> findTop10ByOrderByFechaCreacionDesc() {
        return clienteJpaRepository.findTop10ByOrderByIdDesc();
    }

    @Override
    public boolean existsById(Long id) {
        return clienteJpaRepository.existsById(id);
    }

    @Override
    public boolean existsByDni(String dni) {
        return clienteJpaRepository.existsByNumeroDocumento(dni);
    }

    @Override
    public boolean existsByEmail(String email) {
        return clienteJpaRepository.existsByEmail(email);
    }

    @Override
    public void deleteById(Long id) {
        clienteJpaRepository.deleteById(id);
    }

    @Override
    public Long countByEstado(EstadoCliente estado) {
        return clienteJpaRepository.countByEstado(estado);
    }

    @Override
    public Long countByTipoCliente(TipoCliente tipoCliente) {
        return clienteJpaRepository.countByTipoCliente(tipoCliente);
    }

    @Override
    public Long count() {
        return clienteJpaRepository.count();
    }
}
