package com.api.diversity.application.dto;

import com.api.diversity.domain.model.CategoryEntity.EstadoCategoria;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoriaDto {
    private Long idCategoria;
    private RubroDto rubro;
    private String nombreCategoria;
    private String descripcion;
    private EstadoCategoria estado;
}