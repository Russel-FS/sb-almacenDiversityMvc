package com.api.diversity.infrastructure.controller.kardex;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;
import java.util.Map;
import java.util.HashMap;

import com.api.diversity.application.service.interfaces.IEntradaService;
import com.api.diversity.application.service.interfaces.ISalidaService;
import com.api.diversity.application.service.interfaces.IProductoService;
import com.api.diversity.application.dto.EntradaDto;
import com.api.diversity.application.dto.SalidaDto;
import com.api.diversity.application.dto.ProductoDto;
import com.api.diversity.domain.enums.TipoRubro;
import com.api.diversity.domain.enums.TipoDocumento;

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

            // Obtener últimas entradas de Piñatería
            List<EntradaDto> ultimasEntradas = entradaService.findTop10ByOrderByFechaEntradaDesc()
                    .stream()
                    .limit(5)
                    .toList();

            // Obtener últimas salidas de Piñatería
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
            List<TipoDocumento> tiposDocumento = List.of(TipoDocumento.values());

            // Lista de ejemplo para proveedores
            List<Map<String, Object>> proveedores = new ArrayList<>();
            Map<String, Object> proveedorEjemplo = new HashMap<>();
            proveedorEjemplo.put("id", 1L);
            proveedorEjemplo.put("razonSocial", "Proveedor Demo");
            proveedorEjemplo.put("ruc", "12345678901");
            proveedores.add(proveedorEjemplo);

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

            // Tipos de documento disponibles
            List<TipoDocumento> tiposDocumento = List.of(TipoDocumento.values());

            // Lista de ejemplo para clientes
            List<Map<String, Object>> clientes = new ArrayList<>();
            Map<String, Object> clienteEjemplo = new HashMap<>();
            clienteEjemplo.put("id", 1L);
            clienteEjemplo.put("nombreCompleto", "Cliente Demo");
            clienteEjemplo.put("dni", "12345678");
            clientes.add(clienteEjemplo);

            model.addAttribute("titulo", "Nueva Salida - Piñatería");
            model.addAttribute("subtitulo", "Registrar salida de productos de Piñatería");
            model.addAttribute("rubro", "Piñatería");
            model.addAttribute("productos", productosConStock);
            model.addAttribute("tiposDocumento", tiposDocumento);
            model.addAttribute("clientes", clientes);

        } catch (Exception e) {
            log.error("Error al cargar datos para nueva salida de Piñatería: {}", e.getMessage());

            model.addAttribute("titulo", "Nueva Salida - Piñatería");
            model.addAttribute("subtitulo", "Registrar salida de productos de Piñatería");
            model.addAttribute("rubro", "Piñatería");
            model.addAttribute("productos", new ArrayList<>());
            model.addAttribute("tiposDocumento", new ArrayList<>());
            model.addAttribute("clientes", new ArrayList<>());
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

    /**
     * Procesar nueva entrada para Piñatería
     */
    @PostMapping("/entrada/guardar")
    public String guardarEntrada(
            @RequestParam(value = "numeroFactura", required = false) String numeroFactura,
            @RequestParam("tipoDocumento") TipoDocumento tipoDocumento,
            @RequestParam("proveedorId") Long proveedorId,
            @RequestParam("fechaEntrada") String fechaEntrada,
            @RequestParam("observaciones") String observaciones,
            @RequestParam("productos") List<Map<String, Object>> productos,
            RedirectAttributes redirectAttributes) {

        log.info("Procesando nueva entrada - Piñatería: {}", numeroFactura);

        try {
            // Si el usuario no ingresa número, se genera automáticamente
            if (numeroFactura == null || numeroFactura.trim().isEmpty()) {
                numeroFactura = entradaService.generarNumeroDocumento(tipoDocumento);
            } else {
                // Validar que no exista
                if (entradaService.existsByNumeroFactura(numeroFactura)) {
                    redirectAttributes.addFlashAttribute("error", "El número de factura/documento ya existe");
                    return "redirect:/pinateria/kardex/entrada/nueva";
                }
            }

            // TODO: Implementar lógica para guardar entrada
            // 1. Crear EntradaDto con los datos del formulario
            // 2. Crear DetalleEntradaDto para cada producto
            // 3. Llamar a entradaService.save()

            redirectAttributes.addFlashAttribute("success",
                    "Entrada registrada exitosamente. Número: " + numeroFactura);
            return "redirect:/pinateria/kardex/dashboard";

        } catch (Exception e) {
            log.error("Error al guardar entrada: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Error al guardar la entrada: " + e.getMessage());
            return "redirect:/pinateria/kardex/entrada/nueva";
        }
    }

    /**
     * Procesar nueva salida para Piñatería
     */
    @PostMapping("/salida/guardar")
    public String guardarSalida(
            @RequestParam(value = "numeroDocumento", required = false) String numeroDocumento,
            @RequestParam("tipoDocumento") TipoDocumento tipoDocumento,
            @RequestParam("clienteId") Long clienteId,
            @RequestParam("fechaSalida") String fechaSalida,
            @RequestParam("motivoSalida") String motivoSalida,
            @RequestParam("observaciones") String observaciones,
            @RequestParam("productos") List<Map<String, Object>> productos,
            RedirectAttributes redirectAttributes) {

        log.info("Procesando nueva salida - Piñatería");

        try {
            // Si el usuario no ingresa número, se genera automáticamente
            if (numeroDocumento == null || numeroDocumento.trim().isEmpty()) {
                numeroDocumento = salidaService.generarNumeroDocumento(tipoDocumento);
            } else {
                // Validar que no exista
                if (salidaService.existsByNumeroDocumento(numeroDocumento)) {
                    redirectAttributes.addFlashAttribute("error", "El número de documento ya existe");
                    return "redirect:/pinateria/kardex/salida/nueva";
                }
            }

            // TODO: Implementar lógica para guardar salida
            // 1. Crear SalidaDto con los datos del formulario
            // 2. Crear DetalleSalidaDto para cada producto
            // 3. Llamar a salidaService.save()

            redirectAttributes.addFlashAttribute("success",
                    "Salida registrada exitosamente. Número: " + numeroDocumento);
            return "redirect:/pinateria/kardex/dashboard";

        } catch (Exception e) {
            log.error("Error al guardar salida: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Error al guardar la salida: " + e.getMessage());
            return "redirect:/pinateria/kardex/salida/nueva";
        }
    }
}