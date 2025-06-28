package com.api.diversity.application.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.api.diversity.application.dto.UsuarioRubroDto;
import com.api.diversity.application.mappers.UsuarioRubroMapper;
import com.api.diversity.application.service.interfaces.IUsuarioRubroService;
import com.api.diversity.domain.enums.EstadoUsuarioRubro;
import com.api.diversity.domain.model.RubroEntity;
import com.api.diversity.domain.model.UsuarioEntity;
import com.api.diversity.domain.model.UsuarioRubroEntity;
import com.api.diversity.domain.ports.RubroRepository;
import com.api.diversity.domain.ports.IUsuarioRepository;
import com.api.diversity.domain.ports.IUsuarioRubroRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UsuarioRubroServiceImpl implements IUsuarioRubroService {

    private final IUsuarioRubroRepository usuarioRubroRepository;
    private final IUsuarioRepository usuarioRepository;
    private final RubroRepository rubroRepository;
    private final UsuarioRubroMapper usuarioRubroMapper;

    @Override
    @Transactional
    public UsuarioRubroDto save(UsuarioRubroDto usuarioRubroDto) {
        try {
            // Validar que el usuario y rubro existan
            UsuarioEntity usuario = usuarioRepository.findById(usuarioRubroDto.getIdUsuario())
                    .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

            RubroEntity rubro = rubroRepository.findById(usuarioRubroDto.getIdRubro())
                    .orElseThrow(() -> new EntityNotFoundException("Rubro no encontrado"));

            // Verificar si ya existe la asignación
            if (usuarioRubroRepository.existsByUsuarioIdAndRubroId(usuarioRubroDto.getIdUsuario(),
                    usuarioRubroDto.getIdRubro())) {
                throw new IllegalArgumentException("El usuario ya tiene asignado este rubro");
            }

            UsuarioRubroEntity entity = usuarioRubroMapper.toEntity(usuarioRubroDto);
            entity.setUsuario(usuario);
            entity.setRubro(rubro);

            return usuarioRubroMapper.toDto(usuarioRubroRepository.save(entity));
        } catch (Exception e) {
            log.error("Error al guardar asignación usuario-rubro: {}", e.getMessage(), e);
            throw new RuntimeException("Error al guardar asignación: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public UsuarioRubroDto update(UsuarioRubroDto usuarioRubroDto) {
        try {
            UsuarioRubroEntity entity = usuarioRubroRepository.findById(usuarioRubroDto.getIdUsuarioRubro())
                    .orElseThrow(() -> new EntityNotFoundException("Asignación usuario-rubro no encontrada"));

            entity.setEstado(usuarioRubroDto.getEstado());

            return usuarioRubroMapper.toDto(usuarioRubroRepository.save(entity));
        } catch (Exception e) {
            log.error("Error al actualizar asignación usuario-rubro: {}", e.getMessage(), e);
            throw new RuntimeException("Error al actualizar asignación: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        try {
            if (!usuarioRubroRepository.findById(id).isPresent()) {
                throw new EntityNotFoundException("Asignación usuario-rubro no encontrada");
            }
            usuarioRubroRepository.deleteById(id);
        } catch (Exception e) {
            log.error("Error al eliminar asignación usuario-rubro: {}", e.getMessage(), e);
            throw new RuntimeException("Error al eliminar asignación: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioRubroDto findById(Long id) {
        try {
            return usuarioRubroRepository.findById(id)
                    .map(usuarioRubroMapper::toDto)
                    .orElseThrow(() -> new EntityNotFoundException("Asignación usuario-rubro no encontrada"));
        } catch (Exception e) {
            log.error("Error al buscar asignación por ID: {}", e.getMessage(), e);
            throw new RuntimeException("Error al buscar asignación: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioRubroDto> findAll() {
        try {
            return usuarioRubroRepository.findAll().stream()
                    .map(usuarioRubroMapper::toDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error al obtener todas las asignaciones: {}", e.getMessage(), e);
            throw new RuntimeException("Error al obtener asignaciones: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioRubroDto> findByUsuarioId(Long usuarioId) {
        try {
            return usuarioRubroRepository.findByUsuarioId(usuarioId).stream()
                    .map(usuarioRubroMapper::toDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error al buscar asignaciones por usuario: {}", e.getMessage(), e);
            throw new RuntimeException("Error al buscar asignaciones: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioRubroDto> findByRubroId(Long rubroId) {
        try {
            return usuarioRubroRepository.findByRubroId(rubroId).stream()
                    .map(usuarioRubroMapper::toDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error al buscar asignaciones por rubro: {}", e.getMessage(), e);
            throw new RuntimeException("Error al buscar asignaciones: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioRubroDto> findByUsuarioIdAndEstado(Long usuarioId, String estado) {
        try {
            return usuarioRubroRepository.findByUsuarioIdAndEstado(usuarioId, estado).stream()
                    .map(usuarioRubroMapper::toDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error al buscar asignaciones por usuario y estado: {}", e.getMessage(), e);
            throw new RuntimeException("Error al buscar asignaciones: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByUsuarioIdAndRubroId(Long usuarioId, Long rubroId) {
        return usuarioRubroRepository.existsByUsuarioIdAndRubroId(usuarioId, rubroId);
    }

    @Override
    @Transactional
    public void asignarRubroAUsuario(Long usuarioId, Long rubroId) {
        try {
            // Verificar si ya existe la asignación
            if (usuarioRubroRepository.existsByUsuarioIdAndRubroId(usuarioId, rubroId)) {
                throw new IllegalArgumentException("El usuario ya tiene asignado este rubro");
            }

            // Validar que el usuario y rubro existannnnnnnnnn 1 am a dormir
            UsuarioEntity usuario = usuarioRepository.findById(usuarioId)
                    .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

            RubroEntity rubro = rubroRepository.findById(rubroId)
                    .orElseThrow(() -> new EntityNotFoundException("Rubro no encontrado"));

            // Crear nueva asignación
            UsuarioRubroEntity entity = new UsuarioRubroEntity();
            entity.setUsuario(usuario);
            entity.setRubro(rubro);
            entity.setEstado(EstadoUsuarioRubro.ACTIVO);

            usuarioRubroRepository.save(entity);
            log.info("Rubro {} asignado exitosamente al usuario {}", rubroId, usuarioId);
        } catch (Exception e) {
            log.error("Error al asignar rubro a usuario: {}", e.getMessage(), e);
            throw new RuntimeException("Error al asignar rubro: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public void quitarRubroDeUsuario(Long usuarioId, Long rubroId) {
        try {
            List<UsuarioRubroEntity> asignaciones = usuarioRubroRepository.findByUsuarioId(usuarioId);

            UsuarioRubroEntity asignacion = asignaciones.stream()
                    .filter(ur -> ur.getRubro().getIdRubro().equals(rubroId))
                    .findFirst()
                    .orElseThrow(() -> new EntityNotFoundException("Asignación usuario-rubro no encontrada"));

            usuarioRubroRepository.deleteById(asignacion.getIdUsuarioRubro());
            log.info("Rubro {} removido exitosamente del usuario {}", rubroId, usuarioId);
        } catch (Exception e) {
            log.error("Error al quitar rubro de usuario: {}", e.getMessage(), e);
            throw new RuntimeException("Error al quitar rubro: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public void cambiarEstadoAsignacion(Long usuarioId, Long rubroId, String estado) {
        try {
            List<UsuarioRubroEntity> asignaciones = usuarioRubroRepository.findByUsuarioId(usuarioId);

            UsuarioRubroEntity asignacion = asignaciones.stream()
                    .filter(ur -> ur.getRubro().getIdRubro().equals(rubroId))
                    .findFirst()
                    .orElseThrow(() -> new EntityNotFoundException("Asignación usuario-rubro no encontrada"));

            asignacion.setEstado(EstadoUsuarioRubro.valueOf(estado.toUpperCase()));
            usuarioRubroRepository.save(asignacion);
            log.info("Estado de asignación cambiado a {} para usuario {} y rubro {}", estado, usuarioId, rubroId);
        } catch (Exception e) {
            log.error("Error al cambiar estado de asignación: {}", e.getMessage(), e);
            throw new RuntimeException("Error al cambiar estado: " + e.getMessage(), e);
        }
    }
}