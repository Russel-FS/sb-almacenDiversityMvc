package com.api.diversity.application.service.interfaces;

import com.api.diversity.application.dto.DevolucionRequestDto;
import com.api.diversity.application.dto.EntradaDto;
import com.api.diversity.application.dto.SalidaDto;

public interface DevolucionService {
    EntradaDto procesarDevolucion(DevolucionRequestDto devolucionRequest);

    SalidaDto buscarSalidaPorIdONumeroDocumento(Long idSalida, String numeroDocumento);
}
