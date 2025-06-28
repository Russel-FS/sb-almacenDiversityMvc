package com.api.diversity.application.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.api.diversity.domain.enums.TipoDocumento;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalidaFormDto {

    private String numeroDocumento;
    private TipoDocumento tipoDocumento;
    private Long clienteId;
    private LocalDate fechaSalida;
    private String motivoSalida;
    private String observaciones;
    private List<ProductoFormDto> productos;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductoFormDto {
        private Long productoId;
        private String productoNombre;
        private String productoCodigo;
        private Integer cantidad;
        private BigDecimal precioUnitario;
        private BigDecimal subtotal;
        private Integer stockDisponible;
    }
}