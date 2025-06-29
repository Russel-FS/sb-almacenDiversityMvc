package com.api.diversity.application.mappers;

import org.springframework.stereotype.Component;

import com.api.diversity.application.dto.UsuarioRubroDto;
import com.api.diversity.domain.model.UsuarioRubroEntity;

@Component
public class UsuarioRubroMapper {

    public UsuarioRubroDto toDto(UsuarioRubroEntity entity) {
        if (entity == null) {
            return null;
        }

        return new UsuarioRubroDto(
                entity.getIdUsuarioRubro(),
                entity.getUsuario() != null ? entity.getUsuario().getIdUsuario() : null,
                entity.getUsuario() != null ? entity.getUsuario().getNombreCompleto() : null,
                entity.getRubro() != null ? entity.getRubro().getIdRubro() : null,
                entity.getRubro() != null ? entity.getRubro().getNombreRubro() : null,
                entity.getEstado(),
                entity.getFechaAsignacion());
    }

    public UsuarioRubroEntity toEntity(UsuarioRubroDto dto) {
        if (dto == null) {
            return null;
        }

        UsuarioRubroEntity entity = new UsuarioRubroEntity();
        entity.setIdUsuarioRubro(dto.getIdUsuarioRubro());
        entity.setEstado(dto.getEstado());
        entity.setFechaAsignacion(dto.getFechaAsignacion());

        return entity;
    }
}