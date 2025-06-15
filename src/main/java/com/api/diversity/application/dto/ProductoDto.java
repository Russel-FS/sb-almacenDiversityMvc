package com.api.diversity.application.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.api.diversity.domain.enums.EstadoProducto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductoDto {
    private Long idProducto;
    private String codigoProducto;
    private String nombreProducto;
    private String descripcion;
    private CategoriaDto categoria;
    private BigDecimal precioCompra;
    private BigDecimal precioVenta;
    private Integer stockActual;
    private Integer stockMinimo;
    private Integer stockMaximo;
    private String urlImagen;
    private String publicId;
    private EstadoProducto estado;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaModificacion;
    private UsuarioDto createdBy;
    private UsuarioDto updatedBy;
}