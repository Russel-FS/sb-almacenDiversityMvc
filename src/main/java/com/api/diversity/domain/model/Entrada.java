package com.api.diversity.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Entrada {
    private Integer idEntrada;
    private LocalDateTime fechaEntrada;
    private BigDecimal costoTotal;
}