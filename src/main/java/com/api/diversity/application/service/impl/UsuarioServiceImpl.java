package com.api.diversity.application.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.api.diversity.application.dto.UsuarioDto;
import com.api.diversity.application.mappers.UsuarioMapper;
import com.api.diversity.application.service.interfaces.IUsuarioService;
import com.api.diversity.domain.model.UsuarioEntity;
import com.api.diversity.domain.ports.IUsuarioRepository;

import jakarta.persistence.EntityNotFoundException;
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
            // Encriptación de contraseña si es nueva
            if (usuarioDto.getContraseña() != null && !usuarioDto.getContraseña().startsWith("$2a$")) {
                usuarioDto.setContraseña(passwordEncoder.encode(usuarioDto.getContraseña()));
            }

            UsuarioEntity usuario = usuarioMapper.toEntity(usuarioDto);
            return usuarioMapper.toDto(usuarioRepository.save(usuario));
        } catch (Exception e) {
            log.error("Error al guardar usuario: {}", e.getMessage(), e);
            throw new RuntimeException("Error al guardar usuario: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public UsuarioDto update(UsuarioDto usuarioDto) {
        try {
            UsuarioEntity usuarioExistente = usuarioRepository.findById(usuarioDto.getIdUsuario())
                    .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

            // Manejo de contraseña
            if (usuarioDto.getContraseña() != null && !usuarioDto.getContraseña().isEmpty()) {
                usuarioDto.setContraseña(passwordEncoder.encode(usuarioDto.getContraseña()));
            } else {
                usuarioDto.setContraseña(usuarioExistente.getContraseña());
            }

            UsuarioEntity usuario = usuarioMapper.toEntity(usuarioDto);
            return usuarioMapper.toDto(usuarioRepository.save(usuario));
        } catch (Exception e) {
            log.error("Error al actualizar usuario: {}", e.getMessage(), e);
            throw new RuntimeException("Error al actualizar usuario: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        try {
            if (!usuarioRepository.existsById(id)) {
                throw new EntityNotFoundException("Usuario no encontrado");
            }
            usuarioRepository.deleteById(id);
        } catch (Exception e) {
            log.error("Error al eliminar usuario: {}", e.getMessage(), e);
            throw new RuntimeException("Error al eliminar usuario: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioDto findById(Long id) {
        try {
            return usuarioRepository.findById(id)
                    .map(usuarioMapper::toDto)
                    .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
        } catch (Exception e) {
            log.error("Error al buscar usuario por ID: {}", e.getMessage(), e);
            throw new RuntimeException("Error al buscar usuario: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioDto> findAll() {
        try {
            return usuarioRepository.findAll().stream()
                    .map(usuarioMapper::toDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error al obtener todos los usuarios: {}", e.getMessage(), e);
            throw new RuntimeException("Error al obtener usuarios: " + e.getMessage(), e);
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
        try {
            return usuarioRepository.findByEmail(email)
                    .map(usuarioMapper::toDto)
                    .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
        } catch (Exception e) {
            log.error("Error al buscar usuario por email: {}", e.getMessage(), e);
            throw new RuntimeException("Error al buscar usuario por email: " + e.getMessage(), e);
        }
    }
}