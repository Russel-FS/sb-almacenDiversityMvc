package com.api.diversity.application.mappers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.api.diversity.application.dto.ProveedorDto;
import com.api.diversity.domain.model.ProveedorEntity;

@Component
public class ProveedorMapper {

    public ProveedorDto toDto(ProveedorEntity entity) {
        if (entity == null) {
            return null;
        }

        ProveedorDto dto = new ProveedorDto();
        dto.setIdProveedor(entity.getIdProveedor());
        dto.setRazonSocial(entity.getRazonSocial());
        dto.setRuc(entity.getRuc());
        dto.setDireccion(entity.getDireccion());
        dto.setTelefono(entity.getTelefono());
        dto.setEmail(entity.getEmail());
        dto.setRepresentanteLegal(entity.getRepresentanteLegal());
        dto.setTipoDocumento(entity.getTipoDocumento());
        dto.setEstado(entity.getEstado());
        dto.setFechaCreacion(entity.getFechaCreacion());
        dto.setFechaModificacion(entity.getFechaModificacion());

        // Mapear descripción del estado
        if (entity.getEstado() != null) {
            dto.setEstadoDescripcion(entity.getEstado().name());
        }

        return dto;
    }

    public ProveedorEntity toEntity(ProveedorDto dto) {
        if (dto == null) {
            return null;
        }

        ProveedorEntity entity = new ProveedorEntity();
        entity.setIdProveedor(dto.getIdProveedor());
        entity.setRazonSocial(dto.getRazonSocial());
        entity.setRuc(dto.getRuc());
        entity.setDireccion(dto.getDireccion());
        entity.setTelefono(dto.getTelefono());
        entity.setEmail(dto.getEmail());
        entity.setRepresentanteLegal(dto.getRepresentanteLegal());
        entity.setTipoDocumento(dto.getTipoDocumento());
        entity.setEstado(dto.getEstado());
        entity.setFechaCreacion(dto.getFechaCreacion());
        entity.setFechaModificacion(dto.getFechaModificacion());

        return entity;
    }

    public List<ProveedorDto> toDtoList(List<ProveedorEntity> entities) {
        if (entities == null) {
            return null;
        }
        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<ProveedorEntity> toEntityList(List<ProveedorDto> dtos) {
        if (dtos == null) {
            return null;
        }
        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}