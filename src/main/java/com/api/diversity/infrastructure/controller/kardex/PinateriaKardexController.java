package com.api.diversity.infrastructure.controller.kardex;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

import com.api.diversity.application.service.interfaces.IEntradaService;
import com.api.diversity.application.service.interfaces.ISalidaService;
import com.api.diversity.application.service.interfaces.IProductoService;
import com.api.diversity.application.dto.EntradaDto;
import com.api.diversity.application.dto.SalidaDto;
import com.api.diversity.application.dto.ProductoDto;
import com.api.diversity.domain.enums.TipoRubro;

@Controller
@RequestMapping("/pinateria/kardex")
@RequiredArgsConstructor
@Slf4j
public class PinateriaKardexController {

    private final IEntradaService entradaService;
    private final ISalidaService salidaService;
    private final IProductoService productoService;

    /**
     * Dashboard del Kardex para Piñatería
     */
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        log.info("Accediendo al dashboard del Kardex - Piñatería");

        try {
            // Obtener productos de Piñatería
            List<ProductoDto> productosPinateria = productoService.findAllByRubro(TipoRubro.PIÑATERIA);

            // Calcular estadísticas
            int totalProductos = productosPinateria.size();
            int productosStockBajo = (int) productosPinateria.stream()
                    .filter(p -> p.getStockActual() != null && p.getStockActual() < 10)
                    .count();

            // Calcular valor total del inventario
            BigDecimal valorTotalInventario = productosPinateria.stream()
                    .filter(p -> p.getStockActual() != null && p.getPrecioVenta() != null)
                    .map(p -> p.getPrecioVenta().multiply(BigDecimal.valueOf(p.getStockActual())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            // Obtener últimas entradas de Piñatería (top 5)
            List<EntradaDto> ultimasEntradas = entradaService.findTop10ByOrderByFechaEntradaDesc()
                    .stream()
                    .limit(5)
                    .toList();

            // Obtener últimas salidas de Piñatería (top 5)
            List<SalidaDto> ultimasSalidas = salidaService.findTop10ByOrderByFechaSalidaDesc()
                    .stream()
                    .limit(5)
                    .toList();

            model.addAttribute("titulo", "Dashboard Kardex - Piñatería");
            model.addAttribute("subtitulo", "Resumen del inventario de Piñatería");
            model.addAttribute("rubro", "Piñatería");
            model.addAttribute("totalProductos", totalProductos);
            model.addAttribute("productosStockBajo", productosStockBajo);
            model.addAttribute("valorTotalInventario", valorTotalInventario);
            model.addAttribute("ultimasEntradas", ultimasEntradas);
            model.addAttribute("ultimasSalidas", ultimasSalidas);

        } catch (Exception e) {
            log.error("Error al cargar datos del dashboard de Piñatería: {}", e.getMessage());

            // En caso de error, usar valores por defecto
            model.addAttribute("titulo", "Dashboard Kardex - Piñatería");
            model.addAttribute("subtitulo", "Resumen del inventario de Piñatería");
            model.addAttribute("rubro", "Piñatería");
            model.addAttribute("totalProductos", 0);
            model.addAttribute("productosStockBajo", 0);
            model.addAttribute("valorTotalInventario", BigDecimal.ZERO);
            model.addAttribute("ultimasEntradas", new ArrayList<>());
            model.addAttribute("ultimasSalidas", new ArrayList<>());
            model.addAttribute("error", "Error al cargar los datos. Por favor, intente nuevamente.");
        }

        return "kardex/pinateria/dashboard/index";
    }

    /**
     * Nueva entrada para Piñatería
     */
    @GetMapping("/entrada/nueva")
    public String nuevaEntrada(Model model) {
        log.info("Accediendo al formulario de nueva entrada - Piñatería");

        try {
            // Cargar productos de Piñatería
            List<ProductoDto> productosPinateria = productoService.findAllByRubro(TipoRubro.PIÑATERIA);

            // Tipos de documento disponibles
            List<String> tiposDocumento = List.of("FACTURA", "BOLETA", "NOTA DE CRÉDITO", "NOTA DE DÉBITO",
                    "GUÍA DE REMISIÓN");

            // TODO: Cargar proveedores (necesitaríamos un servicio de proveedores)
            // Por ahora usamos una lista vacía
            List<Object> proveedores = new ArrayList<>();

            model.addAttribute("titulo", "Nueva Entrada - Piñatería");
            model.addAttribute("subtitulo", "Registrar entrada de productos de Piñatería");
            model.addAttribute("rubro", "Piñatería");
            model.addAttribute("productos", productosPinateria);
            model.addAttribute("tiposDocumento", tiposDocumento);
            model.addAttribute("proveedores", proveedores);

        } catch (Exception e) {
            log.error("Error al cargar datos para nueva entrada de Piñatería: {}", e.getMessage());

            model.addAttribute("titulo", "Nueva Entrada - Piñatería");
            model.addAttribute("subtitulo", "Registrar entrada de productos de Piñatería");
            model.addAttribute("rubro", "Piñatería");
            model.addAttribute("productos", new ArrayList<>());
            model.addAttribute("tiposDocumento", new ArrayList<>());
            model.addAttribute("proveedores", new ArrayList<>());
            model.addAttribute("error", "Error al cargar los datos. Por favor, intente nuevamente.");
        }

        return "kardex/pinateria/entrada/nueva";
    }

    /**
     * Nueva salida para Piñatería
     */
    @GetMapping("/salida/nueva")
    public String nuevaSalida(Model model) {
        log.info("Accediendo al formulario de nueva salida - Piñatería");

        try {
            // Cargar productos de Piñatería con stock
            List<ProductoDto> productosPinateria = productoService.findAllByRubro(TipoRubro.PIÑATERIA);
            List<ProductoDto> productosConStock = productosPinateria.stream()
                    .filter(p -> p.getStockActual() != null && p.getStockActual() > 0)
                    .toList();

            model.addAttribute("titulo", "Nueva Salida - Piñatería");
            model.addAttribute("subtitulo", "Registrar salida de productos de Piñatería");
            model.addAttribute("rubro", "Piñatería");
            model.addAttribute("productos", productosConStock);
            // model.addAttribute("clientes", clientes);

        } catch (Exception e) {
            log.error("Error al cargar datos para nueva salida de Piñatería: {}", e.getMessage());

            model.addAttribute("titulo", "Nueva Salida - Piñatería");
            model.addAttribute("subtitulo", "Registrar salida de productos de Piñatería");
            model.addAttribute("rubro", "Piñatería");
            model.addAttribute("productos", new ArrayList<>());
            model.addAttribute("error", "Error al cargar los datos. Por favor, intente nuevamente.");
        }

        return "kardex/pinateria/salida/nueva";
    }

    /**
     * Movimientos de Piñatería
     */
    @GetMapping("/movimientos")
    public String movimientos(Model model) {
        log.info("Accediendo a la lista de movimientos - Piñatería");

        try {
            // Obtener últimas entradas y salidas de Piñatería
            List<EntradaDto> entradas = entradaService.findTop10ByOrderByFechaEntradaDesc();
            List<SalidaDto> salidas = salidaService.findTop10ByOrderByFechaSalidaDesc();

            model.addAttribute("titulo", "Movimientos - Piñatería");
            model.addAttribute("subtitulo", "Historial de movimientos de Piñatería");
            model.addAttribute("rubro", "Piñatería");
            model.addAttribute("entradas", entradas);
            model.addAttribute("salidas", salidas);

        } catch (Exception e) {
            log.error("Error al cargar movimientos de Piñatería: {}", e.getMessage());

            model.addAttribute("titulo", "Movimientos - Piñatería");
            model.addAttribute("subtitulo", "Historial de movimientos de Piñatería");
            model.addAttribute("rubro", "Piñatería");
            model.addAttribute("entradas", new ArrayList<>());
            model.addAttribute("salidas", new ArrayList<>());
            model.addAttribute("error", "Error al cargar los movimientos. Por favor, intente nuevamente.");
        }

        return "kardex/pinateria/movimiento/lista";
    }

    /**
     * Detalle de movimiento de Piñatería
     */
    @GetMapping("/movimientos/{id}")
    public String detalleMovimiento(Long id, Model model) {
        log.info("Accediendo al detalle del movimiento ID: {} - Piñatería", id);

        // TODO: Agregar lógica para cargar:
        // - Detalles del movimiento de piñatería
        // - Lista de productos involucrados
        // - Información del proveedor/cliente

        model.addAttribute("titulo", "Detalle de Movimiento - Piñatería");
        model.addAttribute("subtitulo", "Información detallada del movimiento");
        model.addAttribute("movimientoId", id);
        model.addAttribute("rubro", "Piñatería");

        return "kardex/pinateria/movimiento/detalle";
    }

    /**
     * Reporte de inventario de Piñatería
     */
    @GetMapping("/reporte")
    public String reporteInventario(Model model) {
        log.info("Accediendo al reporte de inventario - Piñatería");

        try {
            // Obtener productos de Piñatería
            List<ProductoDto> productosPinateria = productoService.findAllByRubro(TipoRubro.PIÑATERIA);

            // Productos con stock bajo
            List<ProductoDto> productosStockBajo = productosPinateria.stream()
                    .filter(p -> p.getStockActual() != null
                            && p.getStockActual() < (p.getStockMinimo() != null ? p.getStockMinimo() : 10))
                    .toList();

            // Calcular estadísticas
            int totalProductos = productosPinateria.size();
            int productosConStock = (int) productosPinateria.stream()
                    .filter(p -> p.getStockActual() != null && p.getStockActual() > 0)
                    .count();
            int productosSinStock = totalProductos - productosConStock;

            // Valor total del inventario
            BigDecimal valorTotalInventario = productosPinateria.stream()
                    .filter(p -> p.getStockActual() != null && p.getPrecioVenta() != null)
                    .map(p -> p.getPrecioVenta().multiply(BigDecimal.valueOf(p.getStockActual())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            // Obtener movimientos recientes
            List<EntradaDto> entradasRecientes = entradaService.findTop10ByOrderByFechaEntradaDesc();
            List<SalidaDto> salidasRecientes = salidaService.findTop10ByOrderByFechaSalidaDesc();

            model.addAttribute("titulo", "Reporte de Inventario - Piñatería");
            model.addAttribute("subtitulo", "Análisis y estadísticas de Piñatería");
            model.addAttribute("rubro", "Piñatería");
            model.addAttribute("productos", productosPinateria);
            model.addAttribute("productosStockBajo", productosStockBajo);
            model.addAttribute("totalProductos", totalProductos);
            model.addAttribute("productosConStock", productosConStock);
            model.addAttribute("productosSinStock", productosSinStock);
            model.addAttribute("valorTotalInventario", valorTotalInventario);
            model.addAttribute("entradasRecientes", entradasRecientes);
            model.addAttribute("salidasRecientes", salidasRecientes);

        } catch (Exception e) {
            log.error("Error al cargar reporte de inventario de Piñatería: {}", e.getMessage());

            model.addAttribute("titulo", "Reporte de Inventario - Piñatería");
            model.addAttribute("subtitulo", "Análisis y estadísticas de Piñatería");
            model.addAttribute("rubro", "Piñatería");
            model.addAttribute("productos", new ArrayList<>());
            model.addAttribute("productosStockBajo", new ArrayList<>());
            model.addAttribute("totalProductos", 0);
            model.addAttribute("productosConStock", 0);
            model.addAttribute("productosSinStock", 0);
            model.addAttribute("valorTotalInventario", BigDecimal.ZERO);
            model.addAttribute("entradasRecientes", new ArrayList<>());
            model.addAttribute("salidasRecientes", new ArrayList<>());
            model.addAttribute("error", "Error al cargar el reporte. Por favor, intente nuevamente.");
        }

        return "kardex/pinateria/reporte/index";
    }
}