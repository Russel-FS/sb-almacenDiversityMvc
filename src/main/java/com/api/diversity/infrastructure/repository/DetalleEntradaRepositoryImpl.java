package com.api.diversity.infrastructure.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.api.diversity.domain.model.DetalleEntradaEntity;
import com.api.diversity.domain.ports.IDetalleEntradaRepository;
import com.api.diversity.domain.enums.EstadoDetalleEntrada;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class DetalleEntradaRepositoryImpl implements IDetalleEntradaRepository {

    private final IDetalleEntradaJpaRepository detalleEntradaJpaRepository;

    @Override
    public DetalleEntradaEntity save(DetalleEntradaEntity detalle) {
        return detalleEntradaJpaRepository.save(detalle);
    }

    @Override
    public Optional<DetalleEntradaEntity> findById(Long id) {
        return detalleEntradaJpaRepository.findById(id);
    }

    @Override
    public List<DetalleEntradaEntity> findAll() {
        return detalleEntradaJpaRepository.findAll();
    }

    @Override
    public List<DetalleEntradaEntity> findByEntradaId(Long entradaId) {
        return detalleEntradaJpaRepository.findByEntradaIdEntrada(entradaId);
    }

    @Override
    public List<DetalleEntradaEntity> findByProductoId(Long productoId) {
        return detalleEntradaJpaRepository.findByProductoIdProducto(productoId);
    }

    @Override
    public List<DetalleEntradaEntity> findByEstado(EstadoDetalleEntrada estado) {
        return detalleEntradaJpaRepository.findByEstado(estado);
    }

    @Override
    public void deleteById(Long id) {
        detalleEntradaJpaRepository.deleteById(id);
    }
}