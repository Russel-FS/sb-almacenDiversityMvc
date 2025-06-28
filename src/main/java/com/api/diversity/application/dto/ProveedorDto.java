package com.api.diversity.application.dto;

import java.time.LocalDateTime;

import com.api.diversity.domain.enums.EstadoProveedor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProveedorDto {

    private Long idProveedor;
    private String razonSocial;
    private String ruc;
    private String direccion;
    private String telefono;
    private String email;
    private EstadoProveedor estado;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaModificacion;

    // Campos adicionales para la UI
    private String estadoDescripcion;
    private Integer totalEntradas;
    private String ultimaEntrada;
    private String valorTotalCompras;
}