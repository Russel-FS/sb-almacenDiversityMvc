package com.api.diversity.application.mappers;

import org.springframework.stereotype.Component;

import com.api.diversity.application.dto.CategoriaDto;
import com.api.diversity.domain.model.CategoryEntity;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CategoryMapper {

    private final RubroMapper rubroMapper;

    public CategoriaDto toDto(CategoryEntity entity) {
        if (entity == null) {
            return null;
        }
        return CategoriaDto.builder()
                .idCategoria(entity.getIdCategoria())
                .rubro(rubroMapper.toDto(entity.getRubro()))
                .nombreCategoria(entity.getNombreCategoria())
                .descripcion(entity.getDescripcion())
                .estado(entity.getEstado())
                .build();
    }

    public CategoryEntity toEntity(CategoriaDto dto) {
        if (dto == null) {
            return null;
        }
        CategoryEntity entity = new CategoryEntity();
        entity.setIdCategoria(dto.getIdCategoria());
        entity.setRubro(rubroMapper.toEntity(dto.getRubro()));
        entity.setNombreCategoria(dto.getNombreCategoria());
        entity.setDescripcion(dto.getDescripcion());
        entity.setEstado(dto.getEstado());
        return entity;
    }
}
