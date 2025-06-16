package com.api.diversity.infrastructure.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.diversity.domain.enums.EstadoCategoria;
import com.api.diversity.domain.model.CategoryEntity;

@Repository
public interface CategoryJpaRepository extends JpaRepository<CategoryEntity, Long> {
       boolean existsByNombreCategoria(String nombreCategoria);
       
       List<CategoryEntity> findByRubro_CodeContainingAndEstado(String rubroCode, EstadoCategoria estado);
}
