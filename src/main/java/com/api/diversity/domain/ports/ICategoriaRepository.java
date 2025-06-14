package com.api.diversity.domain.ports;

import java.util.List;
import java.util.Optional;

import com.api.diversity.domain.model.CategoryEntity;

public interface ICategoriaRepository {
    List<CategoryEntity> findAll();
    Optional<CategoryEntity> findById(Long id);
    CategoryEntity save(CategoryEntity category);
    void deleteById(Long id);
    boolean existsById(Long id);
    boolean existsByNombreCategoria(String nombreCategoria);
}
