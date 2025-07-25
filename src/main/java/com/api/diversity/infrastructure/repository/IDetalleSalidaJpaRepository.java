package com.api.diversity.infrastructure.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.diversity.domain.model.DetalleSalidaEntity;
import com.api.diversity.domain.enums.EstadoDetalleSalida;

@Repository
public interface IDetalleSalidaJpaRepository extends JpaRepository<DetalleSalidaEntity, Long> {
    List<DetalleSalidaEntity> findBySalidaIdSalida(Long salidaId);

    List<DetalleSalidaEntity> findByProductoIdProducto(Long productoId);

    List<DetalleSalidaEntity> findByEstado(EstadoDetalleSalida estado);
}