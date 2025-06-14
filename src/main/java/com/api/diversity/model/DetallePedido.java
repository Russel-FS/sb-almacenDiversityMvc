package com.api.diversity.model;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class DetallePedido {
    private Integer idDetalle;
    private Integer pedidoId;
    private String productoId;
    private Integer cantidad;
    private BigDecimal subtotal;
}