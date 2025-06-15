package com.api.diversity.domain.ports;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.diversity.domain.model.RubroEntity;

@Repository
public interface RubroRepository extends JpaRepository<RubroEntity, Long> {
    boolean existsByNombreRubro(String nombreRubro);
    Optional<RubroEntity> findByNombreRubro(String nombreRubro);
}