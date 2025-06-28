package com.api.diversity.application.mappers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.api.diversity.application.dto.UserRoleDto;
import com.api.diversity.domain.model.UserRoleEntity;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserRoleMapper {

    private final RolMapper rolMapper;

    public UserRoleDto toDto(UserRoleEntity entity) {
        if (entity == null) {
            return null;
        }

        return UserRoleDto.builder()
                .idUserRole(entity.getIdUserRole())
                .rol(rolMapper.toDto(entity.getRol()))
                .estado(entity.getEstado())
                .fechaAsignacion(entity.getFechaAsignacion())
                .fechaModificacion(entity.getFechaModificacion())
                .build();
    }

    public UserRoleEntity toEntity(UserRoleDto dto) {
        if (dto == null) {
            return null;
        }

        UserRoleEntity entity = new UserRoleEntity();
        entity.setIdUserRole(dto.getIdUserRole());
        entity.setRol(rolMapper.toEntity(dto.getRol()));
        entity.setEstado(dto.getEstado());
        entity.setFechaAsignacion(dto.getFechaAsignacion());
        entity.setFechaModificacion(dto.getFechaModificacion());
        return entity;
    }

    public List<UserRoleDto> toDtoList(List<UserRoleEntity> entities) {
        if (entities == null) {
            return null;
        }
        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<UserRoleEntity> toEntityList(List<UserRoleDto> dtos) {
        if (dtos == null) {
            return null;
        }
        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}