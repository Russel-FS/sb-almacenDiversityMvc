package com.api.diversity.application.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.api.diversity.domain.enums.EstadoSalida;
import com.api.diversity.domain.enums.TipoCliente;
import com.api.diversity.domain.enums.TipoDocumento;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalidaDto {

    private Long idSalida;
    private String numeroDocumento;
    private TipoDocumento tipoDocumento;
    private Long clienteId;
    private String clienteNombre;
    private String clienteDni;
    private TipoCliente clienteTipo;
    private LocalDateTime fechaSalida;
    private String motivoSalida;
    private BigDecimal totalVenta;
    private EstadoSalida estado;
    private Long usuarioRegistroId;
    private String usuarioRegistroNombre;
    private Long usuarioAprobacionId;
    private String usuarioAprobacionNombre;
    private LocalDateTime fechaAprobacion;
    private String observaciones;
    private List<DetalleSalidaDto> detalles;

    // Campos adicionales para estadísticas
    private Integer totalProductos;
    private String estadoDescripcion;
    private String tipoDocumentoDescripcion;
}