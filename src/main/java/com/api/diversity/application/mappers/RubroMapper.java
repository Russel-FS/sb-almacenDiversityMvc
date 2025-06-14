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
                .publicId(entity.getPublicId())
                .imagenUrl(entity.getImagenUrl())
                .fechaCreacion(entity.getFechaCreacion())
                .fechaModificacion(entity.getFechaModificacion())
                .createdBy(entity.getCreatedBy() != null ? entity.getCreatedBy().getIdUsuario() : null)
                .updatedBy(entity.getUpdatedBy() != null ? entity.getUpdatedBy().getIdUsuario() : null)
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
        entity.setPublicId(dto.getPublicId());
        entity.setImagenUrl(dto.getImagenUrl());
        entity.setFechaCreacion(dto.getFechaCreacion());
        entity.setFechaModificacion(dto.getFechaModificacion());

        // No establecemos createdBy ni updatedBy aquí ya que son entidades
        // UsuarioEntity
        // y necesitarían ser cargadas desde la base de datos

        return entity;
    }
}