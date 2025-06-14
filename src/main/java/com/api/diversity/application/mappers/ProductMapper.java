package com.api.diversity.application.mappers;

import org.springframework.stereotype.Component;

import com.api.diversity.application.dto.CategoriaDto;
import com.api.diversity.application.dto.Producto;
import com.api.diversity.domain.model.ProductoEntity;

import lombok.RequiredArgsConstructor;
 

@Component
@RequiredArgsConstructor
public class ProductMapper {

    private final CategoryMapper categoryMapper;

    public Producto toModel(ProductoEntity entity) {
        Producto producto = new Producto();
        producto.setIdProducto(entity.getIdProducto());
        producto.setNombre(entity.getNombre());
        producto.setDescripcion(entity.getDescripcion());
        if (entity.getCategoria() != null) {
            CategoriaDto categoria = categoryMapper.toDto(entity.getCategoria());
            producto.setCategoria(categoria);
        }
        producto.setStock(entity.getStock());
        producto.setUrlImagen(entity.getUrlImagen());
        producto.setPrecioUnitario(entity.getPrecioUnitario());
        producto.setFechaRegistro(entity.getFechaRegistro());
        return producto;
    }

    public ProductoEntity toEntity(Producto model) {
        ProductoEntity entity = new ProductoEntity();
        entity.setIdProducto(model.getIdProducto());
        entity.setNombre(model.getNombre());
        entity.setDescripcion(model.getDescripcion());
        if (model.getCategoria() != null) {
            CategoriaDto categoria = model.getCategoria();
            entity.setCategoria(categoryMapper.toEntity(categoria));
        }
        entity.setStock(model.getStock());
        entity.setUrlImagen(model.getUrlImagen());
        entity.setPrecioUnitario(model.getPrecioUnitario());
        entity.setFechaRegistro(model.getFechaRegistro());
        return entity;
    }
}
