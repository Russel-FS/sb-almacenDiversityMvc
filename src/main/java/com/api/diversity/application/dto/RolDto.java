package com.api.diversity.application.dto;

import java.time.LocalDateTime;

import com.api.diversity.domain.enums.EstadoRol;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RolDto {
    private Long idRol;
    private String nombreRol;
    private String descripcion;
    private EstadoRol estado;
    private LocalDateTime fechaCreacion;
}