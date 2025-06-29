package com.api.diversity.application.dto;

import java.time.LocalDateTime;

import com.api.diversity.domain.enums.EstadoCliente;
import com.api.diversity.domain.enums.TipoCliente;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClienteDto {

    private Long idCliente;
    private TipoCliente tipoCliente;
    private String nombreCompleto;
    private String razonSocial;
    private String ruc;
    private String dni;
    private String direccion;
    private String telefono;
    private String email;
    private EstadoCliente estado;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaModificacion;
    private Long createdBy;
    private Long updatedBy;

    // Campos adicionales para la UI
    private String estadoDescripcion;
    private String tipoClienteDescripcion;
    private Integer totalCompras;
    private String ultimaCompra;
}