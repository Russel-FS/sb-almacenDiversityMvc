package com.api.diversity.infrastructure.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.diversity.domain.model.DetalleEntradaEntity;
import com.api.diversity.domain.enums.EstadoDetalleEntrada;

@Repository
public interface IDetalleEntradaJpaRepository extends JpaRepository<DetalleEntradaEntity, Long> {
    List<DetalleEntradaEntity> findByEntradaId(Long entradaId);

    List<DetalleEntradaEntity> findByProductoId(Long productoId);

    List<DetalleEntradaEntity> findByEstado(EstadoDetalleEntrada estado);
}