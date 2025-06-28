package com.api.diversity.application.dto;

import java.time.LocalDateTime;

import com.api.diversity.domain.enums.EstadoUserRole;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRoleDto {

    private Long idUserRole;
    private RolDto rol;
    private EstadoUserRole estado;
    private LocalDateTime fechaAsignacion;
    private LocalDateTime fechaModificacion;
}