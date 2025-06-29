package com.api.diversity.domain.ports;

import java.util.List;
import java.util.Optional;

import com.api.diversity.domain.model.DetalleEntradaEntity;
import com.api.diversity.domain.enums.EstadoDetalleEntrada;

public interface IDetalleEntradaRepository {
    DetalleEntradaEntity save(DetalleEntradaEntity detalle);

    Optional<DetalleEntradaEntity> findById(Long id);

    List<DetalleEntradaEntity> findAll();

    List<DetalleEntradaEntity> findByEntradaId(Long entradaId);

    List<DetalleEntradaEntity> findByProductoId(Long productoId);

    List<DetalleEntradaEntity> findByEstado(EstadoDetalleEntrada estado);

    void deleteById(Long id);
}