package com.api.diversity.domain.model;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class DetallePedido {
    private Integer idDetalle;
    private Integer pedidoId;
    private String productoId;
    private Integer cantidad;
    private BigDecimal subtotal;
}