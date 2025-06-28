package com.api.diversity.application.mappers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.api.diversity.application.dto.EntradaDto;
import com.api.diversity.domain.model.EntradaEntity;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EntradaMapper {

    private final DetalleEntradaMapper detalleEntradaMapper;

    public EntradaDto toDto(EntradaEntity entity) {
        if (entity == null) {
            return null;
        }

        EntradaDto dto = new EntradaDto();
        dto.setIdEntrada(entity.getIdEntrada());
        dto.setNumeroFactura(entity.getNumeroFactura());
        dto.setFechaEntrada(entity.getFechaEntrada());
        dto.setCostoTotal(entity.getCostoTotal());
        dto.setEstado(entity.getEstado());
        dto.setFechaAprobacion(entity.getFechaAprobacion());
        dto.setObservaciones(entity.getObservaciones());

        // Mapear proveedor
        if (entity.getProveedor() != null) {
            dto.setProveedorId(entity.getProveedor().getIdProveedor());
            dto.setProveedorNombre(entity.getProveedor().getRazonSocial());
        }

        // Mapear usuario registro
        if (entity.getUsuarioRegistro() != null) {
            dto.setUsuarioRegistroId(entity.getUsuarioRegistro().getIdUsuario());
            dto.setUsuarioRegistroNombre(entity.getUsuarioRegistro().getNombreCompleto());
        }

        // Mapear usuario aprobación
        if (entity.getUsuarioAprobacion() != null) {
            dto.setUsuarioAprobacionId(entity.getUsuarioAprobacion().getIdUsuario());
            dto.setUsuarioAprobacionNombre(entity.getUsuarioAprobacion().getNombreCompleto());
        }

        // Mapear detalles
        if (entity.getDetalles() != null) {
            dto.setDetalles(entity.getDetalles().stream()
                    .map(detalleEntradaMapper::toDto)
                    .collect(Collectors.toList()));
            dto.setTotalProductos(entity.getDetalles().size());
        }

        // Mapear descripción del estado
        if (entity.getEstado() != null) {
            dto.setEstadoDescripcion(entity.getEstado().name());
        }

        return dto;
    }

    public EntradaEntity toEntity(EntradaDto dto) {
        if (dto == null) {
            return null;
        }

        EntradaEntity entity = new EntradaEntity();
        entity.setIdEntrada(dto.getIdEntrada());
        entity.setNumeroFactura(dto.getNumeroFactura());
        entity.setFechaEntrada(dto.getFechaEntrada());
        entity.setCostoTotal(dto.getCostoTotal());
        entity.setEstado(dto.getEstado());
        entity.setFechaAprobacion(dto.getFechaAprobacion());
        entity.setObservaciones(dto.getObservaciones());
        return entity;
    }

    public List<EntradaDto> toDtoList(List<EntradaEntity> entities) {
        if (entities == null) {
            return null;
        }
        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<EntradaEntity> toEntityList(List<EntradaDto> dtos) {
        if (dtos == null) {
            return null;
        }
        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}