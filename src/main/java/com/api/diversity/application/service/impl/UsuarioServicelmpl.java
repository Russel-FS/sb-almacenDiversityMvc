package com.api.diversity.application.service.impl;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.diversity.application.service.interfaces.UsuarioService;
import com.api.diversity.domain.model.Usuario;
import com.api.diversity.infrastructure.repository.UsuarioRepository;

@Service
public class UsuarioServicelmpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public List<Usuario> getAllUsuarios() {
        return usuarioRepository.findAll();
    }

    @Override
    public void saveUsuario(Usuario usuario) {
        this.usuarioRepository.save(usuario);
    }

    @Override
    public Usuario getUsuarioById(Integer id) {
        Optional<Usuario> optional = usuarioRepository.findById(id);
        Usuario usuario = null;
        if (optional.isPresent()) {
            usuario = optional.get();
        } else {
            throw new RuntimeException("Usuario no encontrado por id: " + id);
        }
        return usuario;
    }

    @Override
    public void deleteUsuarioById(Integer id) {
        this.usuarioRepository.deleteById(id);
    }
}