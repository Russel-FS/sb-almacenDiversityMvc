package com.api.diversity.application.mappers;

import org.springframework.stereotype.Component;

import com.api.diversity.application.dto.RubroDto;
import com.api.diversity.domain.model.RubroEntity;

@Component
public class RubroMapper {

    public RubroDto toDto(RubroEntity entity) {
        if (entity == null) {
            return null;
        }

        return RubroDto.builder()
                .idRubro(entity.getIdRubro())
                .nombreRubro(entity.getNombreRubro())
                .code(entity.getCode())
                .descripcion(entity.getDescripcion())
                .estado(entity.getEstado())
                .build();
    }

    public RubroEntity toEntity(RubroDto dto) {
        if (dto == null) {
            return null;
        }

        RubroEntity entity = new RubroEntity();
        entity.setIdRubro(dto.getIdRubro());
        entity.setNombreRubro(dto.getNombreRubro());
        entity.setCode(dto.getCode());
        entity.setDescripcion(dto.getDescripcion());
        entity.setEstado(dto.getEstado());
        return entity;
    }
}