package com.api.diversity.infrastructure.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.diversity.domain.model.ProductoEntity;

@Repository
public interface IProductoJpaRepository extends JpaRepository<ProductoEntity, Long> {
    List<ProductoEntity> findByCategoria_IdCategoria(Long categoriaId);
}
