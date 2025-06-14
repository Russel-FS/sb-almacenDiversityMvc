package com.api.diversity.application.mappers;

import org.springframework.stereotype.Component;

import com.api.diversity.application.dto.Producto;
import com.api.diversity.domain.model.ProductoEntity;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProductMapper {

    private final CategoryMapper categoryMapper;
    private final RubroMapper rubroMapper;

    public Producto toModel(ProductoEntity entity) {
        if (entity == null) {
            return null;
        }

        return Producto.builder()
                .idProducto(entity.getIdProducto())
                .codigoProducto(entity.getCodigoProducto())
                .nombreProducto(entity.getNombreProducto())
                .descripcion(entity.getDescripcion())
                .categoria(categoryMapper.toDto(entity.getCategoria()))
                .rubro(rubroMapper.toDto(entity.getRubro()))
                .precioCompra(entity.getPrecioCompra())
                .precioVenta(entity.getPrecioVenta())
                .stockActual(entity.getStockActual())
                .stockMinimo(entity.getStockMinimo())
                .stockMaximo(entity.getStockMaximo())
                .estado(entity.getEstado())
                .fechaCreacion(entity.getFechaCreacion())
                .fechaModificacion(entity.getFechaModificacion())
                .build();
    }

    public ProductoEntity toEntity(Producto model) {
        if (model == null) {
            return null;
        }

        ProductoEntity entity = new ProductoEntity();
        entity.setIdProducto(model.getIdProducto());
        entity.setCodigoProducto(model.getCodigoProducto());
        entity.setNombreProducto(model.getNombreProducto());
        entity.setDescripcion(model.getDescripcion());
        entity.setCategoria(categoryMapper.toEntity(model.getCategoria()));
        entity.setRubro(rubroMapper.toEntity(model.getRubro()));
        entity.setPrecioCompra(model.getPrecioCompra());
        entity.setPrecioVenta(model.getPrecioVenta());
        entity.setStockActual(model.getStockActual());
        entity.setStockMinimo(model.getStockMinimo());
        entity.setStockMaximo(model.getStockMaximo());
        entity.setEstado(model.getEstado());
        entity.setFechaCreacion(model.getFechaCreacion());
        entity.setFechaModificacion(model.getFechaModificacion());
        return entity;
    }
}
