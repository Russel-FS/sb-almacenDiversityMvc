package com.api.diversity.model;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class DetalleSalida {
    private Integer idDetalleSalida;
    private Integer salidaId;
    private String productoId;
    private Integer cantidad;
    private BigDecimal subtotal;
}