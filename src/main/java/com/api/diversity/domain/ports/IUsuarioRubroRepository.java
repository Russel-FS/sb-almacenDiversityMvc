package com.api.diversity.domain.ports;

import java.util.List;
import java.util.Optional;

import com.api.diversity.domain.model.UsuarioRubroEntity;

public interface IUsuarioRubroRepository {

    List<UsuarioRubroEntity> findAll();

    Optional<UsuarioRubroEntity> findById(Long id);

    List<UsuarioRubroEntity> findByUsuarioId(Long usuarioId);

    List<UsuarioRubroEntity> findByRubroId(Long rubroId);

    List<UsuarioRubroEntity> findByUsuarioIdAndEstado(Long usuarioId, String estado);

    UsuarioRubroEntity save(UsuarioRubroEntity usuarioRubro);

    void deleteById(Long id);

    boolean existsByUsuarioIdAndRubroId(Long usuarioId, Long rubroId);
}