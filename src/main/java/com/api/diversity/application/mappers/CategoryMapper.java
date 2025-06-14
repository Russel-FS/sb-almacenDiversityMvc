package com.api.diversity.application.mappers;

import org.springframework.stereotype.Component;

import com.api.diversity.application.dto.CategoriaDto; 
import com.api.diversity.domain.model.CategoryEntity;
@Component
public class CategoryMapper {

    public CategoriaDto toDto(CategoryEntity entity) {
        if (entity == null) {
            return null;
        }
        CategoriaDto dto = CategoriaDto.builder()
                .idCategoria(entity.getIdCategoria())
                .nombreCategoria(entity.getNombreCategoria())
                .descripcion(entity.getDescripcion())
                .build();
        return dto;
    }

    public CategoryEntity toEntity(CategoriaDto dto) {
        if (dto == null) {
            return null;
        }
        CategoryEntity entity = new CategoryEntity();
        entity.setIdCategoria(dto.getIdCategoria());
        entity.setNombreCategoria(dto.getNombreCategoria());
        entity.setDescripcion(dto.getDescripcion());
        return entity;
    }
}
