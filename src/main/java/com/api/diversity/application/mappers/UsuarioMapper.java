package com.api.diversity.application.mappers;

import org.springframework.stereotype.Component;

import com.api.diversity.application.dto.UsuarioDto;
import com.api.diversity.domain.model.UsuarioEntity;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UsuarioMapper {

    private final RolMapper rolMapper;

    public UsuarioDto toDto(UsuarioEntity entity) {
        if (entity == null) {
            return null;
        }

        return UsuarioDto.builder()
                .idUsuario(entity.getIdUsuario())
                .nombreUsuario(entity.getNombreUsuario())
                .email(entity.getEmail())
                .nombreCompleto(entity.getNombreCompleto())
                .rol(rolMapper.toDto(entity.getRol()))
                .urlImagen(entity.getUrlImagen())
                .publicId(entity.getPublicId())
                .contraseña(entity.getContraseña())
                .estado(entity.getEstado())
                .ultimoAcceso(entity.getUltimoAcceso())
                .fechaCreacion(entity.getFechaCreacion())
                .fechaModificacion(entity.getFechaModificacion())
                .build();
    }

    public UsuarioEntity toEntity(UsuarioDto dto) {
        if (dto == null) {
            return null;
        }

        UsuarioEntity entity = new UsuarioEntity();
        entity.setIdUsuario(dto.getIdUsuario());
        entity.setNombreUsuario(dto.getNombreUsuario());
        entity.setEmail(dto.getEmail());
        entity.setNombreCompleto(dto.getNombreCompleto());
        entity.setRol(rolMapper.toEntity(dto.getRol()));
        entity.setUrlImagen(dto.getUrlImagen());
        entity.setPublicId(dto.getPublicId());
        entity.setContraseña(dto.getContraseña());
        entity.setEstado(dto.getEstado());
        entity.setUltimoAcceso(dto.getUltimoAcceso());
        entity.setFechaCreacion(dto.getFechaCreacion());
        entity.setFechaModificacion(dto.getFechaModificacion());
        return entity;
    }
}