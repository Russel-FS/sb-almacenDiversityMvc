package com.api.diversity.application.dto;

import java.math.BigDecimal;

import com.api.diversity.domain.enums.EstadoDetalleEntrada;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetalleEntradaDto {

    private Long idDetalleEntrada;
    private Long entradaId;
    private Long productoId;
    private String productoNombre;
    private String productoCodigo;
    private Integer cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;
    private EstadoDetalleEntrada estado;

    // Campos adicionales para la UI
    private String estadoDescripcion;
    private String categoriaNombre;
    private String rubroNombre;
}