package com.api.diversity.domain.ports;

import com.api.diversity.domain.model.RubroEntity;
import com.api.diversity.domain.enums.NombreRubro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RubroRepository extends JpaRepository<RubroEntity, Long> {
    boolean existsByNombreRubro(NombreRubro nombreRubro);
}