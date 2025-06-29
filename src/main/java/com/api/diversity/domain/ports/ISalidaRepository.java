package com.api.diversity.domain.ports;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.api.diversity.domain.model.SalidaEntity;
import com.api.diversity.domain.enums.EstadoSalida;

public interface ISalidaRepository {

    SalidaEntity save(SalidaEntity salida);

    Optional<SalidaEntity> findById(Long id);

    List<SalidaEntity> findAll();

    List<SalidaEntity> findByEstado(EstadoSalida estado);

    List<SalidaEntity> findByFechaSalidaBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);

    List<SalidaEntity> findByClienteId(Long clienteId);

    List<SalidaEntity> findByUsuarioRegistroId(Long usuarioId);

    List<SalidaEntity> findByRubroId(Long rubroId);

    List<SalidaEntity> findTop10ByOrderByFechaSalidaDesc();

    boolean existsById(Long id);

    void deleteById(Long id);

    Long countByEstado(EstadoSalida estado);

    Long countByFechaSalidaBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);

    boolean existsByNumeroDocumento(String numeroDocumento);
}