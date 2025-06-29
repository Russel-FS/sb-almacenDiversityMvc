package com.api.diversity.infrastructure.controller.kardex;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.api.diversity.application.service.interfaces.IEntradaService;
import com.api.diversity.application.service.interfaces.ISalidaService;
import com.api.diversity.application.service.interfaces.IProductoService;
import com.api.diversity.application.service.interfaces.IProveedorService;
import com.api.diversity.application.service.interfaces.IClienteService;
import com.api.diversity.application.dto.EntradaDto;
import com.api.diversity.application.dto.SalidaDto;
import com.api.diversity.application.dto.ProductoDto;
import com.api.diversity.application.dto.ProveedorDto;
import com.api.diversity.application.dto.ClienteDto;
import com.api.diversity.application.dto.EntradaFormDto;
import com.api.diversity.application.dto.SalidaFormDto;
import com.api.diversity.application.dto.DetalleEntradaDto;
import com.api.diversity.application.dto.DetalleSalidaDto;
import com.api.diversity.domain.enums.TipoRubro;
import com.api.diversity.domain.enums.TipoDocumento;
import com.api.diversity.domain.enums.EstadoProveedor;
import com.api.diversity.domain.enums.EstadoCliente;
import com.api.diversity.infrastructure.security.SecurityContext;

@Controller
@RequestMapping("/pinateria/kardex")
@RequiredArgsConstructor
@Slf4j
public class PinateriaKardexController {

    private final IEntradaService entradaService;
    private final ISalidaService salidaService;
    private final IProductoService productoService;
    private final IProveedorService proveedorService;
    private final IClienteService clienteService;
    private final SecurityContext securityContext;

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

            // Obtener proveedores activos
            List<ProveedorDto> proveedores = proveedorService.findByEstado(EstadoProveedor.Activo);

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
        try {
            log.info("Cargando formulario de nueva salida para Piñatería");

            // Obtener productos activos de Piñatería
            List<ProductoDto> productos = productoService.findAllByRubro(TipoRubro.PIÑATERIA);
            log.info("Productos encontrados: {}", productos.size());

            // Obtener clientes activos
            List<ClienteDto> clientes = clienteService.findByEstado(EstadoCliente.Activo);
            log.info("Clientes activos encontrados: {}", clientes.size());

            // Generar número de documento
            String numeroDocumento = salidaService.generarNumeroDocumento(TipoDocumento.BOLETA);

            model.addAttribute("productos", productos);
            model.addAttribute("clientes", clientes);
            model.addAttribute("numeroDocumento", numeroDocumento);
            model.addAttribute("tiposDocumento", TipoDocumento.values());

            return "kardex/pinateria/salida/nueva";
        } catch (Exception e) {
            log.error("Error al cargar formulario de nueva salida: {}", e.getMessage(), e);
            model.addAttribute("error", "Error al cargar el formulario: " + e.getMessage());
            return "redirect:/pinateria/kardex/dashboard";
        }
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

            log.info("Entradas encontradas: {}", entradas != null ? entradas.size() : 0);
            log.info("Salidas encontradas: {}", salidas != null ? salidas.size() : 0);

            // Validar que las listas no sean null
            if (entradas == null) {
                entradas = new ArrayList<>();
            }
            if (salidas == null) {
                salidas = new ArrayList<>();
            }

