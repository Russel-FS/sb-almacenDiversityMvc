package com.api.diversity.infrastructure.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.api.diversity.domain.model.ProveedorEntity;
import com.api.diversity.domain.ports.IProveedorRepository;
import com.api.diversity.domain.enums.EstadoProveedor;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ProveedorRepositoryImpl implements IProveedorRepository {

    private final IProveedorJpaRepository proveedorJpaRepository;

    @Override
    public ProveedorEntity save(ProveedorEntity proveedor) {
        return proveedorJpaRepository.save(proveedor);
    }

    @Override
    public Optional<ProveedorEntity> findById(Long id) {
        return proveedorJpaRepository.findById(id);
    }

    @Override
    public List<ProveedorEntity> findAll() {
        return proveedorJpaRepository.findAll();
    }

    @Override
    public List<ProveedorEntity> findByEstado(EstadoProveedor estado) {
        return proveedorJpaRepository.findByEstado(estado);
    }

    @Override
    public Optional<ProveedorEntity> findByRuc(String ruc) {
        return proveedorJpaRepository.findByNumeroDocumento(ruc);
    }

    @Override
    public Optional<ProveedorEntity> findByEmail(String email) {
        return proveedorJpaRepository.findByEmailContainingIgnoreCase(email).stream().findFirst();
    }

    @Override
    public List<ProveedorEntity> findByRazonSocialContainingIgnoreCase(String razonSocial) {
        return proveedorJpaRepository.findByNombreEmpresaContainingIgnoreCase(razonSocial);
    }

    @Override
    public List<ProveedorEntity> findByRepresentanteLegalContainingIgnoreCase(String representanteLegal) {
        return proveedorJpaRepository.findByNombreContactoContainingIgnoreCase(representanteLegal);
    }

    @Override
    public List<ProveedorEntity> findTop10ByOrderByFechaCreacionDesc() {
        return proveedorJpaRepository.findTop10ByOrderByIdDesc();
    }

    @Override
    public boolean existsById(Long id) {
        return proveedorJpaRepository.existsById(id);
    }

    @Override
    public boolean existsByRuc(String ruc) {
        return proveedorJpaRepository.existsByNumeroDocumento(ruc);
    }

    @Override
    public boolean existsByEmail(String email) {
        return proveedorJpaRepository.existsByEmail(email);
    }

    @Override
    public void deleteById(Long id) {
        proveedorJpaRepository.deleteById(id);
    }

    @Override
    public Long countByEstado(EstadoProveedor estado) {
        return proveedorJpaRepository.countByEstado(estado);
    }

    @Override
    public Long count() {
        return proveedorJpaRepository.count();
    }
}
