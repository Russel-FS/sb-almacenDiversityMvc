package com.api.diversity.application.mappers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.api.diversity.application.dto.DetalleSalidaDto;
import com.api.diversity.domain.model.DetalleSalidaEntity;

@Component
public class DetalleSalidaMapper {

    public DetalleSalidaDto toDto(DetalleSalidaEntity entity) {
        if (entity == null) {
            return null;
        }

        DetalleSalidaDto dto = new DetalleSalidaDto();
        dto.setIdDetalleSalida(entity.getIdDetalleSalida());
        dto.setCantidad(entity.getCantidad());
        dto.setPrecioUnitario(entity.getPrecioUnitario());
        dto.setSubtotal(entity.getSubtotal());
        dto.setEstado(entity.getEstado());

        // Mapear salida
        if (entity.getSalida() != null) {
            dto.setSalidaId(entity.getSalida().getIdSalida());
        }

        // Mapear producto
        if (entity.getProducto() != null) {
            dto.setProductoId(entity.getProducto().getIdProducto());
            dto.setProductoNombre(entity.getProducto().getNombreProducto());
            dto.setProductoCodigo(entity.getProducto().getCodigoProducto());
            dto.setStockDisponible(entity.getProducto().getStockActual());

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

    public DetalleSalidaEntity toEntity(DetalleSalidaDto dto) {
        if (dto == null) {
            return null;
        }

        DetalleSalidaEntity entity = new DetalleSalidaEntity();
        entity.setIdDetalleSalida(dto.getIdDetalleSalida());
        entity.setCantidad(dto.getCantidad());
        entity.setPrecioUnitario(dto.getPrecioUnitario());
        entity.setSubtotal(dto.getSubtotal());
        entity.setEstado(dto.getEstado());
        return entity;
    }

    public List<DetalleSalidaDto> toDtoList(List<DetalleSalidaEntity> entities) {
        if (entities == null) {
            return null;
        }
        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<DetalleSalidaEntity> toEntityList(List<DetalleSalidaDto> dtos) {
        if (dtos == null) {
            return null;
        }
        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}