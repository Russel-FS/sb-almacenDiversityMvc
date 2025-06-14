package com.api.diversity.application.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.api.diversity.domain.model.ProductoEntity.EstadoProducto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Producto {
    private Long idProducto;
    private String codigoProducto;
    private String nombreProducto;
    private String descripcion;
    private CategoriaDto categoria;
    private RubroDto rubro;
    private BigDecimal precioCompra;
    private BigDecimal precioVenta;
    private Integer stockActual;
    private Integer stockMinimo;
    private Integer stockMaximo;
    private EstadoProducto estado;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaModificacion;
}