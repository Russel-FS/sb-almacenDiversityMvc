package com.api.diversity.model;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class DetalleEntrada {
    private Integer idDetalleEntrada;
    private Integer entradaId;
    private String productoId;
    private Integer cantidad;
    private BigDecimal subtotal;
}