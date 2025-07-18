package com.api.diversity.application.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.api.diversity.domain.enums.EstadoEntrada;
import com.api.diversity.domain.enums.TipoDocumento;
import com.api.diversity.domain.enums.TipoEntrada;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EntradaDto {

    private Long idEntrada;
    private String numeroFactura;
    private TipoDocumento tipoDocumento;
    private TipoEntrada tipoEntrada;
    private Long idSalidaReferencia;
    private Long proveedorId;
    private String proveedorNombre;
    private String proveedorRuc;
    private String proveedorDireccion;
    private LocalDateTime fechaEntrada;
    private BigDecimal costoTotal;
    private EstadoEntrada estado;
    private Long usuarioRegistroId;
    private String usuarioRegistroNombre;
    private Long usuarioAprobacionId;
    private String usuarioAprobacionNombre;
    private LocalDateTime fechaAprobacion;
    private String observaciones;
    private List<DetalleEntradaDto> detalles;

    // para estadísticas
    private Integer totalProductos;
    private String estadoDescripcion;
}