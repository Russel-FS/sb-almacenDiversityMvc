package com.api.diversity.model;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Producto {
    private Integer idProducto;
    private String nombre;
    private String descripcion;
    private Integer categoriaId;
    private Integer stock;
    private BigDecimal precioUnitario;
    private LocalDateTime fechaRegistro;
}