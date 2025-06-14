package com.api.diversity.infrastructure.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.api.diversity.domain.model.UsuarioEntity;
import com.api.diversity.domain.ports.IUsuarioRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UsuarioRepositoryImpl implements IUsuarioRepository {

    private final UsuarioJpaRepository usuarioJpaRepository;

    @Override
    public List<UsuarioEntity> findAll() {
        return usuarioJpaRepository.findAll();
    }

    @Override
    public Optional<UsuarioEntity> findById(Long id) {
        return usuarioJpaRepository.findById(id);
    }

    @Override
    public Optional<UsuarioEntity> findByEmail(String email) {
        return usuarioJpaRepository.findByEmail(email);
    }

    @Override
    public UsuarioEntity save(UsuarioEntity usuario) {
        return usuarioJpaRepository.save(usuario);
    }

    @Override
    public void deleteById(Long id) {
        usuarioJpaRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return usuarioJpaRepository.existsById(id);
    }

    @Override
    public boolean existsByEmail(String email) {
        return usuarioJpaRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByNombreUsuario(String nombreUsuario) {
        return usuarioJpaRepository.existsByNombreUsuario(nombreUsuario);
    }
}