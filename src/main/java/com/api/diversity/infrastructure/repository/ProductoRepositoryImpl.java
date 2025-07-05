package com.api.diversity.infrastructure.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.api.diversity.domain.model.ProductoEntity;
import com.api.diversity.domain.ports.IProductoRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ProductoRepositoryImpl implements IProductoRepository {

    private final IProductoJpaRepository productoJpaRepository;

    @Override
    public List<ProductoEntity> findAll() {
        return productoJpaRepository.findAll();
    }

    @Override
    public Optional<ProductoEntity> findById(Long id) {
        return productoJpaRepository.findById(id);
    }

    @Override
    public ProductoEntity save(ProductoEntity producto) {
        return productoJpaRepository.save(producto);
    }

    @Override
    public void deleteById(Long id) {
        productoJpaRepository.deleteById(id);
    }

    @Override
    public List<ProductoEntity> findByCategoria(Long categoriaId) {
        return productoJpaRepository.findByCategoria_IdCategoria(categoriaId);
    }

    @Override
    public boolean existsByCodigoProducto(String codigoProducto) {
        return productoJpaRepository.existsByCodigoProducto(codigoProducto);
    }

    @Override
    public boolean existsByCodigoProductoAndIdProductoNot(String codigoProducto, Long idProducto) {
        return productoJpaRepository.existsByCodigoProductoAndIdProductoNot(codigoProducto, idProducto);
    }
}
