package com.api.diversity.application.mappers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.api.diversity.application.dto.ClienteDto;
import com.api.diversity.domain.model.ClienteEntity;

@Component
public class ClienteMapper {

    public ClienteDto toDto(ClienteEntity entity) {
        if (entity == null) {
            return null;
        }

        ClienteDto dto = new ClienteDto();
        dto.setIdCliente(entity.getIdCliente());
        dto.setTipoCliente(entity.getTipoCliente());
        dto.setNombreCompleto(entity.getNombreCompleto());
        dto.setRazonSocial(entity.getRazonSocial());
        dto.setRuc(entity.getRuc());
        dto.setDni(entity.getDni());
        dto.setDireccion(entity.getDireccion());
        dto.setTelefono(entity.getTelefono());
        dto.setEmail(entity.getEmail());
        dto.setEstado(entity.getEstado());
        dto.setFechaCreacion(entity.getFechaCreacion());
        dto.setFechaModificacion(entity.getFechaModificacion());
        dto.setCreatedBy(entity.getCreatedBy() != null ? entity.getCreatedBy().getIdUsuario() : null);
        dto.setUpdatedBy(entity.getUpdatedBy() != null ? entity.getUpdatedBy().getIdUsuario() : null);

        if (entity.getEstado() != null) {
            dto.setEstadoDescripcion(entity.getEstado().name());
        }
        if (entity.getTipoCliente() != null) {
            dto.setTipoClienteDescripcion(entity.getTipoCliente().name());
        }

        return dto;
    }

    public ClienteEntity toEntity(ClienteDto dto) {
        if (dto == null) {
            return null;
        }

        ClienteEntity entity = new ClienteEntity();
        entity.setIdCliente(dto.getIdCliente());
        entity.setTipoCliente(dto.getTipoCliente());
        entity.setNombreCompleto(dto.getNombreCompleto());
        entity.setRazonSocial(dto.getRazonSocial());
        entity.setRuc(dto.getRuc());
        entity.setDni(dto.getDni());
        entity.setDireccion(dto.getDireccion());
        entity.setTelefono(dto.getTelefono());
        entity.setEmail(dto.getEmail());
        entity.setEstado(dto.getEstado());
        entity.setFechaCreacion(dto.getFechaCreacion());
        entity.setFechaModificacion(dto.getFechaModificacion());

        return entity;
    }

    public List<ClienteDto> toDtoList(List<ClienteEntity> entities) {
        if (entities == null) {
            return null;
        }
        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<ClienteEntity> toEntityList(List<ClienteDto> dtos) {
        if (dtos == null) {
            return null;
        }
        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}