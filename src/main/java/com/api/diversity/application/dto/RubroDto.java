package com.api.diversity.application.dto;

import com.api.diversity.domain.model.RubroEntity.EstadoRubro;
import com.api.diversity.domain.model.RubroEntity.NombreRubro;

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
    private NombreRubro nombreRubro;
    private String descripcion;
    private EstadoRubro estado;
}