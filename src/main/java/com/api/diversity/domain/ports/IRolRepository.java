package com.api.diversity.domain.ports;

import java.util.Optional;

import com.api.diversity.domain.model.RolEntity;

public interface IRolRepository {
    Optional<RolEntity> findById(Long id);

    RolEntity save(RolEntity rol);

    Optional<RolEntity> findByNombreRol(String nombreRol);
}