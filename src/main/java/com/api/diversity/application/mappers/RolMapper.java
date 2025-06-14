package com.api.diversity.application.mappers;

import org.springframework.stereotype.Component;

import com.api.diversity.application.dto.RolDto;
import com.api.diversity.domain.model.RolEntity;

@Component
public class RolMapper {

    public RolDto toDto(RolEntity entity) {
        if (entity == null) {
            return null;
        }

        return RolDto.builder()
                .idRol(entity.getIdRol())
                .nombreRol(entity.getNombreRol())
                .descripcion(entity.getDescripcion())
                .estado(entity.getEstado())
                .fechaCreacion(entity.getFechaCreacion())
                .build();
    }

    public RolEntity toEntity(RolDto dto) {
        if (dto == null) {
            return null;
        }

        RolEntity entity = new RolEntity();
        entity.setIdRol(dto.getIdRol());
        entity.setNombreRol(dto.getNombreRol());
        entity.setDescripcion(dto.getDescripcion());
        entity.setEstado(dto.getEstado());
        entity.setFechaCreacion(dto.getFechaCreacion());
        return entity;
    }
}