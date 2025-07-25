package com.api.diversity.application.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.api.diversity.domain.enums.EstadoUsuario;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioDto {
    private Long idUsuario;
    private String nombreUsuario;
    private String email;
    private String nombreCompleto;
    private List<RubroDto> rubros;
    private String urlImagen;
    private String publicId;
    private String contraseña;
    private EstadoUsuario estado;
    private LocalDateTime ultimoAcceso;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaModificacion;
    private List<UserRoleDto> roles;
    private List<Long> rolesIds;
    private List<Long> rubrosIds;
}