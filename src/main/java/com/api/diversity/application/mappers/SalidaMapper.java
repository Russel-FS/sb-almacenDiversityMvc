package com.api.diversity.application.mappers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.api.diversity.application.dto.SalidaDto;
import com.api.diversity.domain.model.SalidaEntity;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SalidaMapper {

    private final DetalleSalidaMapper detalleSalidaMapper;

    public SalidaDto toDto(SalidaEntity entity) {
        if (entity == null) {
            return null;
        }

        SalidaDto dto = new SalidaDto();
        dto.setIdSalida(entity.getIdSalida());
        dto.setNumeroDocumento(entity.getNumeroDocumento());
        dto.setTipoDocumento(entity.getTipoDocumento());
        dto.setFechaSalida(entity.getFechaSalida());
        dto.setMotivoSalida(entity.getMotivoSalida());
        dto.setTotalVenta(entity.getTotalVenta());
        dto.setEstado(entity.getEstado());
        dto.setFechaAprobacion(entity.getFechaAprobacion());
        dto.setObservaciones(entity.getObservaciones());

        // Mapear cliente
        if (entity.getCliente() != null) {
            dto.setClienteId(entity.getCliente().getIdCliente());
            dto.setClienteNombre(entity.getCliente().getNombreCompleto());
            dto.setClienteDni(entity.getCliente().getDni());
            dto.setClienteTipo(
                    entity.getCliente().getTipoCliente() != null ? entity.getCliente().getTipoCliente().name() : null);
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
                    .map(detalleSalidaMapper::toDto)
                    .collect(Collectors.toList()));
            dto.setTotalProductos(entity.getDetalles().size());
        }

        // Mapear descripciones
        if (entity.getEstado() != null) {
            dto.setEstadoDescripcion(entity.getEstado().name());
        }
        if (entity.getTipoDocumento() != null) {
            dto.setTipoDocumentoDescripcion(entity.getTipoDocumento().getDescripcion());
        }

        return dto;
    }

    public SalidaEntity toEntity(SalidaDto dto) {
        if (dto == null) {
            return null;
        }

        SalidaEntity entity = new SalidaEntity();
        entity.setIdSalida(dto.getIdSalida());
        entity.setNumeroDocumento(dto.getNumeroDocumento());
        entity.setTipoDocumento(dto.getTipoDocumento());
        entity.setFechaSalida(dto.getFechaSalida());
        entity.setMotivoSalida(dto.getMotivoSalida());
        entity.setTotalVenta(dto.getTotalVenta());
        entity.setEstado(dto.getEstado());
        entity.setFechaAprobacion(dto.getFechaAprobacion());
        entity.setObservaciones(dto.getObservaciones());
        return entity;
    }

    public List<SalidaDto> toDtoList(List<SalidaEntity> entities) {
        if (entities == null) {
            return null;
        }
        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<SalidaEntity> toEntityList(List<SalidaDto> dtos) {
        if (dtos == null) {
            return null;
        }
        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}