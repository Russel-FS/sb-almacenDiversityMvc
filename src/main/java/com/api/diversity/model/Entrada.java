package com.api.diversity.model;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Entrada {
    private Integer idEntrada;
    private LocalDateTime fechaEntrada;
    private BigDecimal costoTotal;
}