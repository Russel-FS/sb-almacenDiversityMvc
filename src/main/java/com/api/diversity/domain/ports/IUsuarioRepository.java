package com.api.diversity.domain.ports;

import java.util.List;
import java.util.Optional;

import com.api.diversity.domain.model.UsuarioEntity;

public interface IUsuarioRepository {
    List<UsuarioEntity> findAll();

    Optional<UsuarioEntity> findById(Long id);

    Optional<UsuarioEntity> findByEmail(String email);

    UsuarioEntity save(UsuarioEntity usuario);

    void deleteById(Long id);

    boolean existsById(Long id);

    boolean existsByEmail(String email);

    boolean existsByNombreUsuario(String nombreUsuario);
}