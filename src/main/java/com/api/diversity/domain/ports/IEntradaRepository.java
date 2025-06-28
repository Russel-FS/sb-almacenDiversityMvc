package com.api.diversity.domain.ports;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.api.diversity.domain.model.EntradaEntity;
import com.api.diversity.domain.enums.EstadoEntrada;

public interface IEntradaRepository {

    EntradaEntity save(EntradaEntity entrada);

    Optional<EntradaEntity> findById(Long id);

    List<EntradaEntity> findAll();

    List<EntradaEntity> findByEstado(EstadoEntrada estado);

    List<EntradaEntity> findByFechaEntradaBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);

    List<EntradaEntity> findByProveedorId(Long proveedorId);

    List<EntradaEntity> findByUsuarioRegistroId(Long usuarioId);

    List<EntradaEntity> findByRubroId(Long rubroId);

    List<EntradaEntity> findTop10ByOrderByFechaEntradaDesc();

    boolean existsById(Long id);

    void deleteById(Long id);

    Long countByEstado(EstadoEntrada estado);

    Long countByFechaEntradaBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);
}