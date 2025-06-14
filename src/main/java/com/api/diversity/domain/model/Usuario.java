package com.api.diversity.domain.model;

import lombok.Data;

@Data
public class Usuario {
    private Integer idUsuario;
    private String nombreUsuario;
    private String rol;
    private String contraseña;
}