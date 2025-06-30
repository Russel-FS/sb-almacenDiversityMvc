package com.api.diversity.application.dto;

import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SunatResponseDto {
    private boolean autorizado;
    private String codigoAutorizacion;
    private String mensaje;
    private String codigoError;
    private String xmlRespuesta;
    private String hash;
}