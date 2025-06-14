package com.api.diversity.application.dto;

import java.time.LocalDateTime;

import com.api.diversity.domain.enums.EstadoCategoria;

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
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaModificacion;
    private UsuarioDto createdBy;
    private UsuarioDto updatedBy;
}