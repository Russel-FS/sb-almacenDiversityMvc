package com.api.diversity.application.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetalleDevolucionDto {
    private Long idProducto;
    private Integer cantidad;
    private BigDecimal precioUnitario; // Precio al que se devuelve, podría ser el de venta original
}
