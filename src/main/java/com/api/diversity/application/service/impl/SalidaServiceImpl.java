package com.api.diversity.application.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Set;
import java.util.HashSet;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.api.diversity.application.dto.SalidaDto;
import com.api.diversity.application.dto.DetalleSalidaDto;
import com.api.diversity.application.mappers.SalidaMapper;
import com.api.diversity.application.mappers.DetalleSalidaMapper;
import com.api.diversity.application.service.interfaces.ISalidaService;
import com.api.diversity.domain.model.SalidaEntity;
import com.api.diversity.domain.model.DetalleSalidaEntity;
import com.api.diversity.domain.model.ClienteEntity;
import com.api.diversity.domain.model.ProductoEntity;
import com.api.diversity.domain.model.UsuarioEntity;
import com.api.diversity.domain.ports.ISalidaRepository;
import com.api.diversity.domain.ports.IClienteRepository;
import com.api.diversity.domain.ports.IProductoRepository;
import com.api.diversity.domain.ports.IUsuarioRepository;
import com.api.diversity.domain.ports.IDetalleSalidaRepository;
import com.api.diversity.domain.enums.EstadoSalida;
import com.api.diversity.domain.enums.EstadoDetalleSalida;
import com.api.diversity.domain.enums.TipoDocumento;
import com.api.diversity.domain.enums.TipoRubro;
import com.api.diversity.application.service.interfaces.IRubroService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class SalidaServiceImpl implements ISalidaService {

    private final ISalidaRepository salidaRepository;
    private final IClienteRepository clienteRepository;
    private final IProductoRepository productoRepository;
    private final IUsuarioRepository usuarioRepository;
    private final IDetalleSalidaRepository detalleSalidaRepository;
    private final SalidaMapper salidaMapper;
    private final DetalleSalidaMapper detalleSalidaMapper;
    private final IRubroService rubroService;

    @Override
    @Transactional
    public SalidaDto save(SalidaDto salidaDto) {
        try {
            log.info("Guardando nueva salida: {}", salidaDto.getNumeroDocumento());

            // Validar cliente
            ClienteEntity cliente = clienteRepository.findById(salidaDto.getClienteId())
                    .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado"));

            // Validar usuario registro
            UsuarioEntity usuarioRegistro = usuarioRepository.findById(salidaDto.getUsuarioRegistroId())
                    .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

            // Validar que no haya productos duplicados en los detalles
            if (salidaDto.getDetalles() != null) {
                Set<Long> productosIds = new HashSet<>();
                for (DetalleSalidaDto detalleDto : salidaDto.getDetalles()) {
                    if (!productosIds.add(detalleDto.getProductoId())) {
                        throw new IllegalStateException(
                                "No se puede agregar el mismo producto más de una vez en la misma salida. " +
                                        "Producto ID: " + detalleDto.getProductoId());
                    }
                }
            }

            // La validación de stock se realizará en el momento de la aprobación

            // Crear entidad
            SalidaEntity salida = salidaMapper.toEntity(salidaDto);
            salida.setCliente(cliente);
            salida.setUsuarioRegistro(usuarioRegistro);
            salida.setEstado(EstadoSalida.Pendiente);
            salida.setFechaSalida(LocalDateTime.now());

            // Calcular total venta
            BigDecimal totalVenta = salidaDto.getDetalles().stream()
                    .map(detalle -> detalle.getPrecioUnitario().multiply(BigDecimal.valueOf(detalle.getCantidad())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            salida.setTotalVenta(totalVenta);

            // Guardar salida
            salida = salidaRepository.save(salida);

            // Guardar detalles
            if (salidaDto.getDetalles() != null) {
                for (DetalleSalidaDto detalleDto : salidaDto.getDetalles()) {
                    DetalleSalidaEntity detalle = detalleSalidaMapper.toEntity(detalleDto);
                    detalle.setSalida(salida);
                    detalle.setEstado(EstadoDetalleSalida.Activo);
                    detalle.setUsuarioRegistro(usuarioRegistro);

                    // Validar producto
                    ProductoEntity producto = productoRepository.findById(detalleDto.getProductoId())
                            .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado"));
                    detalle.setProducto(producto);

                    // Calcular subtotal
                    detalle.setSubtotal(
                            detalle.getPrecioUnitario().multiply(BigDecimal.valueOf(detalle.getCantidad())));

                    // validar que el producto no tenga

                    // Guardar detalle
                    detalleSalidaRepository.save(detalle);

                    // La actualización de stock se moverá al método de aprobación
                }
            }

            return salidaMapper.toDto(salida);
        } catch (Exception e) {
            log.error("Error al guardar salida: {}", e.getMessage(), e);
            throw new RuntimeException("Error al guardar salida: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public SalidaDto update(SalidaDto salidaDto) {
        try {
            log.info("Actualizando salida ID: {}", salidaDto.getIdSalida());

            SalidaEntity salidaExistente = salidaRepository.findById(salidaDto.getIdSalida())
                    .orElseThrow(() -> new EntityNotFoundException("Salida no encontrada"));

            // Solo permitir actualizar si está pendiente
            if (salidaExistente.getEstado() != EstadoSalida.Pendiente) {
                throw new IllegalStateException("Solo se pueden actualizar salidas pendientes");
            }

            // Actualizar campos básicos
            salidaExistente.setNumeroDocumento(salidaDto.getNumeroDocumento());
            salidaExistente.setMotivoSalida(salidaDto.getMotivoSalida());
            salidaExistente.setObservaciones(salidaDto.getObservaciones());

            // Recalcular total venta si hay detalles
            if (salidaDto.getDetalles() != null) {
                BigDecimal totalVenta = salidaDto.getDetalles().stream()
                        .map(detalle -> detalle.getPrecioUnitario().multiply(BigDecimal.valueOf(detalle.getCantidad())))
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                salidaExistente.setTotalVenta(totalVenta);
            }

            return salidaMapper.toDto(salidaRepository.save(salidaExistente));
        } catch (Exception e) {
            log.error("Error al actualizar salida: {}", e.getMessage(), e);
            throw new RuntimeException("Error al actualizar salida: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        try {
            if (!salidaRepository.existsById(id)) {
                throw new EntityNotFoundException("Salida no encontrada");
            }
            salidaRepository.deleteById(id);
            log.info("Salida eliminada ID: {}", id);
        } catch (Exception e) {
            log.error("Error al eliminar salida: {}", e.getMessage(), e);
            throw new RuntimeException("Error al eliminar salida: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public SalidaDto findById(Long id) {
        try {
            return salidaRepository.findById(id)
                    .map(salidaMapper::toDto)
                    .orElseThrow(() -> new EntityNotFoundException("Salida no encontrada"));
        } catch (Exception e) {
            log.error("Error al buscar salida por ID: {}", e.getMessage(), e);
            throw new RuntimeException("Error al buscar salida: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<SalidaDto> findAll() {
        try {
            return salidaRepository.findAll().stream()
                    .map(salidaMapper::toDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error al obtener todas las salidas: {}", e.getMessage(), e);
            throw new RuntimeException("Error al obtener salidas: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<SalidaDto> findByEstado(EstadoSalida estado) {
        try {
            return salidaRepository.findByEstado(estado).stream()
                    .map(salidaMapper::toDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error al buscar salidas por estado: {}", e.getMessage(), e);
            throw new RuntimeException("Error al buscar salidas por estado: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<SalidaDto> findByFechaSalidaBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        try {
            return salidaRepository.findByFechaSalidaBetween(fechaInicio, fechaFin).stream()
                    .map(salidaMapper::toDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error al buscar salidas por fecha: {}", e.getMessage(), e);
            throw new RuntimeException("Error al buscar salidas por fecha: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<SalidaDto> findByClienteId(Long clienteId) {
        try {
            return salidaRepository.findByClienteId(clienteId).stream()
                    .map(salidaMapper::toDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error al buscar salidas por cliente: {}", e.getMessage(), e);
            throw new RuntimeException("Error al buscar salidas por cliente: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<SalidaDto> findByUsuarioRegistroId(Long usuarioId) {
        try {
            return salidaRepository.findByUsuarioRegistroId(usuarioId).stream()
                    .map(salidaMapper::toDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error al buscar salidas por usuario: {}", e.getMessage(), e);
            throw new RuntimeException("Error al buscar salidas por usuario: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<SalidaDto> findByRubroId(Long rubroId) {
        try {
            return salidaRepository.findByRubroId(rubroId).stream()
                    .map(salidaMapper::toDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error al buscar salidas por rubro: {}", e.getMessage(), e);
            throw new RuntimeException("Error al buscar salidas por rubro: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<SalidaDto> findTop10ByOrderByFechaSalidaDesc() {
        try {
            return salidaRepository.findTop10ByOrderByFechaSalidaDesc().stream()
                    .map(salidaMapper::toDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error al obtener últimas salidas: {}", e.getMessage(), e);
            throw new RuntimeException("Error al obtener últimas salidas: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return salidaRepository.existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Long countByEstado(EstadoSalida estado) {
        return salidaRepository.countByEstado(estado);
    }

    @Override
    @Transactional(readOnly = true)
    public Long countByFechaSalidaBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return salidaRepository.countByFechaSalidaBetween(fechaInicio, fechaFin);
    }

    @Override
    @Transactional
    public SalidaDto aprobarSalida(Long id, Long usuarioAprobacionId) {
        try {
            log.info("Aprobando salida ID: {}", id);

            SalidaEntity salida = salidaRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Salida no encontrada"));

            if (salida.getEstado() != EstadoSalida.Pendiente) {
                throw new IllegalStateException(
                        "Solo se pueden aprobar salidas pendientes. Estado actual: " + salida.getEstado());
            }

            UsuarioEntity usuarioAprobacion = usuarioRepository.findById(usuarioAprobacionId)
                    .orElseThrow(() -> new EntityNotFoundException("Usuario de aprobación no encontrado"));

            // se valida el stock y se actualiza
            if (salida.getDetalles() != null) {
                for (DetalleSalidaEntity detalle : salida.getDetalles()) {
                    ProductoEntity producto = detalle.getProducto();
                    if (!validarStockDisponible(producto, detalle.getCantidad())) {
                        throw new IllegalStateException(
                                "Stock insuficiente para el producto: '" + producto.getNombreProducto() +
                                        "'. Stock disponible: " + producto.getStockActual() +
                                        ", Cantidad solicitada: " + detalle.getCantidad());
                    }
                    // Descontar stock
                    producto.setStockActual(producto.getStockActual() - detalle.getCantidad());
                    productoRepository.save(producto);
                    log.info("Stock actualizado para producto {}: {} - {} = {}",
                            producto.getNombreProducto(),
                            producto.getStockActual() + detalle.getCantidad(),
                            detalle.getCantidad(),
                            producto.getStockActual());
                }
            }

            salida.setEstado(EstadoSalida.Completado);
            salida.setUsuarioAprobacion(usuarioAprobacion);
            salida.setFechaAprobacion(LocalDateTime.now());

            return salidaMapper.toDto(salidaRepository.save(salida));
        } catch (Exception e) {
            log.error("Error al aprobar salida: {}", e.getMessage(), e);
            throw new RuntimeException("Error al aprobar salida: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public SalidaDto anularSalida(Long id, String motivo) {
        try {
            log.info("Anulando salida ID: {}", id);

            SalidaEntity salida = salidaRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Salida no encontrada"));

            if (salida.getEstado() == EstadoSalida.Anulado) {
                throw new IllegalStateException("La salida ya está anulada");
            }

            // Si la salida ya fue completada, se debe revertir el stock
            if (salida.getEstado() == EstadoSalida.Completado) {
                if (salida.getDetalles() != null) {
                    for (DetalleSalidaEntity detalle : salida.getDetalles()) {
                        ProductoEntity producto = detalle.getProducto();
                        producto.setStockActual(producto.getStockActual() + detalle.getCantidad());
                        productoRepository.save(producto);
                        log.info("Stock revertido para producto {}: {} + {} = {}",
                                producto.getNombreProducto(),
                                producto.getStockActual() - detalle.getCantidad(),
                                detalle.getCantidad(),
                                producto.getStockActual());
                    }
                }
            }

            salida.setEstado(EstadoSalida.Anulado);
            salida.setObservaciones(motivo != null ? motivo : "Salida anulada sin motivo específico.");

            return salidaMapper.toDto(salidaRepository.save(salida));
        } catch (Exception e) {
            log.error("Error al anular salida: {}", e.getMessage(), e);
            throw new RuntimeException("Error al anular salida: " + e.getMessage(), e);
        }
    }

    @Override
    public String generarNumeroDocumento(TipoDocumento tipoDocumento) {
        String prefijo;
        switch (tipoDocumento) {
            case BOLETA:
                prefijo = "BOL";
                break;
            case FACTURA:
                prefijo = "FAC";
                break;
            case NOTA_CREDITO:
                prefijo = "NCR";
                break;
            case NOTA_DEBITO:
                prefijo = "NDB";
                break;
            case GUIA_REMISION:
                prefijo = "GUI";
                break;
            default:
                prefijo = "DOC";
                break;
        }
        return prefijo + "-" + System.currentTimeMillis();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean validarStockDisponible(Long productoId, Integer cantidad) {
        try {
            ProductoEntity producto = productoRepository.findById(productoId)
                    .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado"));

            return validarStockDisponible(producto, cantidad);
        } catch (Exception e) {
            log.error("Error al validar stock disponible: {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * Método sobrecargado para validar stock disponible utilizando el producto
     * directamente
     */
    private boolean validarStockDisponible(ProductoEntity producto, Integer cantidad) {
        try {
            return producto.getStockActual() >= cantidad;
        } catch (Exception e) {
            log.error("Error al validar stock disponible: {}", e.getMessage(), e);
            return false;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByNumeroDocumento(String numeroDocumento) {
        try {
            return salidaRepository.existsByNumeroDocumento(numeroDocumento);
        } catch (Exception e) {
            log.error("Error al verificar existencia de número de documento: {}", e.getMessage(), e);
            return false;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<SalidaDto> findTop10ByTipoRubroOrderByFechaSalidaDesc(TipoRubro tipoRubro) {
        Long idRubro = rubroService.findByNombreRubro(tipoRubro.getNombre())
                .map(r -> r.getIdRubro())
                .orElse(null);
        if (idRubro == null) {
            return List.of();
        }
        return salidaRepository.findByRubroId(idRubro).stream()
                .sorted((s1, s2) -> s2.getFechaSalida().compareTo(s1.getFechaSalida()))
                .limit(10)
                .map(salidaMapper::toDto)
                .collect(java.util.stream.Collectors.toList());
    }
}