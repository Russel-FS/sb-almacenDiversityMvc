package com.api.diversity.domain.ports;

import java.util.List;
import java.util.Optional;

import com.api.diversity.domain.model.ProveedorEntity;
import com.api.diversity.domain.enums.EstadoProveedor;

public interface IProveedorRepository {
    ProveedorEntity save(ProveedorEntity proveedor);

    Optional<ProveedorEntity> findById(Long id);

    List<ProveedorEntity> findAll();

    List<ProveedorEntity> findByEstado(EstadoProveedor estado);

    Optional<ProveedorEntity> findByRuc(String ruc);

    List<ProveedorEntity> findByNombreContainingIgnoreCase(String nombre);

    boolean existsById(Long id);

    boolean existsByRuc(String ruc);

    void deleteById(Long id);

    Long countByEstado(EstadoProveedor estado);
}