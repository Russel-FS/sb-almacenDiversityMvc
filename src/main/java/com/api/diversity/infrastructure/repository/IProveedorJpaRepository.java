package com.api.diversity.infrastructure.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.diversity.domain.model.ProveedorEntity;
import com.api.diversity.domain.enums.EstadoProveedor;

@Repository
public interface IProveedorJpaRepository extends JpaRepository<ProveedorEntity, Long> {
    List<ProveedorEntity> findByEstado(EstadoProveedor estado);

    Optional<ProveedorEntity> findByRuc(String ruc);

    List<ProveedorEntity> findByNombreContainingIgnoreCase(String nombre);

    Long countByEstado(EstadoProveedor estado);
}