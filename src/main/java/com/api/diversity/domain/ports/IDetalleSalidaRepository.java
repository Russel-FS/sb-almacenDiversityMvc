package com.api.diversity.domain.ports;

import java.util.List;
import java.util.Optional;

import com.api.diversity.domain.model.DetalleSalidaEntity;
import com.api.diversity.domain.enums.EstadoDetalleSalida;

public interface IDetalleSalidaRepository {
    DetalleSalidaEntity save(DetalleSalidaEntity detalle);

    Optional<DetalleSalidaEntity> findById(Long id);

    List<DetalleSalidaEntity> findAll();

    List<DetalleSalidaEntity> findBySalidaId(Long salidaId);

    List<DetalleSalidaEntity> findByProductoId(Long productoId);

    List<DetalleSalidaEntity> findByEstado(EstadoDetalleSalida estado);

    void deleteById(Long id);
}