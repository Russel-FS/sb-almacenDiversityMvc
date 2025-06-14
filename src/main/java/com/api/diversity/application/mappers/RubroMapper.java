package com.api.diversity.application.mappers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.api.diversity.application.dto.RubroDto;
import com.api.diversity.domain.model.RubroEntity;
import com.api.diversity.domain.repository.UsuarioRepository;

@Component
public class RubroMapper {

    @Autowired
    private UsuarioRepository usuarioRepository;

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

        // Establecemos el usuario creador
        if (dto.getCreatedBy() != null) {
            usuarioRepository.findById(dto.getCreatedBy())
                    .ifPresent(entity::setCreatedBy);
        }

        // Establecemos el usuario que modificó
        if (dto.getUpdatedBy() != null) {
            usuarioRepository.findById(dto.getUpdatedBy())
                    .ifPresent(entity::setUpdatedBy);
        }

        return entity;
    }
}