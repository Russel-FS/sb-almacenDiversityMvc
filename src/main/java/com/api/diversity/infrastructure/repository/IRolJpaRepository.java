package com.api.diversity.infrastructure.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.diversity.domain.model.RolEntity;

@Repository
public interface IRolJpaRepository extends JpaRepository<RolEntity, Long> {
    Optional<RolEntity> findByNombreRol(String nombreRol);
}