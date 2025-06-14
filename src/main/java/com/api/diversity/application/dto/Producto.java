package com.api.diversity.application.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Producto {
    private String idProducto;
    private String nombre;
    private String descripcion;
    private CategoriaDto categoria;
    private Integer stock;
    private String urlImagen;
    private String publicId;
    private BigDecimal precioUnitario;
    private LocalDateTime fechaRegistro;
}