            model.addAttribute("titulo", "Movimientos - Piñatería");
            model.addAttribute("subtitulo", "Historial de movimientos de Piñatería");
            model.addAttribute("rubro", "Piñatería");
            model.addAttribute("entradas", entradas);
            model.addAttribute("salidas", salidas);

        } catch (Exception e) {
            log.error("Error al cargar movimientos de Piñatería: {}", e.getMessage(), e);
            List<EntradaDto> entradas = new ArrayList<>();
            List<SalidaDto> salidas = new ArrayList<>();
            model.addAttribute("titulo", "Movimientos - Piñatería");
            model.addAttribute("subtitulo", "Historial de movimientos de Piñatería");
            model.addAttribute("rubro", "Piñatería");
            model.addAttribute("entradas", entradas);
            model.addAttribute("salidas", salidas);
            model.addAttribute("error", "Error al cargar los movimientos. Por favor, intente nuevamente.");
        }

        return "kardex/pinateria/movimiento/lista";
    }

    /**
     * Detalle de movimiento de Piñatería (general - mantiene compatibilidad)
     */
    @GetMapping("/movimientos/{id}")
    public String detalleMovimiento(@PathVariable Long id, Model model) {
        log.info("Accediendo al detalle del movimiento ID: {} - Piñatería", id);

        try {
            // Intentar buscar como entrada primero
            EntradaDto entrada = entradaService.findById(id);
            if (entrada != null) {
                log.info("Movimiento encontrado como entrada: {}", entrada.getNumeroFactura());
                return "redirect:/pinateria/kardex/entrada/" + id;
            }

            // Si no es entrada, buscar como salida
            SalidaDto salida = salidaService.findById(id);
            if (salida != null) {
                log.info("Movimiento encontrado como salida: {}", salida.getNumeroDocumento());
                return "redirect:/pinateria/kardex/salida/" + id;
            }

            // Si no se encuentra, mostrar error
            log.warn("Movimiento no encontrado con ID: {}", id);
            model.addAttribute("error", "Movimiento no encontrado");
            return "redirect:/pinateria/kardex/movimientos";

        } catch (Exception e) {
            log.error("Error al cargar detalle del movimiento ID {}: {}", id, e.getMessage(), e);
            model.addAttribute("error", "Error al cargar el detalle del movimiento: " + e.getMessage());
            return "redirect:/pinateria/kardex/movimientos";
        }
    }

    /**
     * Detalle específico de entrada de Piñatería
     */
    @GetMapping("/entrada/{id}")
    public String detalleEntrada(@PathVariable Long id, Model model) {
        log.info("Accediendo al detalle de entrada ID: {} - Piñatería", id);

        try {
            EntradaDto entrada = entradaService.findById(id);
            if (entrada == null) {
                log.warn("Entrada no encontrada con ID: {}", id);
                model.addAttribute("error", "Entrada no encontrada");
                return "redirect:/pinateria/kardex/movimientos";
            }

            log.info("Entrada encontrada: {}", entrada.getNumeroFactura());

            model.addAttribute("titulo", "Detalle de Entrada - Piñatería");
            model.addAttribute("subtitulo", "Información detallada de la entrada");
            model.addAttribute("movimientoId", id);
            model.addAttribute("rubro", "Piñatería");
            model.addAttribute("entrada", entrada);

            return "kardex/pinateria/movimiento/entrada-detalle";

        } catch (Exception e) {
            log.error("Error al cargar detalle de entrada ID {}: {}", id, e.getMessage(), e);
            model.addAttribute("error", "Error al cargar el detalle de la entrada: " + e.getMessage());
            return "redirect:/pinateria/kardex/movimientos";
        }
    }

    /**
     * Detalle específico de salida de Piñatería
     */
    @GetMapping("/salida/{id}")
    public String detalleSalida(@PathVariable Long id, Model model) {
        log.info("Accediendo al detalle de salida ID: {} - Piñatería", id);

        try {
            SalidaDto salida = salidaService.findById(id);
            if (salida == null) {
                log.warn("Salida no encontrada con ID: {}", id);
                model.addAttribute("error", "Salida no encontrada");
                return "redirect:/pinateria/kardex/movimientos";
            }

            log.info("Salida encontrada: {}", salida.getNumeroDocumento());

            model.addAttribute("titulo", "Detalle de Salida - Piñatería");
            model.addAttribute("subtitulo", "Información detallada de la salida");
            model.addAttribute("movimientoId", id);
            model.addAttribute("rubro", "Piñatería");
            model.addAttribute("salida", salida);

            return "kardex/pinateria/movimiento/salida-detalle";

        } catch (Exception e) {
            log.error("Error al cargar detalle de salida ID {}: {}", id, e.getMessage(), e);
            model.addAttribute("error", "Error al cargar el detalle de la salida: " + e.getMessage());
            return "redirect:/pinateria/kardex/movimientos";
        }
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
            @ModelAttribute EntradaFormDto entradaForm,
            RedirectAttributes redirectAttributes) {

        log.info("Procesando nueva entrada - Piñatería: {}", entradaForm.getNumeroFactura());

        try {
            // Validar que haya productos
            if (entradaForm.getProductos() == null || entradaForm.getProductos().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Debe agregar al menos un producto");
                return "redirect:/pinateria/kardex/entrada/nueva";
            }

            // Validar fecha de entrada
            LocalDate fechaActual = LocalDate.now();

            if (entradaForm.getFechaEntrada().isAfter(fechaActual)) {
                redirectAttributes.addFlashAttribute("error", "La fecha de entrada no puede ser futura");
                return "redirect:/pinateria/kardex/entrada/nueva";
            }

            if (entradaForm.getFechaEntrada().isBefore(fechaActual.minusDays(30))) {
                redirectAttributes.addFlashAttribute("error", "La fecha de entrada no puede ser anterior a 30 días");
                return "redirect:/pinateria/kardex/entrada/nueva";
            }

            // Si el usuario no ingresa número, se genera automáticamente
            if (entradaForm.getNumeroFactura() == null || entradaForm.getNumeroFactura().trim().isEmpty()) {
                entradaForm.setNumeroFactura(entradaService.generarNumeroDocumento(entradaForm.getTipoDocumento()));
            } else {
                // Validar que no exista
                if (entradaService.existsByNumeroFactura(entradaForm.getNumeroFactura())) {
                    redirectAttributes.addFlashAttribute("error", "El número de factura/documento ya existe");
                    return "redirect:/pinateria/kardex/entrada/nueva";
                }
            }

            log.info("Entrada a procesar: {}", entradaForm);
            log.info("Productos: {}", entradaForm.getProductos());

            // Obtener usuario actual
            Long usuarioId = securityContext.getCurrentUserId();
            if (usuarioId == null) {
                redirectAttributes.addFlashAttribute("error", "Usuario no autenticado");
                return "redirect:/pinateria/kardex/entrada/nueva";
            }

            // Crear EntradaDto
            EntradaDto entradaDto = new EntradaDto();
            entradaDto.setNumeroFactura(entradaForm.getNumeroFactura());
            entradaDto.setTipoDocumento(entradaForm.getTipoDocumento());
            entradaDto.setProveedorId(entradaForm.getProveedorId());
            entradaDto.setFechaEntrada(entradaForm.getFechaEntrada().atStartOfDay());
            entradaDto.setObservaciones(entradaForm.getObservaciones());
            entradaDto.setUsuarioRegistroId(usuarioId);

            // detalle de entrada
            List<DetalleEntradaDto> detalles = entradaForm.getProductos().stream()
                    .map(productoForm -> {
                        DetalleEntradaDto detalle = new DetalleEntradaDto();
                        detalle.setProductoId(productoForm.getProductoId());
                        detalle.setCantidad(productoForm.getCantidad());
                        detalle.setPrecioUnitario(productoForm.getPrecioUnitario());
                        detalle.setSubtotal(productoForm.getPrecioUnitario()
                                .multiply(BigDecimal.valueOf(productoForm.getCantidad())));
                        return detalle;
                    })
                    .toList();

            entradaDto.setDetalles(detalles);

            EntradaDto entradaGuardada = entradaService.save(entradaDto);

            log.info("Entrada guardada exitosamente con ID: {}", entradaGuardada.getIdEntrada());

            redirectAttributes.addFlashAttribute("success",
                    "Entrada registrada exitosamente. Número: " + entradaForm.getNumeroFactura());
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
    public String guardarSalida(@ModelAttribute SalidaFormDto salidaForm,
            RedirectAttributes redirectAttributes) {
        try {
            log.info("Salida a procesar: {}", salidaForm);
            log.info("Productos: {}", salidaForm.getProductos());

            // Validar campos requeridos
            if (salidaForm.getTipoDocumento() == null) {
                redirectAttributes.addFlashAttribute("error", "Debe seleccionar un tipo de documento");
                return "redirect:/pinateria/kardex/salida/nueva";
            }

            if (salidaForm.getClienteId() == null) {
                redirectAttributes.addFlashAttribute("error", "Debe seleccionar un cliente");
                return "redirect:/pinateria/kardex/salida/nueva";
            }

            if (salidaForm.getMotivoSalida() == null || salidaForm.getMotivoSalida().trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Debe seleccionar un motivo de salida");
                return "redirect:/pinateria/kardex/salida/nueva";
            }

            // Validar que haya productos
            if (salidaForm.getProductos() == null || salidaForm.getProductos().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Debe agregar al menos un producto");
                return "redirect:/pinateria/kardex/salida/nueva";
            }

            // Validar fecha de salida
            if (salidaForm.getFechaSalida() != null) {
                LocalDate fechaActual = LocalDate.now();
                if (salidaForm.getFechaSalida().isAfter(fechaActual)) {
                    redirectAttributes.addFlashAttribute("error", "La fecha de salida no puede ser futura");
                    return "redirect:/pinateria/kardex/salida/nueva";
                }
            }

            // Si el usuario no ingresa número de documento, se genera automáticamente
            if (salidaForm.getNumeroDocumento() == null || salidaForm.getNumeroDocumento().trim().isEmpty()) {
                salidaForm
                        .setNumeroDocumento(salidaService.generarNumeroDocumento(salidaForm.getTipoDocumento()));
            } else {
                // Validar que no exista
                if (salidaService.existsByNumeroDocumento(salidaForm.getNumeroDocumento())) {
                    redirectAttributes.addFlashAttribute("error", "El número de documento ya existe");
                    return "redirect:/pinateria/kardex/salida/nueva";
                }
            }

            // Obtener usuario actual
            Long usuarioId = securityContext.getCurrentUserId();
            if (usuarioId == null) {
                redirectAttributes.addFlashAttribute("error", "Usuario no autenticado");
                return "redirect:/pinateria/kardex/salida/nueva";
            }

            // Crear SalidaDto
            SalidaDto salidaDto = new SalidaDto();
            salidaDto.setNumeroDocumento(salidaForm.getNumeroDocumento());
            salidaDto.setTipoDocumento(salidaForm.getTipoDocumento());
            salidaDto.setClienteId(salidaForm.getClienteId());
            salidaDto.setFechaSalida(LocalDateTime.now());
            salidaDto.setMotivoSalida(salidaForm.getMotivoSalida());
            salidaDto.setObservaciones(salidaForm.getObservaciones());
            salidaDto.setUsuarioRegistroId(usuarioId);

            // Convertir productos del formulario a DetalleSalidaDto
            List<DetalleSalidaDto> detalles = new ArrayList<>();
            if (salidaForm.getProductos() != null) {
                for (SalidaFormDto.ProductoFormDto productoForm : salidaForm.getProductos()) {
                    DetalleSalidaDto detalle = new DetalleSalidaDto();
                    detalle.setProductoId(productoForm.getProductoId());
                    detalle.setCantidad(productoForm.getCantidad());
                    detalle.setPrecioUnitario(productoForm.getPrecioUnitario());
                    detalle.setSubtotal(productoForm.getSubtotal());
                    detalles.add(detalle);
                }
            }
            salidaDto.setDetalles(detalles);

            // Guardar salida
            SalidaDto salidaGuardada = salidaService.save(salidaDto);
            log.info("Salida guardada exitosamente: {}", salidaGuardada.getIdSalida());

            redirectAttributes.addFlashAttribute("success",
                    "Salida registrada exitosamente con número: " + salidaForm.getNumeroDocumento());
            return "redirect:/pinateria/kardex/dashboard";
        } catch (Exception e) {
            log.error("Error al guardar salida: {}", e.getMessage(), e);
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/pinateria/kardex/salida/nueva";
        }
    }
}