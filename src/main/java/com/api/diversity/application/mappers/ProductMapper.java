package com.api.diversity.application.mappers;

import org.springframework.stereotype.Component;

import com.api.diversity.application.dto.CategoriaDto;
import com.api.diversity.application.dto.Producto;
import com.api.diversity.domain.model.ProductoEntity;

@Component
public class ProductMapper {

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
            CategoriaDto categoria = new CategoriaDto();
            categoria.setIdCategoria(model.getCategoria().getIdCategoria());
            categoria.setNombreCategoria(model.getCategoria().getNombreCategoria());
            categoria.setDescripcion(model.getCategoria().getDescripcion());
            entity.setCategoria(categoria);
        }
        entity.setStock(model.getStock());
        entity.setUrlImagen(model.getUrlImagen());
        entity.setPrecioUnitario(model.getPrecioUnitario());
        entity.setFechaRegistro(model.getFechaRegistro());
        return entity;
    }
}
