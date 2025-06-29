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
public class EntradaFormDto {

    private String numeroFactura;
    private TipoDocumento tipoDocumento;
    private Long proveedorId;
    private LocalDate fechaEntrada;
    private String observaciones;
    private List<ProductoEntradaDto> productos;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductoEntradaDto {
        private Long productoId;
        private Integer cantidad;
        private BigDecimal precioUnitario;
    }
}