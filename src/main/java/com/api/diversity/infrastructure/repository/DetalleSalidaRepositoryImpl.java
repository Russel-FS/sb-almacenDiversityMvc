package com.api.diversity.infrastructure.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.api.diversity.domain.model.DetalleSalidaEntity;
import com.api.diversity.domain.ports.IDetalleSalidaRepository;
import com.api.diversity.domain.enums.EstadoDetalleSalida;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class DetalleSalidaRepositoryImpl implements IDetalleSalidaRepository {

    private final IDetalleSalidaJpaRepository detalleSalidaJpaRepository;

    @Override
    public DetalleSalidaEntity save(DetalleSalidaEntity detalle) {
        return detalleSalidaJpaRepository.save(detalle);
    }

    @Override
    public Optional<DetalleSalidaEntity> findById(Long id) {
        return detalleSalidaJpaRepository.findById(id);
    }

    @Override
    public List<DetalleSalidaEntity> findAll() {
        return detalleSalidaJpaRepository.findAll();
    }

    @Override
    public List<DetalleSalidaEntity> findBySalidaId(Long salidaId) {
        return detalleSalidaJpaRepository.findBySalidaId(salidaId);
    }

    @Override
    public List<DetalleSalidaEntity> findByProductoId(Long productoId) {
        return detalleSalidaJpaRepository.findByProductoId(productoId);
    }

    @Override
    public List<DetalleSalidaEntity> findByEstado(EstadoDetalleSalida estado) {
        return detalleSalidaJpaRepository.findByEstado(estado);
    }

    @Override
    public void deleteById(Long id) {
        detalleSalidaJpaRepository.deleteById(id);
    }
}