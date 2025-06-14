package com.api.diversity.infrastructure.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.diversity.domain.model.ProductoEntity;

public interface IProductoJpaRepository extends JpaRepository<ProductoEntity, String> {
    List<ProductoEntity> findByCategoria_IdCategoria(Long categoriaId);
}
