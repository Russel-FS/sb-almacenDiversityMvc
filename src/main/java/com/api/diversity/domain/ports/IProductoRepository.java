package com.api.diversity.domain.ports;

import java.util.List;
import java.util.Optional;

import com.api.diversity.domain.model.ProductoEntity;

public interface IProductoRepository {
    List<ProductoEntity> findAll();
    Optional<ProductoEntity> findById(String id);
    ProductoEntity save(ProductoEntity producto);
    void deleteById(String id);
    List<ProductoEntity> findByCategoria(Long categoriaId);
}
