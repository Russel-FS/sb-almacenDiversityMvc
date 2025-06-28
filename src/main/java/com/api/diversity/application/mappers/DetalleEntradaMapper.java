package com.api.diversity.application.mappers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.api.diversity.application.dto.DetalleEntradaDto;
import com.api.diversity.domain.model.DetalleEntradaEntity;

@Component
public class DetalleEntradaMapper {

    public DetalleEntradaDto toDto(DetalleEntradaEntity entity) {
        if (entity == null) {
            return null;
        }

        DetalleEntradaDto dto = new DetalleEntradaDto();
        dto.setIdDetalleEntrada(entity.getIdDetalleEntrada());
        dto.setCantidad(entity.getCantidad());
        dto.setPrecioUnitario(entity.getPrecioUnitario());
        dto.setSubtotal(entity.getSubtotal());
        dto.setEstado(entity.getEstado());

        // Mapear entrada
        if (entity.getEntrada() != null) {
            dto.setEntradaId(entity.getEntrada().getIdEntrada());
        }

        // Mapear producto
        if (entity.getProducto() != null) {
            dto.setProductoId(entity.getProducto().getIdProducto());
            dto.setProductoNombre(entity.getProducto().getNombreProducto());
            dto.setProductoCodigo(entity.getProducto().getCodigoProducto());

            // Mapear categoría y rubro
            if (entity.getProducto().getCategoria() != null) {
                dto.setCategoriaNombre(entity.getProducto().getCategoria().getNombreCategoria());

                if (entity.getProducto().getCategoria().getRubro() != null) {
                    dto.setRubroNombre(entity.getProducto().getCategoria().getRubro().getNombreRubro());
                }
            }
        }

        // Mapear descripción del estado
        if (entity.getEstado() != null) {
            dto.setEstadoDescripcion(entity.getEstado().name());
        }

        return dto;
    }

    public DetalleEntradaEntity toEntity(DetalleEntradaDto dto) {
        if (dto == null) {
            return null;
        }

        DetalleEntradaEntity entity = new DetalleEntradaEntity();
        entity.setIdDetalleEntrada(dto.getIdDetalleEntrada());
        entity.setCantidad(dto.getCantidad());
        entity.setPrecioUnitario(dto.getPrecioUnitario());
        entity.setSubtotal(dto.getSubtotal());
        entity.setEstado(dto.getEstado());

        // Las relaciones se establecen en el servicio

        return entity;
    }

    public List<DetalleEntradaDto> toDtoList(List<DetalleEntradaEntity> entities) {
        if (entities == null) {
            return null;
        }
        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<DetalleEntradaEntity> toEntityList(List<DetalleEntradaDto> dtos) {
        if (dtos == null) {
            return null;
        }
        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}