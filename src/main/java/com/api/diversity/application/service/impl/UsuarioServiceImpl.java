package com.api.diversity.application.service.impl;

import org.springframework.dao.DataIntegrityViolationException;
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
            // Validaciones existentes
            if (usuarioRepository.existsByEmail(usuarioDto.getEmail())) {
                throw new DataIntegrityViolationException("El email ya está registrado");
            }
            if (usuarioRepository.existsByNombreUsuario(usuarioDto.getNombreUsuario())) {
                throw new DataIntegrityViolationException("El nombre de usuario ya está registrado");
            }

            // Validaciones adicionales
            if (usuarioDto.getRubro() == null || usuarioDto.getRubro().getIdRubro() == null) {
                throw new IllegalArgumentException("El rubro es requerido");
            }

            if (usuarioDto.getRol() == null || usuarioDto.getRol().getIdRol() == null) {
                throw new IllegalArgumentException("El rol es requerido");
            }

            // Encriptación de contraseña
            if (usuarioDto.getContraseña() != null && !usuarioDto.getContraseña().startsWith("$2a$")) {
                usuarioDto.setContraseña(passwordEncoder.encode(usuarioDto.getContraseña()));
            }

            UsuarioEntity usuario = usuarioMapper.toEntity(usuarioDto);
            return usuarioMapper.toDto(usuarioRepository.save(usuario));
        } catch (DataIntegrityViolationException e) {
            log.error("Error de integridad de datos al guardar usuario", e);
            throw new DataIntegrityViolationException("Error al guardar usuario: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("Error de validación al guardar usuario", e);
            throw new IllegalArgumentException("Error de validación: " + e.getMessage());
        } catch (Exception e) {
            log.error("Error inesperado al guardar usuario", e);
            throw new RuntimeException("Error inesperado al guardar usuario", e);
        }
    }

    @Override
    @Transactional
    public UsuarioDto update(UsuarioDto usuarioDto) {
        try {
            UsuarioEntity usuarioExistente = usuarioRepository.findById(usuarioDto.getIdUsuario())
                    .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

            if (usuarioDto.getRubro() == null || usuarioDto.getRubro().getIdRubro() == null) {
                throw new IllegalArgumentException("El rubro es requerido");
            }

            if (usuarioDto.getRol() == null || usuarioDto.getRol().getIdRol() == null) {
                throw new IllegalArgumentException("El rol es requerido");
            }

            // Manejo de contraseña
            if (usuarioDto.getContraseña() != null && !usuarioDto.getContraseña().isEmpty()) {
                usuarioDto.setContraseña(passwordEncoder.encode(usuarioDto.getContraseña()));
            } else {
                usuarioDto.setContraseña(usuarioExistente.getContraseña());
            }

            UsuarioEntity usuario = usuarioMapper.toEntity(usuarioDto);
            return usuarioMapper.toDto(usuarioRepository.save(usuario));
        } catch (EntityNotFoundException e) {
            log.error("Usuario no encontrado", e);
            throw new EntityNotFoundException(e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("Error de validación al actualizar usuario", e);
            throw new IllegalArgumentException("Error de validación: " + e.getMessage());
        } catch (DataIntegrityViolationException e) {
            log.error("Error de integridad de datos al actualizar usuario", e);
            throw new DataIntegrityViolationException("Error al actualizar usuario: " + e.getMessage());
        } catch (Exception e) {
            log.error("Error inesperado al actualizar usuario", e);
            throw new RuntimeException("Error inesperado al actualizar usuario", e);
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
        } catch (EntityNotFoundException e) {
            log.error("Usuario no encontrado al intentar eliminar", e);
            throw new EntityNotFoundException(e.getMessage());
        } catch (DataIntegrityViolationException e) {
            log.error("Error de integridad de datos al eliminar usuario", e);
            throw new DataIntegrityViolationException(
                    "No se puede eliminar el usuario porque tiene registros relacionados");
        } catch (Exception e) {
            log.error("Error inesperado al eliminar usuario", e);
            throw new RuntimeException("Error inesperado al eliminar usuario", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioDto findById(Long id) {
        try {
            return usuarioRepository.findById(id)
                    .map(usuarioMapper::toDto)
                    .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
        } catch (EntityNotFoundException e) {
            log.error("Usuario no encontrado", e);
            throw new EntityNotFoundException(e.getMessage());
        } catch (Exception e) {
            log.error("Error inesperado al buscar usuario por ID", e);
            throw new RuntimeException("Error inesperado al buscar usuario", e);
        }
    }

    // Los métodos de consulta simples pueden mantener su implementación actual
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
        } catch (EntityNotFoundException e) {
            log.error("Usuario no encontrado por email", e);
            throw new EntityNotFoundException(e.getMessage());
        } catch (Exception e) {
            log.error("Error inesperado al buscar usuario por email", e);
            throw new RuntimeException("Error inesperado al buscar usuario por email", e);
        }
    }
}