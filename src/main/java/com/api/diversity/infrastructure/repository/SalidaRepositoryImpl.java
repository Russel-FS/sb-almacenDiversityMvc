package com.api.diversity.infrastructure.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.api.diversity.domain.model.SalidaEntity;
import com.api.diversity.domain.ports.ISalidaRepository;
import com.api.diversity.domain.enums.EstadoSalida;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class SalidaRepositoryImpl implements ISalidaRepository {

    private final ISalidaJpaRepository salidaJpaRepository;

    @Override
    public SalidaEntity save(SalidaEntity salida) {
        return salidaJpaRepository.save(salida);
    }

    @Override
    public Optional<SalidaEntity> findById(Long id) {
        return salidaJpaRepository.findById(id);
    }

    @Override
    public List<SalidaEntity> findAll() {
        return salidaJpaRepository.findAll();
    }

    @Override
    public List<SalidaEntity> findByEstado(EstadoSalida estado) {
        return salidaJpaRepository.findByEstado(estado);
    }

    @Override
    public List<SalidaEntity> findByFechaSalidaBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return salidaJpaRepository.findByFechaSalidaBetween(fechaInicio, fechaFin);
    }

    @Override
    public List<SalidaEntity> findByClienteId(Long clienteId) {
        return salidaJpaRepository.findByClienteIdCliente(clienteId);
    }

    @Override
    public List<SalidaEntity> findByUsuarioRegistroId(Long usuarioId) {
        return salidaJpaRepository.findByUsuarioRegistroIdUsuario(usuarioId);
    }

    @Override
    public List<SalidaEntity> findByRubroId(Long rubroId) {
        return salidaJpaRepository.findByRubroId(rubroId);
    }

    @Override
    public List<SalidaEntity> findTop10ByOrderByFechaSalidaDesc() {
        return salidaJpaRepository.findTop10ByOrderByFechaSalidaDesc();
    }

    @Override
    public boolean existsById(Long id) {
        return salidaJpaRepository.existsById(id);
    }

    @Override
    public void deleteById(Long id) {
        salidaJpaRepository.deleteById(id);
    }

    @Override
    public Long countByEstado(EstadoSalida estado) {
        return salidaJpaRepository.countByEstado(estado);
    }

    @Override
    public Long countByFechaSalidaBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return salidaJpaRepository.countByFechaSalidaBetween(fechaInicio, fechaFin);
    }

    @Override
    public boolean existsByNumeroDocumento(String numeroDocumento) {
        return salidaJpaRepository.existsByNumeroDocumento(numeroDocumento);
    }
}