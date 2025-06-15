package com.api.diversity.application.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.api.diversity.application.dto.UsuarioDto;
import com.api.diversity.application.mappers.UsuarioMapper;
import com.api.diversity.application.service.interfaces.IUsuarioService;
import com.api.diversity.domain.model.UsuarioEntity;
import com.api.diversity.domain.ports.IUsuarioRepository; 

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UsuarioServiceImpl implements IUsuarioService {

    private final IUsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UsuarioDto save(UsuarioDto usuarioDto) {
        try {
            // Validaciones existentes 
            if (usuarioRepository.existsByEmail(usuarioDto.getEmail())) {
                throw new RuntimeException("El email ya está registrado");
            }
            if (usuarioRepository.existsByNombreUsuario(usuarioDto.getNombreUsuario())) {
                throw new RuntimeException("El nombre de usuario ya está registrado");
            }

            // Validaciones adicionales
            if (usuarioDto.getRubro() == null || usuarioDto.getRubro().getIdRubro() == null) {
                throw new RuntimeException("El rubro es requerido");
            }

            if (usuarioDto.getRol() == null || usuarioDto.getRol().getIdRol() == null) {
                throw new RuntimeException("El rol es requerido");
            }

            // Encriptación de contraseñaa
            if (usuarioDto.getContraseña() != null && !usuarioDto.getContraseña().startsWith("$2a$")) {
                usuarioDto.setContraseña(passwordEncoder.encode(usuarioDto.getContraseña()));
            }

            UsuarioEntity usuario = usuarioMapper.toEntity(usuarioDto);
            return usuarioMapper.toDto(usuarioRepository.save(usuario));
        } catch (Exception e) {
            log.error("Error al guardar usuario", e);
            throw new RuntimeException("Error al guardar usuario: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public UsuarioDto update(UsuarioDto usuarioDto) {
        try {
            UsuarioEntity usuarioExistente = usuarioRepository.findById(usuarioDto.getIdUsuario())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
 
            if (usuarioDto.getRubro() == null || usuarioDto.getRubro().getIdRubro() == null) {
                throw new RuntimeException("El rubro es requerido");
            }

            if (usuarioDto.getRol() == null || usuarioDto.getRol().getIdRol() == null) {
                throw new RuntimeException("El rol es requerido");
            }

            // Manejo de contraseña
            if (usuarioDto.getContraseña() != null && !usuarioDto.getContraseña().isEmpty()) {
                usuarioDto.setContraseña(passwordEncoder.encode(usuarioDto.getContraseña()));
            } else {
                usuarioDto.setContraseña(usuarioExistente.getContraseña());
            }

            UsuarioEntity usuario = usuarioMapper.toEntity(usuarioDto);
            return usuarioMapper.toDto(usuarioRepository.save(usuario));
        } catch (Exception e) {
            log.error("Error al actualizar usuario", e);
            throw new RuntimeException("Error al actualizar usuario: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        try {
            usuarioRepository.deleteById(id);
        } catch (Exception e) {
            log.error("Error al eliminar usuario", e);
            throw new RuntimeException("Error al eliminar usuario", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioDto findById(Long id) {
        try {
            return usuarioRepository.findById(id)
                    .map(usuarioMapper::toDto)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        } catch (Exception e) {
            log.error("Error al buscar usuario por ID", e);
            throw new RuntimeException("Error al buscar usuario", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByNombreUsuario(String nombreUsuario) {
        return usuarioRepository.existsByNombreUsuario(nombreUsuario);
    }

    @Override
    public UsuarioDto findByEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .map(usuarioMapper::toDto)
                .orElse(null);
    }
}