package com.api.diversity.application.service.impl;

import com.api.diversity.application.dto.DevolucionRequestDto;
import com.api.diversity.application.dto.EntradaDto;
import com.api.diversity.application.dto.SalidaDto;
import com.api.diversity.application.mappers.EntradaMapper;
import com.api.diversity.application.mappers.SalidaMapper;
import com.api.diversity.application.service.interfaces.DevolucionService;
import com.api.diversity.domain.enums.EstadoEntrada;
import com.api.diversity.domain.enums.TipoEntrada;
import com.api.diversity.domain.model.DetalleEntradaEntity;
import com.api.diversity.domain.model.DetalleSalidaEntity;
import com.api.diversity.domain.model.EntradaEntity;
import com.api.diversity.domain.model.ProductoEntity;
import com.api.diversity.domain.model.SalidaEntity;
import com.api.diversity.domain.model.UsuarioEntity;
import com.api.diversity.infrastructure.repository.IDetalleEntradaJpaRepository;
import com.api.diversity.infrastructure.repository.IEntradaJpaRepository;
import com.api.diversity.infrastructure.repository.IProductoJpaRepository;
import com.api.diversity.infrastructure.repository.ISalidaJpaRepository;
import com.api.diversity.infrastructure.repository.UsuarioJpaRepository;
import com.api.diversity.infrastructure.security.SecurityContext;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DevolucionServiceImpl implements DevolucionService {

    private final ISalidaJpaRepository salidaJpaRepository;
    private final IProductoJpaRepository productoJpaRepository;
    private final IEntradaJpaRepository entradaJpaRepository;
    private final IDetalleEntradaJpaRepository detalleEntradaJpaRepository;
    private final UsuarioJpaRepository usuarioJpaRepository;
    private final SecurityContext securityContext;
    private final EntradaMapper entradaMapper;
    private final SalidaMapper salidaMapper;

    @Override
    @Transactional
    public EntradaDto procesarDevolucion(DevolucionRequestDto devolucionRequest) {
        SalidaEntity salidaOriginal = salidaJpaRepository.findById(devolucionRequest.getIdSalidaOriginal())
                .orElseThrow(() -> new RuntimeException("Salida original no encontrada"));

        // validar que los productos a devolver realmente existan en la salida original
        Map<Long, Integer> productosVendidos = salidaOriginal.getDetalles().stream()
                .collect(Collectors.toMap(detalle -> detalle.getProducto().getIdProducto(),
                        DetalleSalidaEntity::getCantidad));

        // calcular cantidades ya devueltas para esta salida jajajaja tengo sueño pierdo
        // la cordura
        Map<Long, Integer> productosYaDevueltos = new HashMap<>();
        List<EntradaEntity> devolucionesPrevias = entradaJpaRepository
                .findByTipoEntradaAndIdSalidaReferencia(TipoEntrada.DEVOLUCION, salidaOriginal.getIdSalida());

        for (EntradaEntity devPrev : devolucionesPrevias) {
            for (DetalleEntradaEntity detEntrada : devPrev.getDetalles()) {
                productosYaDevueltos.merge(detEntrada
                        .getProducto().getIdProducto(),
                        detEntrada.getCantidad(),
                        Integer::sum);
            }
        }

        // Validar la solicitud de devolución actual
        for (var detalleDevolucion : devolucionRequest.getProductosDevueltos()) {
            Long idProducto = detalleDevolucion.getIdProducto();
            Integer cantidadDevolver = detalleDevolucion.getCantidad();

            if (!productosVendidos.containsKey(idProducto)) {
                throw new RuntimeException("El producto con ID " + idProducto + " no fue parte de la salida original.");
            }

            Integer cantidadOriginalVendida = productosVendidos.get(idProducto);
            Integer cantidadPreviamenteDevuelta = productosYaDevueltos.getOrDefault(idProducto, 0);

            if (cantidadDevolver <= 0) {
                throw new RuntimeException(
                        "La cantidad a devolver para el producto " + idProducto + " debe ser mayor a cero.");
            }

            if ((cantidadPreviamenteDevuelta + cantidadDevolver) > cantidadOriginalVendida) {
                throw new RuntimeException("La cantidad total a devolver para el producto " + idProducto +
                        " (" + (cantidadPreviamenteDevuelta + cantidadDevolver)
                        + ") excede la cantidad vendida originalmente (" +
                        cantidadOriginalVendida + ").");
            }
        }

        EntradaEntity nuevaEntrada = new EntradaEntity();
        nuevaEntrada.setNumeroFactura("DEV-" + salidaOriginal.getNumeroDocumento());
        nuevaEntrada.setTipoDocumento(salidaOriginal.getTipoDocumento());
        nuevaEntrada.setTipoEntrada(TipoEntrada.DEVOLUCION);
        nuevaEntrada.setIdSalidaReferencia(salidaOriginal.getIdSalida());
        nuevaEntrada.setProveedor(null);
        nuevaEntrada.setFechaEntrada(LocalDateTime.now());
        nuevaEntrada.setEstado(EstadoEntrada.Completado);
        UsuarioEntity usuarioRegistro = usuarioJpaRepository.findById(securityContext.getCurrentUserId())
                .orElseThrow(() -> new RuntimeException("Usuario de registro no encontrado"));
        nuevaEntrada.setUsuarioRegistro(usuarioRegistro);
        nuevaEntrada.setUsuarioAprobacion(usuarioRegistro);
        nuevaEntrada.setFechaAprobacion(LocalDateTime.now());
        nuevaEntrada.setObservaciones(devolucionRequest.getMotivoDevolucion());

        BigDecimal costoTotalDevolucion = BigDecimal.ZERO;
        List<DetalleEntradaEntity> detallesEntrada = new ArrayList<>();

        for (var detalleDevolucion : devolucionRequest.getProductosDevueltos()) {
            ProductoEntity producto = productoJpaRepository.findById(detalleDevolucion.getIdProducto())
                    .orElseThrow(
                            () -> new RuntimeException("Producto no encontrado: " + detalleDevolucion.getIdProducto()));

            // Actualizar stock del producto
            producto.setStockActual(producto.getStockActual() + detalleDevolucion.getCantidad());
            productoJpaRepository.save(producto);

            DetalleEntradaEntity detalleEntrada = new DetalleEntradaEntity();
            detalleEntrada.setEntrada(nuevaEntrada);
            detalleEntrada.setProducto(producto);
            detalleEntrada.setCantidad(detalleDevolucion.getCantidad());
            detalleEntrada.setPrecioUnitario(detalleDevolucion.getPrecioUnitario());
            BigDecimal subtotal = detalleDevolucion.getPrecioUnitario()
                    .multiply(BigDecimal.valueOf(detalleDevolucion.getCantidad()));
            detalleEntrada.setSubtotal(subtotal);
            detalleEntrada.setUsuarioRegistro(usuarioRegistro);
            detalleEntrada.setFechaRegistro(LocalDateTime.now());

            costoTotalDevolucion = costoTotalDevolucion.add(subtotal);
            detallesEntrada.add(detalleEntrada);
        }

        nuevaEntrada.setCostoTotal(costoTotalDevolucion);
        nuevaEntrada.setDetalles(detallesEntrada);

        EntradaEntity savedEntrada = entradaJpaRepository.save(nuevaEntrada);
        detalleEntradaJpaRepository.saveAll(detallesEntrada);

        return entradaMapper.toDto(savedEntrada);
    }

    @Override
    public SalidaDto buscarSalidaPorIdONumeroDocumento(Long idSalida, String numeroDocumento) {
        SalidaEntity salida = null;
        if (idSalida != null) {
            salida = salidaJpaRepository.findById(idSalida).orElse(null);
        } else if (numeroDocumento != null && !numeroDocumento.isEmpty()) {
            List<SalidaEntity> resultados = salidaJpaRepository.findAll();
            for (SalidaEntity s : resultados) {
                if (s.getNumeroDocumento().equalsIgnoreCase(numeroDocumento)) {
                    salida = s;
                    break;
                }
            }
        }
        if (salida == null) {
            throw new RuntimeException("No se encontró la salida con los datos proporcionados");
        }
        return salidaMapper.toDto(salida);
    }
}
