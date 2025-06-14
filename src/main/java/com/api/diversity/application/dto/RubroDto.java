package com.api.diversity.application.dto;

import com.api.diversity.domain.enums.EstadoRubro;
import com.api.diversity.domain.enums.NombreRubro;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RubroDto {
    private Long idRubro;
    private String nombreRubro;
    private NombreRubro code;
    private String descripcion;
    private EstadoRubro estado;
}