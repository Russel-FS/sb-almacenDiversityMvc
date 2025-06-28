package com.api.diversity.infrastructure.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.api.diversity.domain.model.EntradaEntity;
import com.api.diversity.domain.ports.IEntradaRepository;
import com.api.diversity.domain.enums.EstadoEntrada;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class EntradaRepositoryImpl implements IEntradaRepository {

    private final IEntradaJpaRepository entradaJpaRepository;

    @Override
    public EntradaEntity save(EntradaEntity entrada) {
        return entradaJpaRepository.save(entrada);
    }

    @Override
    public Optional<EntradaEntity> findById(Long id) {
        return entradaJpaRepository.findById(id);
    }

    @Override
    public List<EntradaEntity> findAll() {
        return entradaJpaRepository.findAll();
    }

    @Override
    public List<EntradaEntity> findByEstado(EstadoEntrada estado) {
        return entradaJpaRepository.findByEstado(estado);
    }

    @Override
    public List<EntradaEntity> findByFechaEntradaBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return entradaJpaRepository.findByFechaEntradaBetween(fechaInicio, fechaFin);
    }

    @Override
    public List<EntradaEntity> findByProveedorId(Long proveedorId) {
        return entradaJpaRepository.findByProveedorIdProveedor(proveedorId);
    }

    @Override
    public List<EntradaEntity> findByUsuarioRegistroId(Long usuarioId) {
        return entradaJpaRepository.findByUsuarioRegistroIdUsuario(usuarioId);
    }

    @Override
    public List<EntradaEntity> findByRubroId(Long rubroId) {
        return entradaJpaRepository.findByRubroId(rubroId);
    }

    @Override
    public List<EntradaEntity> findTop10ByOrderByFechaEntradaDesc() {
        return entradaJpaRepository.findTop10ByOrderByFechaEntradaDesc();
    }

    @Override
    public boolean existsById(Long id) {
        return entradaJpaRepository.existsById(id);
    }

    @Override
    public void deleteById(Long id) {
        entradaJpaRepository.deleteById(id);
    }

    @Override
    public Long countByEstado(EstadoEntrada estado) {
        return entradaJpaRepository.countByEstado(estado);
    }

    @Override
    public Long countByFechaEntradaBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return entradaJpaRepository.countByFechaEntradaBetween(fechaInicio, fechaFin);
    }
}