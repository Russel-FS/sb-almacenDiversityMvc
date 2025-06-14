package com.api.diversity.application.service.interfaces;

import com.api.diversity.application.dto.UsuarioDto;

public interface IUsuarioService {
    UsuarioDto findByEmailAndPassword(String email, String contraseña);

    UsuarioDto findByEmail(String email);

    UsuarioDto save(UsuarioDto usuarioDto);

    UsuarioDto update(UsuarioDto usuarioDto);

    void deleteById(Long id);

    UsuarioDto findById(Long id);

    boolean existsByEmail(String email);

    boolean existsByNombreUsuario(String nombreUsuario);
}