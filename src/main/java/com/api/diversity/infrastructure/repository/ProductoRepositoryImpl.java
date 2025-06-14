package com.api.diversity.infrastructure.repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.api.diversity.application.dto.CategoriaDto;
import com.api.diversity.application.dto.Producto;
import com.api.diversity.domain.model.CategoryEntity;
import com.api.diversity.domain.model.ProductoEntity;
import com.api.diversity.domain.ports.IProductoRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ProductoRepositoryImpl implements IProductoRepository {
    
    private final IProductoJpaRepository productoJpaRepository;
    
    private Producto mapToModel(ProductoEntity entity) {
        Producto producto = new Producto();
        producto.setIdProducto(entity.getIdProducto());
        producto.setNombre(entity.getNombre());
        producto.setDescripcion(entity.getDescripcion());
        if (entity.getCategoria() != null) {
            CategoriaDto categoria = new CategoriaDto();
            categoria.setIdCategoria(entity.getCategoria().getIdCategoria());
            categoria.setNombreCategoria(entity.getCategoria().getNombreCategoria());
            categoria.setDescripcion(entity.getCategoria().getDescripcion());
            producto.setCategoria(categoria);
        }
        producto.setStock(entity.getStock());
        producto.setUrlImagen(entity.getUrlImagen());
        producto.setPrecioUnitario(entity.getPrecioUnitario());
        producto.setFechaRegistro(entity.getFechaRegistro());
        return producto;
    }
    
    private ProductoEntity mapToEntity(Producto model) {
        ProductoEntity entity = new ProductoEntity();
        entity.setIdProducto(model.getIdProducto());
        entity.setNombre(model.getNombre());
        entity.setDescripcion(model.getDescripcion());
        if (model.getCategoria() != null) {
            CategoryEntity categoriaEntity = new CategoryEntity();
            categoriaEntity.setIdCategoria(model.getCategoria().getIdCategoria());
            categoriaEntity.setNombreCategoria(model.getCategoria().getNombreCategoria());
            categoriaEntity.setDescripcion(model.getCategoria().getDescripcion());
            entity.setCategoria(categoriaEntity);
        }
        entity.setStock(model.getStock());
        entity.setUrlImagen(model.getUrlImagen());
        entity.setPrecioUnitario(model.getPrecioUnitario());
        entity.setFechaRegistro(model.getFechaRegistro());
        return entity;
    }

    @Override
    public List<Producto> findAll() {
        return productoJpaRepository.findAll()
                .stream()
                .map(this::mapToModel)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Producto> findById(String id) {
        return productoJpaRepository.findById(id)
                .map(this::mapToModel);
    }

    @Override
    public Producto save(Producto producto) {
        ProductoEntity entity = mapToEntity(producto);
        return mapToModel(productoJpaRepository.save(entity));
    }

    @Override
    public void deleteById(String id) {
        productoJpaRepository.deleteById(id);
    }

    @Override
    public List<Producto> findByCategoria(Long categoriaId) {
        return productoJpaRepository.findByCategoria_IdCategoria(categoriaId)
                .stream()
                .map(this::mapToModel)
                .collect(Collectors.toList());
    }
}
