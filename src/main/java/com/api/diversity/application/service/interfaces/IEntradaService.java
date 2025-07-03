package com.api.diversity.application.service.interfaces;

import java.time.LocalDateTime;
import java.util.List;

import com.api.diversity.application.dto.EntradaDto;
import com.api.diversity.domain.enums.EstadoEntrada;
import com.api.diversity.domain.enums.TipoDocumento;
import com.api.diversity.domain.enums.TipoRubro;

public interface IEntradaService {

    EntradaDto save(EntradaDto entradaDto);

    EntradaDto update(EntradaDto entradaDto);

    void deleteById(Long id);

    EntradaDto findById(Long id);

    List<EntradaDto> findAll();

    List<EntradaDto> findByEstado(EstadoEntrada estado);

    List<EntradaDto> findByFechaEntradaBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);

    List<EntradaDto> findByProveedorId(Long proveedorId);

    List<EntradaDto> findByUsuarioRegistroId(Long usuarioId);

    List<EntradaDto> findByRubroId(Long rubroId);

    List<EntradaDto> findTop10ByOrderByFechaEntradaDesc();

    boolean existsById(Long id);

    Long countByEstado(EstadoEntrada estado);

    Long countByFechaEntradaBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);

    // metodos para el kardex
    EntradaDto aprobarEntrada(Long id, Long usuarioAprobacionId);

    EntradaDto anularEntrada(Long id, String motivo);

    String generarNumeroDocumento(TipoDocumento tipoDocumento);

    boolean existsByNumeroFactura(String numeroFactura);

    /**
     * Obtiene las 10 últimas entradas filtradas por TipoRubro, ordenadas por fecha
     * descendente.
     */
    List<EntradaDto> findTop10ByTipoRubroOrderByFechaEntradaDesc(TipoRubro tipoRubro);
}