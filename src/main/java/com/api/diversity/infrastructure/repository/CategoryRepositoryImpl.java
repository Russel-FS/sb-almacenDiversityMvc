package com.api.diversity.infrastructure.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.api.diversity.domain.enums.EstadoCategoria;
import com.api.diversity.domain.enums.TipoRubro;
import com.api.diversity.domain.model.CategoryEntity;
import com.api.diversity.domain.ports.ICategoriaRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CategoryRepositoryImpl implements ICategoriaRepository {

    private final CategoryJpaRepository categoryJpaRepository;

    @Override
    public List<CategoryEntity> findAll() {
        return categoryJpaRepository.findAll();
    }

    @Override
    public Optional<CategoryEntity> findById(Long id) {
        return categoryJpaRepository.findById(id);
    }

    @Override
    public CategoryEntity save(CategoryEntity category) {
        return categoryJpaRepository.save(category);
    }

    @Override
    public void deleteById(Long id) {
        categoryJpaRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return categoryJpaRepository.existsById(id);
    }

    @Override
    public boolean existsByNombreCategoria(String nombreCategoria) {
        return categoryJpaRepository.existsByNombreCategoria(nombreCategoria);
    }

    @Override
    public List<CategoryEntity> findAllByRubroAndEstado(TipoRubro rubro, EstadoCategoria estado) {
        return categoryJpaRepository.findByRubro_CodeContainingAndEstado(rubro.getCode(), estado);
    }
}
