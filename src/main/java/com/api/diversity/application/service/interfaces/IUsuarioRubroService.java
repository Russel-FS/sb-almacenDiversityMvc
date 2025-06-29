package com.api.diversity.application.service.interfaces;

import java.util.List;

import com.api.diversity.application.dto.UsuarioRubroDto;

public interface IUsuarioRubroService {

    UsuarioRubroDto save(UsuarioRubroDto usuarioRubroDto);

    UsuarioRubroDto update(UsuarioRubroDto usuarioRubroDto);

    void deleteById(Long id);

    UsuarioRubroDto findById(Long id);

    List<UsuarioRubroDto> findAll();

    List<UsuarioRubroDto> findByUsuarioId(Long usuarioId);

    List<UsuarioRubroDto> findByRubroId(Long rubroId);

    List<UsuarioRubroDto> findByUsuarioIdAndEstado(Long usuarioId, String estado);

    boolean existsByUsuarioIdAndRubroId(Long usuarioId, Long rubroId);

    void asignarRubroAUsuario(Long usuarioId, Long rubroId);

    void quitarRubroDeUsuario(Long usuarioId, Long rubroId);

    void cambiarEstadoAsignacion(Long usuarioId, Long rubroId, String estado);
}