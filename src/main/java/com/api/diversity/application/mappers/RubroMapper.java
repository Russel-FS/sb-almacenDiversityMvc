package com.api.diversity.application.mappers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.api.diversity.application.dto.RubroDto;
import com.api.diversity.domain.model.RubroEntity;
import com.api.diversity.domain.enums.EstadoRubro;

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
                .publicId(entity.getPublicId())
                .imagenUrl(entity.getImagenUrl())
                .fechaCreacion(entity.getFechaCreacion())
                .fechaModificacion(entity.getFechaModificacion())
                .build();
    }

    public RubroEntity toEntity(RubroDto dto) {
        if (dto == null) {
            return null;
        }

        RubroEntity entity = new RubroEntity();
        entity.setIdRubro(dto.getIdRubro());
        entity.setNombreRubro(dto.getNombreRubro() != null ? dto.getNombreRubro() : "");
        entity.setCode(dto.getCode());
        entity.setDescripcion(dto.getDescripcion());
        entity.setEstado(dto.getEstado() != null ? dto.getEstado() : EstadoRubro.Activo);
        entity.setPublicId(dto.getPublicId());
        entity.setImagenUrl(dto.getImagenUrl());
        entity.setFechaCreacion(dto.getFechaCreacion());
        entity.setFechaModificacion(dto.getFechaModificacion());
        return entity;
    }

    public List<RubroDto> toDtoList(List<RubroEntity> entities) {
        if (entities == null) {
            return null;
        }
        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}