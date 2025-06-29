package com.api.diversity.infrastructure.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.api.diversity.domain.model.UsuarioRubroEntity;
import com.api.diversity.domain.ports.IUsuarioRubroRepository;

@Repository
public class UsuarioRubroRepositoryImpl implements IUsuarioRubroRepository {

    @Autowired
    private UsuarioRubroJpaRepository usuarioRubroJpaRepository;

    @Override
    public List<UsuarioRubroEntity> findAll() {
        return usuarioRubroJpaRepository.findAll();
    }

    @Override
    public Optional<UsuarioRubroEntity> findById(Long id) {
        return usuarioRubroJpaRepository.findById(id);
    }

    @Override
    public List<UsuarioRubroEntity> findByUsuarioId(Long usuarioId) {
        return usuarioRubroJpaRepository.findByUsuarioId(usuarioId);
    }

    @Override
    public List<UsuarioRubroEntity> findByRubroId(Long rubroId) {
        return usuarioRubroJpaRepository.findByRubroId(rubroId);
    }

    @Override
    public List<UsuarioRubroEntity> findByUsuarioIdAndEstado(Long usuarioId, String estado) {
        return usuarioRubroJpaRepository.findByUsuarioIdAndEstado(usuarioId, estado);
    }

    @Override
    public UsuarioRubroEntity save(UsuarioRubroEntity usuarioRubro) {
        return usuarioRubroJpaRepository.save(usuarioRubro);
    }

    @Override
    public void deleteById(Long id) {
        usuarioRubroJpaRepository.deleteById(id);
    }

    @Override
    public boolean existsByUsuarioIdAndRubroId(Long usuarioId, Long rubroId) {
        return usuarioRubroJpaRepository.existsByUsuarioIdAndRubroId(usuarioId, rubroId);
    }
}