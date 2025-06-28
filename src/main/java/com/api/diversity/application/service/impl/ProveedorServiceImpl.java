package com.api.diversity.application.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.api.diversity.application.dto.ProveedorDto;
import com.api.diversity.application.mappers.ProveedorMapper;
import com.api.diversity.application.service.interfaces.IProveedorService;
import com.api.diversity.domain.model.ProveedorEntity;
import com.api.diversity.domain.ports.IProveedorRepository;
import com.api.diversity.domain.enums.EstadoProveedor;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProveedorServiceImpl implements IProveedorService {

    private final IProveedorRepository proveedorRepository;
    private final ProveedorMapper proveedorMapper;

    @Override
    @Transactional
    public ProveedorDto save(ProveedorDto proveedorDto) {
        try {
            log.info("Guardando nuevo proveedor: {}", proveedorDto.getRazonSocial());

            // Validar que el RUC no esté duplicado si se proporciona
            if (proveedorDto.getRuc() != null && !proveedorDto.getRuc().trim().isEmpty()) {
                if (proveedorRepository.existsByRuc(proveedorDto.getRuc())) {
                    throw new DataIntegrityViolationException(
                            "Ya existe un proveedor con el RUC: " + proveedorDto.getRuc());
                }
            }

            // Validar que el email no esté duplicado
            if (proveedorRepository.existsByEmail(proveedorDto.getEmail())) {
                throw new DataIntegrityViolationException(
                        "Ya existe un proveedor con el email: " + proveedorDto.getEmail());
            }

            ProveedorEntity proveedor = proveedorMapper.toEntity(proveedorDto);
            proveedor.setEstado(EstadoProveedor.Activo);
            // La fecha de creación se maneja automáticamente con @CreationTimestamp

            return proveedorMapper.toDto(proveedorRepository.save(proveedor));
        } catch (Exception e) {
            log.error("Error al guardar proveedor: {}", e.getMessage(), e);
            throw new RuntimeException("Error al guardar proveedor: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public ProveedorDto update(ProveedorDto proveedorDto) {
        try {
            log.info("Actualizando proveedor ID: {}", proveedorDto.getIdProveedor());

            ProveedorEntity proveedorExistente = proveedorRepository.findById(proveedorDto.getIdProveedor())
                    .orElseThrow(() -> new EntityNotFoundException("Proveedor no encontrado"));

            // Validar que el RUC no esté duplicado en otros proveedores
            if (proveedorDto.getRuc() != null && !proveedorDto.getRuc().trim().isEmpty()) {
                ProveedorEntity proveedorConRuc = proveedorRepository.findByRuc(proveedorDto.getRuc()).orElse(null);
                if (proveedorConRuc != null
                        && !proveedorConRuc.getIdProveedor().equals(proveedorDto.getIdProveedor())) {
                    throw new DataIntegrityViolationException(
                            "Ya existe otro proveedor con el RUC: " + proveedorDto.getRuc());
                }
            }

            // Validar que el email no esté duplicado en otros proveedores
            ProveedorEntity proveedorConEmail = proveedorRepository.findByEmail(proveedorDto.getEmail()).orElse(null);
            if (proveedorConEmail != null
                    && !proveedorConEmail.getIdProveedor().equals(proveedorDto.getIdProveedor())) {
                throw new DataIntegrityViolationException(
                        "Ya existe otro proveedor con el email: " + proveedorDto.getEmail());
            }

            // Actualizar campos
            proveedorExistente.setRazonSocial(proveedorDto.getRazonSocial());
            proveedorExistente.setRuc(proveedorDto.getRuc());
            proveedorExistente.setEmail(proveedorDto.getEmail());
            proveedorExistente.setTelefono(proveedorDto.getTelefono());
            proveedorExistente.setDireccion(proveedorDto.getDireccion());
            proveedorExistente.setRepresentanteLegal(proveedorDto.getRepresentanteLegal());
            proveedorExistente.setTipoDocumento(proveedorDto.getTipoDocumento());
            // La fecha de modificación se maneja automáticamente con @UpdateTimestamp

            return proveedorMapper.toDto(proveedorRepository.save(proveedorExistente));
        } catch (Exception e) {
            log.error("Error al actualizar proveedor: {}", e.getMessage(), e);
            throw new RuntimeException("Error al actualizar proveedor: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        try {
            if (!proveedorRepository.existsById(id)) {
                throw new EntityNotFoundException("Proveedor no encontrado");
            }
            proveedorRepository.deleteById(id);
            log.info("Proveedor eliminado ID: {}", id);
        } catch (Exception e) {
            log.error("Error al eliminar proveedor: {}", e.getMessage(), e);
            throw new RuntimeException("Error al eliminar proveedor: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public ProveedorDto cambiarEstado(Long id, EstadoProveedor nuevoEstado) {
        try {
            log.info("Cambiando estado del proveedor ID: {} a {}", id, nuevoEstado);

            ProveedorEntity proveedor = proveedorRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Proveedor no encontrado"));

            proveedor.setEstado(nuevoEstado);
            // La fecha de modificación se maneja automáticamente con @UpdateTimestamp

            return proveedorMapper.toDto(proveedorRepository.save(proveedor));
        } catch (Exception e) {
            log.error("Error al cambiar estado del proveedor: {}", e.getMessage(), e);
            throw new RuntimeException("Error al cambiar estado del proveedor: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ProveedorDto findById(Long id) {
        try {
            return proveedorRepository.findById(id)
                    .map(proveedorMapper::toDto)
                    .orElseThrow(() -> new EntityNotFoundException("Proveedor no encontrado"));
        } catch (Exception e) {
            log.error("Error al buscar proveedor por ID: {}", e.getMessage(), e);
            throw new RuntimeException("Error al buscar proveedor: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ProveedorDto findByRuc(String ruc) {
        try {
            return proveedorRepository.findByRuc(ruc)
                    .map(proveedorMapper::toDto)
                    .orElseThrow(() -> new EntityNotFoundException("Proveedor no encontrado con RUC: " + ruc));
        } catch (Exception e) {
            log.error("Error al buscar proveedor por RUC: {}", e.getMessage(), e);
            throw new RuntimeException("Error al buscar proveedor por RUC: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ProveedorDto findByEmail(String email) {
        try {
            return proveedorRepository.findByEmail(email)
                    .map(proveedorMapper::toDto)
                    .orElseThrow(() -> new EntityNotFoundException("Proveedor no encontrado con email: " + email));
        } catch (Exception e) {
            log.error("Error al buscar proveedor por email: {}", e.getMessage(), e);
            throw new RuntimeException("Error al buscar proveedor por email: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProveedorDto> findAll() {
        try {
            return proveedorRepository.findAll().stream()
                    .map(proveedorMapper::toDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error al obtener todos los proveedores: {}", e.getMessage(), e);
            throw new RuntimeException("Error al obtener proveedores: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProveedorDto> findByEstado(EstadoProveedor estado) {
        try {
            return proveedorRepository.findByEstado(estado).stream()
                    .map(proveedorMapper::toDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error al buscar proveedores por estado: {}", e.getMessage(), e);
            throw new RuntimeException("Error al buscar proveedores por estado: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProveedorDto> findByRazonSocialContainingIgnoreCase(String razonSocial) {
        try {
            return proveedorRepository.findByRazonSocialContainingIgnoreCase(razonSocial).stream()
                    .map(proveedorMapper::toDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error al buscar proveedores por razón social: {}", e.getMessage(), e);
            throw new RuntimeException("Error al buscar proveedores por razón social: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProveedorDto> findByRepresentanteLegalContainingIgnoreCase(String representanteLegal) {
        try {
            return proveedorRepository.findByRepresentanteLegalContainingIgnoreCase(representanteLegal).stream()
                    .map(proveedorMapper::toDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error al buscar proveedores por representante legal: {}", e.getMessage(), e);
            throw new RuntimeException("Error al buscar proveedores por representante legal: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProveedorDto> findTop10ByOrderByFechaRegistroDesc() {
        try {
            return proveedorRepository.findTop10ByOrderByFechaCreacionDesc().stream()
                    .map(proveedorMapper::toDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error al obtener últimos proveedores: {}", e.getMessage(), e);
            throw new RuntimeException("Error al obtener últimos proveedores: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return proveedorRepository.existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByRuc(String ruc) {
        return proveedorRepository.existsByRuc(ruc);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return proveedorRepository.existsByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public Long countByEstado(EstadoProveedor estado) {
        return proveedorRepository.countByEstado(estado);
    }

    @Override
    @Transactional(readOnly = true)
    public Long countTotal() {
        return proveedorRepository.count();
    }
}