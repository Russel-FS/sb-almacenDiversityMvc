package com.api.diversity.application.service.interfaces;

import com.api.diversity.application.dto.DevolucionRequestDto;
import com.api.diversity.application.dto.EntradaDto;

public interface DevolucionService {
    EntradaDto procesarDevolucion(DevolucionRequestDto devolucionRequest);
}
