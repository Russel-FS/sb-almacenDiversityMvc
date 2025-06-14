package com.api.diversity.application.dto;

import com.api.diversity.domain.enums.EstadoRubro;

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
    private String code;
    private String descripcion;
    private EstadoRubro estado;
    private String publicId;
    private String imagenUrl;
    private java.time.LocalDateTime fechaCreacion;
    private java.time.LocalDateTime fechaModificacion;
    private Long createdBy;
    private Long updatedBy;
}