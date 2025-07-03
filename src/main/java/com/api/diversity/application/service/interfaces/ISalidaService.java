package com.api.diversity.application.service.interfaces;

import java.time.LocalDateTime;
import java.util.List;

import com.api.diversity.application.dto.SalidaDto;
import com.api.diversity.domain.enums.EstadoSalida;
import com.api.diversity.domain.enums.TipoDocumento;
import com.api.diversity.domain.enums.TipoRubro;

public interface ISalidaService {

    SalidaDto save(SalidaDto salidaDto);

    SalidaDto update(SalidaDto salidaDto);

    void deleteById(Long id);

    SalidaDto findById(Long id);

    List<SalidaDto> findAll();

    List<SalidaDto> findByEstado(EstadoSalida estado);

    List<SalidaDto> findByFechaSalidaBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);

    List<SalidaDto> findByClienteId(Long clienteId);

    List<SalidaDto> findByUsuarioRegistroId(Long usuarioId);

    List<SalidaDto> findByRubroId(Long rubroId);

    List<SalidaDto> findTop10ByOrderByFechaSalidaDesc();

    boolean existsById(Long id);

    Long countByEstado(EstadoSalida estado);

    Long countByFechaSalidaBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);

    // Métodos específicos para el Kardex
    SalidaDto aprobarSalida(Long id, Long usuarioAprobacionId);

    SalidaDto anularSalida(Long id, String motivo);

    String generarNumeroDocumento(TipoDocumento tipoDocumento);

    boolean existsByNumeroDocumento(String numeroDocumento);

    boolean validarStockDisponible(Long productoId, Integer cantidad);

    /**
     * Obtiene las 10 últimas salidas filtradas por TipoRubro, ordenadas por fecha
     * descendente.
     */
    List<SalidaDto> findTop10ByTipoRubroOrderByFechaSalidaDesc(TipoRubro tipoRubro);
}