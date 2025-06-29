package com.api.diversity.application.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.api.diversity.domain.enums.EstadoUsuarioRubro;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioRubroDto {

    private Long idUsuarioRubro;
    private Long idUsuario;
    private String nombreUsuario;
    private Long idRubro;
    private String nombreRubro;
    private EstadoUsuarioRubro estado;
    private LocalDateTime fechaAsignacion;
}