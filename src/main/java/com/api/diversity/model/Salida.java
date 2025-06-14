package com.api.diversity.model;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Salida {
    private Integer idSalida;
    private LocalDateTime fechaSalida;
    private String motivoSalida;
}