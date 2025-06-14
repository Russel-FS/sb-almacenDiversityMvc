package com.api.diversity.domain.model;

import lombok.Data;

@Data
public class Cliente {
    private Integer idCliente;
    private String nombre;
    private String telefono;
    private String direccion;
}