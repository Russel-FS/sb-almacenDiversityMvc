package com.api.diversity.application.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DevolucionRequestDto {
    private Long idSalidaOriginal;
    private String motivoDevolucion;
    private List<DetalleDevolucionDto> productosDevueltos;
}
