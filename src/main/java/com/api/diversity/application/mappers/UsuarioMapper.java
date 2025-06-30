package com.api.diversity.application.mappers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.api.diversity.application.dto.UsuarioDto;
import com.api.diversity.domain.model.UsuarioEntity;
import com.api.diversity.domain.model.RolEntity;
import com.api.diversity.domain.model.RubroEntity;
import com.api.diversity.domain.ports.IRolRepository;
import com.api.diversity.domain.ports.RubroRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UsuarioMapper {

    private final RubroMapper rubroMapper;
    private final UserRoleMapper userRoleMapper;
    private final IRolRepository rolRepository;
    private final RubroRepository rubroRepository;

    public UsuarioDto toDto(UsuarioEntity entity) {
        if (entity == null) {
            return null;
        }

        return UsuarioDto.builder()
                .idUsuario(entity.getIdUsuario())
                .nombreUsuario(entity.getNombreUsuario())
                .email(entity.getEmail())
                .nombreCompleto(entity.getNombreCompleto())
                .rubros(rubroMapper.toDtoList(entity.getRubrosActivos()))
                .urlImagen(entity.getUrlImagen())
                .publicId(entity.getPublicId())
                .contraseña(entity.getContraseña())
                .estado(entity.getEstado())
                .ultimoAcceso(entity.getUltimoAcceso())
                .fechaCreacion(entity.getFechaCreacion())
                .fechaModificacion(entity.getFechaModificacion())
                .roles(userRoleMapper.toDtoList(entity.getUserRoles()))
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
        entity.setUrlImagen(dto.getUrlImagen());
        entity.setPublicId(dto.getPublicId());
        entity.setContraseña(dto.getContraseña());
        entity.setEstado(dto.getEstado());
        entity.setUltimoAcceso(dto.getUltimoAcceso());
        entity.setFechaCreacion(dto.getFechaCreacion());
        entity.setFechaModificacion(dto.getFechaModificacion());

        // roles
        if (dto.getRoles() != null && !dto.getRoles().isEmpty()) {
            dto.getRoles().forEach(userRoleDto -> {
                if (userRoleDto.getRol() != null) {
                    RolEntity rol = rolRepository.findById(userRoleDto.getRol().getIdRol())
                            .orElseThrow(() -> new RuntimeException(
                                    "Rol no encontrado: " + userRoleDto.getRol().getIdRol()));
                    entity.addRole(rol);
                }
            });
        }

        // Mrubros
        if (dto.getRubros() != null && !dto.getRubros().isEmpty()) {
            dto.getRubros().forEach(rubroDto -> {
                RubroEntity rubro = rubroRepository.findById(rubroDto.getIdRubro())
                        .orElseThrow(() -> new RuntimeException("Rubro no encontrado: " + rubroDto.getIdRubro()));
                entity.addRubro(rubro);
            });
        }

        return entity;
    }

    public List<UsuarioDto> toDtoList(List<UsuarioEntity> entities) {
        if (entities == null) {
            return null;
        }
        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}