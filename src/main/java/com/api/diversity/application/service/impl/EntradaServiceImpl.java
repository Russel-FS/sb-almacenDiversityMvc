package com.api.diversity.application.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.api.diversity.application.dto.EntradaDto;
import com.api.diversity.application.dto.DetalleEntradaDto;
import com.api.diversity.application.mappers.EntradaMapper;
import com.api.diversity.application.mappers.DetalleEntradaMapper;
import com.api.diversity.application.service.interfaces.IEntradaService;
import com.api.diversity.domain.model.EntradaEntity;
import com.api.diversity.domain.model.DetalleEntradaEntity;
import com.api.diversity.domain.model.ProveedorEntity;
import com.api.diversity.domain.model.ProductoEntity;
import com.api.diversity.domain.model.UsuarioEntity;
import com.api.diversity.domain.ports.IEntradaRepository;
import com.api.diversity.domain.ports.IProveedorRepository;
import com.api.diversity.domain.ports.IProductoRepository;
import com.api.diversity.domain.ports.IUsuarioRepository;
import com.api.diversity.domain.enums.EstadoEntrada;
import com.api.diversity.domain.enums.EstadoDetalleEntrada;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class EntradaServiceImpl implements IEntradaService {

    private final IEntradaRepository entradaRepository;
    private final IProveedorRepository proveedorRepository;
    private final IProductoRepository productoRepository;
    private final IUsuarioRepository usuarioRepository;
    private final EntradaMapper entradaMapper;
    private final DetalleEntradaMapper detalleEntradaMapper;

    @Override
    @Transactional
    public EntradaDto save(EntradaDto entradaDto) {
        try {
            log.info("Guardando nueva entrada: {}", entradaDto.getNumeroFactura());

            // Validar proveedor
            ProveedorEntity proveedor = proveedorRepository.findById(entradaDto.getProveedorId())
                    .orElseThrow(() -> new EntityNotFoundException("Proveedor no encontrado"));

            // Validar usuario registro
            UsuarioEntity usuarioRegistro = usuarioRepository.findById(entradaDto.getUsuarioRegistroId())
                    .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

            // Crear entidad
            EntradaEntity entrada = entradaMapper.toEntity(entradaDto);
            entrada.setProveedor(proveedor);
            entrada.setUsuarioRegistro(usuarioRegistro);
            entrada.setEstado(EstadoEntrada.Pendiente);
            entrada.setFechaEntrada(LocalDateTime.now());

            // Calcular costo total
            BigDecimal costoTotal = entradaDto.getDetalles().stream()
                    .map(detalle -> detalle.getPrecioUnitario().multiply(BigDecimal.valueOf(detalle.getCantidad())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            entrada.setCostoTotal(costoTotal);

            // Guardar entrada
            entrada = entradaRepository.save(entrada);

            // Guardar detalles
            if (entradaDto.getDetalles() != null) {
                for (DetalleEntradaDto detalleDto : entradaDto.getDetalles()) {
                    DetalleEntradaEntity detalle = detalleEntradaMapper.toEntity(detalleDto);
                    detalle.setEntrada(entrada);
                    detalle.setEstado(EstadoDetalleEntrada.Activo);

                    // Validar producto
                    ProductoEntity producto = productoRepository.findById(detalleDto.getProductoId())
                            .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado"));
                    detalle.setProducto(producto);

                    // Calcular subtotal
                    detalle.setSubtotal(
                            detalle.getPrecioUnitario().multiply(BigDecimal.valueOf(detalle.getCantidad())));
                }
            }

            return entradaMapper.toDto(entrada);
        } catch (Exception e) {
            log.error("Error al guardar entrada: {}", e.getMessage(), e);
            throw new RuntimeException("Error al guardar entrada: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public EntradaDto update(EntradaDto entradaDto) {
        try {
            log.info("Actualizando entrada ID: {}", entradaDto.getIdEntrada());

            EntradaEntity entradaExistente = entradaRepository.findById(entradaDto.getIdEntrada())
                    .orElseThrow(() -> new EntityNotFoundException("Entrada no encontrada"));

            // Solo permitir actualizar si está pendiente
            if (entradaExistente.getEstado() != EstadoEntrada.Pendiente) {
                throw new IllegalStateException("Solo se pueden actualizar entradas pendientes");
            }

            // Actualizar campos básicos
            entradaExistente.setNumeroFactura(entradaDto.getNumeroFactura());
            entradaExistente.setTipoDocumento(entradaDto.getTipoDocumento());
            entradaExistente.setObservaciones(entradaDto.getObservaciones());

            // Recalcular costo total si hay detalles
            if (entradaDto.getDetalles() != null) {
                BigDecimal costoTotal = entradaDto.getDetalles().stream()
                        .map(detalle -> detalle.getPrecioUnitario().multiply(BigDecimal.valueOf(detalle.getCantidad())))
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                entradaExistente.setCostoTotal(costoTotal);
            }

            return entradaMapper.toDto(entradaRepository.save(entradaExistente));
        } catch (Exception e) {
            log.error("Error al actualizar entrada: {}", e.getMessage(), e);
            throw new RuntimeException("Error al actualizar entrada: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        try {
            if (!entradaRepository.existsById(id)) {
                throw new EntityNotFoundException("Entrada no encontrada");
            }
            entradaRepository.deleteById(id);
            log.info("Entrada eliminada ID: {}", id);
        } catch (Exception e) {
            log.error("Error al eliminar entrada: {}", e.getMessage(), e);
            throw new RuntimeException("Error al eliminar entrada: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public EntradaDto findById(Long id) {
        try {
            return entradaRepository.findById(id)
                    .map(entradaMapper::toDto)
                    .orElseThrow(() -> new EntityNotFoundException("Entrada no encontrada"));
        } catch (Exception e) {
            log.error("Error al buscar entrada por ID: {}", e.getMessage(), e);
            throw new RuntimeException("Error al buscar entrada: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<EntradaDto> findAll() {
        try {
            return entradaRepository.findAll().stream()
                    .map(entradaMapper::toDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error al obtener todas las entradas: {}", e.getMessage(), e);
            throw new RuntimeException("Error al obtener entradas: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<EntradaDto> findByEstado(EstadoEntrada estado) {
        try {
            return entradaRepository.findByEstado(estado).stream()
                    .map(entradaMapper::toDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error al buscar entradas por estado: {}", e.getMessage(), e);
            throw new RuntimeException("Error al buscar entradas por estado: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<EntradaDto> findByFechaEntradaBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        try {
            return entradaRepository.findByFechaEntradaBetween(fechaInicio, fechaFin).stream()
                    .map(entradaMapper::toDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error al buscar entradas por fecha: {}", e.getMessage(), e);
            throw new RuntimeException("Error al buscar entradas por fecha: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<EntradaDto> findByProveedorId(Long proveedorId) {
        try {
            return entradaRepository.findByProveedorId(proveedorId).stream()
                    .map(entradaMapper::toDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error al buscar entradas por proveedor: {}", e.getMessage(), e);
            throw new RuntimeException("Error al buscar entradas por proveedor: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<EntradaDto> findByUsuarioRegistroId(Long usuarioId) {
        try {
            return entradaRepository.findByUsuarioRegistroId(usuarioId).stream()
                    .map(entradaMapper::toDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error al buscar entradas por usuario: {}", e.getMessage(), e);
            throw new RuntimeException("Error al buscar entradas por usuario: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<EntradaDto> findByRubroId(Long rubroId) {
        try {
            return entradaRepository.findByRubroId(rubroId).stream()
                    .map(entradaMapper::toDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error al buscar entradas por rubro: {}", e.getMessage(), e);
            throw new RuntimeException("Error al buscar entradas por rubro: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<EntradaDto> findTop10ByOrderByFechaEntradaDesc() {
        try {
            return entradaRepository.findTop10ByOrderByFechaEntradaDesc().stream()
                    .map(entradaMapper::toDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error al obtener últimas entradas: {}", e.getMessage(), e);
            throw new RuntimeException("Error al obtener últimas entradas: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return entradaRepository.existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Long countByEstado(EstadoEntrada estado) {
        return entradaRepository.countByEstado(estado);
    }

    @Override
    @Transactional(readOnly = true)
    public Long countByFechaEntradaBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return entradaRepository.countByFechaEntradaBetween(fechaInicio, fechaFin);
    }

    @Override
    @Transactional
    public EntradaDto aprobarEntrada(Long id, Long usuarioAprobacionId) {
        try {
            log.info("Aprobando entrada ID: {}", id);

            EntradaEntity entrada = entradaRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Entrada no encontrada"));

            if (entrada.getEstado() != EstadoEntrada.Pendiente) {
                throw new IllegalStateException("Solo se pueden aprobar entradas pendientes");
            }

            UsuarioEntity usuarioAprobacion = usuarioRepository.findById(usuarioAprobacionId)
                    .orElseThrow(() -> new EntityNotFoundException("Usuario de aprobación no encontrado"));

            entrada.setEstado(EstadoEntrada.Completado);
            entrada.setUsuarioAprobacion(usuarioAprobacion);
            entrada.setFechaAprobacion(LocalDateTime.now());

            // Actualizar stock de productos
            if (entrada.getDetalles() != null) {
                for (DetalleEntradaEntity detalle : entrada.getDetalles()) {
                    ProductoEntity producto = detalle.getProducto();
                    producto.setStockActual(producto.getStockActual() + detalle.getCantidad());
                    productoRepository.save(producto);
                }
            }

            return entradaMapper.toDto(entradaRepository.save(entrada));
        } catch (Exception e) {
            log.error("Error al aprobar entrada: {}", e.getMessage(), e);
            throw new RuntimeException("Error al aprobar entrada: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public EntradaDto anularEntrada(Long id, String motivo) {
        try {
            log.info("Anulando entrada ID: {}", id);

            EntradaEntity entrada = entradaRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Entrada no encontrada"));

            if (entrada.getEstado() == EstadoEntrada.Anulado) {
                throw new IllegalStateException("La entrada ya está anulada");
            }

            entrada.setEstado(EstadoEntrada.Anulado);
            entrada.setObservaciones(motivo);

            return entradaMapper.toDto(entradaRepository.save(entrada));
        } catch (Exception e) {
            log.error("Error al anular entrada: {}", e.getMessage(), e);
            throw new RuntimeException("Error al anular entrada: " + e.getMessage(), e);
        }
    }

    @Override
    public String generarNumeroFactura() {
        // Lógica para generar número de factura único
        return "FAC-" + System.currentTimeMillis();
    }
}