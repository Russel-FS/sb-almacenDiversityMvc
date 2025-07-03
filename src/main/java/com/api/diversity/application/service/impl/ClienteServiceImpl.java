package com.api.diversity.application.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.api.diversity.application.dto.ClienteDto;
import com.api.diversity.application.mappers.ClienteMapper;
import com.api.diversity.application.service.interfaces.IClienteService;
import com.api.diversity.domain.model.ClienteEntity;
import com.api.diversity.domain.ports.IClienteRepository;
import com.api.diversity.infrastructure.repository.UsuarioJpaRepository;
import com.api.diversity.domain.enums.EstadoCliente;
import com.api.diversity.domain.enums.TipoCliente;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClienteServiceImpl implements IClienteService {

    private final UsuarioJpaRepository usuarioJpaRepository;
    private final IClienteRepository clienteRepository;
    private final ClienteMapper clienteMapper;

    @Override
    @Transactional
    public ClienteDto save(ClienteDto clienteDto) {
        try {
            log.info("Guardando nuevo cliente: {}", clienteDto.getNombreCompleto());

            // Validar que el DNI no esté duplicado si se proporcionaa
            if (clienteDto.getDni() != null && !clienteDto.getDni().trim().isEmpty()) {
                if (clienteRepository.existsByDni(clienteDto.getDni())) {
                    throw new DataIntegrityViolationException(
                            "Ya existe un cliente con el DNI: " + clienteDto.getDni());
                }
            }

            // Validar que el email no esté duplicado
            if (clienteRepository.existsByEmail(clienteDto.getEmail())) {
                throw new DataIntegrityViolationException(
                        "Ya existe un cliente con el email: " + clienteDto.getEmail());
            }

            ClienteEntity cliente = clienteMapper.toEntity(clienteDto);
            cliente.setEstado(EstadoCliente.Activo);

            // Asignar el usuario que crea el cliente si se proporciona
            if (clienteDto.getCreatedBy() != null) {
                cliente.setCreatedBy(usuarioJpaRepository.findById(clienteDto.getCreatedBy())
                        .orElseThrow(() -> new EntityNotFoundException("Usuario creador no encontrado")));
            } else {
                log.warn("No se proporcionó usuario creador para el cliente: {}", clienteDto.getNombreCompleto());
            }

            return clienteMapper.toDto(clienteRepository.save(cliente));
        } catch (Exception e) {
            log.error("Error al guardar cliente: {}", e.getMessage(), e);
            throw new RuntimeException("Error al guardar cliente: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public ClienteDto update(ClienteDto clienteDto) {
        try {
            log.info("Actualizando cliente ID: {}", clienteDto.getIdCliente());

            ClienteEntity clienteExistente = clienteRepository.findById(clienteDto.getIdCliente())
                    .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado"));

            // Validar que el DNI no esté duplicado en otros clientes
            if (clienteDto.getDni() != null && !clienteDto.getDni().trim().isEmpty()) {
                ClienteEntity clienteConDni = clienteRepository.findByDni(clienteDto.getDni()).orElse(null);
                if (clienteConDni != null && !clienteConDni.getIdCliente().equals(clienteDto.getIdCliente())) {
                    throw new DataIntegrityViolationException(
                            "Ya existe otro cliente con el DNI: " + clienteDto.getDni());
                }
            }

            // Validar que el email no esté duplicado en otros clientes
            ClienteEntity clienteConEmail = clienteRepository.findByEmail(clienteDto.getEmail()).orElse(null);
            if (clienteConEmail != null && !clienteConEmail.getIdCliente().equals(clienteDto.getIdCliente())) {
                throw new DataIntegrityViolationException(
                        "Ya existe otro cliente con el email: " + clienteDto.getEmail());
            }

            // Asignar el usuario que actualiza el cliente si se proporciona
            if (clienteDto.getUpdatedBy() != null) {
                clienteExistente.setUpdatedBy(usuarioJpaRepository.findById(clienteDto.getUpdatedBy())
                        .orElseThrow(() -> new EntityNotFoundException("Usuario actualizador no encontrado")));
            } else {
                log.warn("No se proporcionó usuario actualizador para el cliente ID: {}", clienteDto.getIdCliente());
            }

            // Actualizar campos
            clienteExistente.setNombreCompleto(clienteDto.getNombreCompleto());
            clienteExistente.setDni(clienteDto.getDni());
            clienteExistente.setEmail(clienteDto.getEmail());
            clienteExistente.setTelefono(clienteDto.getTelefono());
            clienteExistente.setDireccion(clienteDto.getDireccion());
            clienteExistente.setTipoCliente(clienteDto.getTipoCliente());
            clienteExistente.setEstado(clienteDto.getEstado() != null ? clienteDto.getEstado() : EstadoCliente.Activo);
            return clienteMapper.toDto(clienteRepository.save(clienteExistente));
        } catch (Exception e) {
            log.error("Error al actualizar cliente: {}", e.getMessage(), e);
            throw new RuntimeException("Error al actualizar cliente: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        try {
            ClienteEntity cliente = clienteRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado"));
            cliente.setEstado(EstadoCliente.Eliminado);
            clienteRepository.save(cliente);
            log.info("Cliente eliminado lógicamente ID: {}", id);
        } catch (Exception e) {
            log.error("Error al eliminar cliente: {}", e.getMessage(), e);
            throw new RuntimeException("Error al eliminar cliente: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public ClienteDto cambiarEstado(Long id, EstadoCliente nuevoEstado) {
        try {
            log.info("Cambiando estado del cliente ID: {} a {}", id, nuevoEstado);

            ClienteEntity cliente = clienteRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado"));

            cliente.setEstado(nuevoEstado);

            return clienteMapper.toDto(clienteRepository.save(cliente));
        } catch (Exception e) {
            log.error("Error al cambiar estado del cliente: {}", e.getMessage(), e);
            throw new RuntimeException("Error al cambiar estado del cliente: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ClienteDto findById(Long id) {
        try {
            return clienteRepository.findById(id)
                    .map(clienteMapper::toDto)
                    .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado"));
        } catch (Exception e) {
            log.error("Error al buscar cliente por ID: {}", e.getMessage(), e);
            throw new RuntimeException("Error al buscar cliente: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ClienteDto findByDni(String dni) {
        try {
            return clienteRepository.findByDni(dni)
                    .map(clienteMapper::toDto)
                    .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado con DNI: " + dni));
        } catch (Exception e) {
            log.error("Error al buscar cliente por DNI: {}", e.getMessage(), e);
            throw new RuntimeException("Error al buscar cliente por DNI: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ClienteDto findByEmail(String email) {
        try {
            return clienteRepository.findByEmail(email)
                    .map(clienteMapper::toDto)
                    .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado con email: " + email));
        } catch (Exception e) {
            log.error("Error al buscar cliente por email: {}", e.getMessage(), e);
            throw new RuntimeException("Error al buscar cliente por email: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClienteDto> findAll() {
        try {
            return clienteRepository.findAll().stream()
                    .map(clienteMapper::toDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error al obtener todos los clientes: {}", e.getMessage(), e);
            throw new RuntimeException("Error al obtener clientes: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClienteDto> findByEstado(EstadoCliente estado) {
        try {
            return clienteRepository.findByEstado(estado).stream()
                    .map(clienteMapper::toDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error al buscar clientes por estado: {}", e.getMessage(), e);
            throw new RuntimeException("Error al buscar clientes por estado: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClienteDto> findByTipoCliente(TipoCliente tipoCliente) {
        try {
            return clienteRepository.findByTipoCliente(tipoCliente).stream()
                    .map(clienteMapper::toDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error al buscar clientes por tipo: {}", e.getMessage(), e);
            throw new RuntimeException("Error al buscar clientes por tipo: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClienteDto> findByNombreCompletoContainingIgnoreCase(String nombre) {
        try {
            return clienteRepository.findByNombreCompletoContainingIgnoreCase(nombre).stream()
                    .map(clienteMapper::toDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error al buscar clientes por nombre: {}", e.getMessage(), e);
            throw new RuntimeException("Error al buscar clientes por nombre: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClienteDto> findTop10ByOrderByFechaRegistroDesc() {
        try {
            return clienteRepository.findTop10ByOrderByFechaCreacionDesc().stream()
                    .map(clienteMapper::toDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error al obtener últimos clientes: {}", e.getMessage(), e);
            throw new RuntimeException("Error al obtener últimos clientes: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return clienteRepository.existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByDni(String dni) {
        return clienteRepository.existsByDni(dni);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return clienteRepository.existsByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public Long countByEstado(EstadoCliente estado) {
        return clienteRepository.countByEstado(estado);
    }

    @Override
    @Transactional(readOnly = true)
    public Long countByTipoCliente(TipoCliente tipoCliente) {
        return clienteRepository.countByTipoCliente(tipoCliente);
    }

    @Override
    @Transactional(readOnly = true)
    public Long countTotal() {
        return clienteRepository.count();
    }
}