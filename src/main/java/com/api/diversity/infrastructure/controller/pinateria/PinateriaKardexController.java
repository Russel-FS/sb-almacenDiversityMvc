package com.api.diversity.infrastructure.controller.pinateria;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.core.io.ByteArrayResource;
import java.io.ByteArrayOutputStream;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.time.format.DateTimeFormatter;

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
import com.api.diversity.application.service.impl.BarcodeService;
import com.itextpdf.text.Image;

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
    private final BarcodeService barcodeService;

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

            // Obtener últimas entradas y salidas de Piñatería para dashboard
            List<EntradaDto> ultimasEntradas = entradaService
                    .findTop10ByTipoRubroOrderByFechaEntradaDesc(TipoRubro.PIÑATERIA);
            List<SalidaDto> ultimasSalidas = salidaService
                    .findTop10ByTipoRubroOrderByFechaSalidaDesc(TipoRubro.PIÑATERIA);

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

            // En caso de errorrr, usar valores por defecto
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

        return "pinateria/kardex/dashboard/index";
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

        return "pinateria/kardex/entrada/nueva";
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

            return "pinateria/kardex/salida/nueva";
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
            // Obtener últimas entradas y salidas de Piñatería para movimientos
            List<EntradaDto> entradas = entradaService.findTop10ByTipoRubroOrderByFechaEntradaDesc(TipoRubro.PIÑATERIA);
            List<SalidaDto> salidas = salidaService.findTop10ByTipoRubroOrderByFechaSalidaDesc(TipoRubro.PIÑATERIA);

            log.info("Entradas encontradas: {}", entradas != null ? entradas.size() : 0);
            log.info("Salidas encontradas: {}", salidas != null ? salidas.size() : 0);

            // validaciones de que las listas no sean null
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

        return "pinateria/kardex/movimiento/lista";
    }

    /**
     * Detalle de movimiento de Piñatería
     */
    @GetMapping("/movimientos/{id}")
    public String detalleMovimiento(@PathVariable Long id, Model model) {
        log.info("Accediendo al detalle del movimiento ID: {} - Piñatería", id);

        try {
            // buscar como entrada primero
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

            // error si no se encuentra
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

            return "pinateria/kardex/movimiento/entrada-detalle";

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

            return "pinateria/kardex/movimiento/salida-detalle";

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

            // Obtener movimientos recientes para reportes
            List<EntradaDto> entradasRecientes = entradaService
                    .findTop10ByTipoRubroOrderByFechaEntradaDesc(TipoRubro.PIÑATERIA);
            List<SalidaDto> salidasRecientes = salidaService
                    .findTop10ByTipoRubroOrderByFechaSalidaDesc(TipoRubro.PIÑATERIA);

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

        return "pinateria/kardex/reporte/index";
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
            redirectAttributes.addFlashAttribute("error", e.getMessage());
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

    /**
     * Descargar reporte del dashboard en PDF
     */
    @GetMapping("/dashboard/descargar/pdf")
    public ResponseEntity<ByteArrayResource> descargarDashboardPDF() {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Document document = new Document();
            PdfWriter.getInstance(document, baos);

            document.open();
            document.add(new Paragraph("Dashboard Kardex - Piñatería"));
            document.add(new Paragraph("Fecha: " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
            document.add(new Paragraph(" "));

            // Obtener datos del dashboard
            List<ProductoDto> productosPinateria = productoService.findAllByRubro(TipoRubro.PIÑATERIA);
            int totalProductos = productosPinateria.size();
            int productosStockBajo = (int) productosPinateria.stream()
                    .filter(p -> p.getStockActual() != null && p.getStockActual() < 10)
                    .count();
            BigDecimal valorTotalInventario = productosPinateria.stream()
                    .filter(p -> p.getStockActual() != null && p.getPrecioVenta() != null)
                    .map(p -> p.getPrecioVenta().multiply(BigDecimal.valueOf(p.getStockActual())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            document.add(new Paragraph("Resumen del Inventario:"));
            document.add(new Paragraph("Total de productos: " + totalProductos));
            document.add(new Paragraph("Productos con stock bajo: " + productosStockBajo));
            document.add(new Paragraph("Valor total del inventario: S/ " + valorTotalInventario));

            document.close();

            ByteArrayResource resource = new ByteArrayResource(baos.toByteArray());

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=dashboard-pinateria.pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .contentLength(baos.size())
                    .body(resource);

        } catch (Exception e) {
            log.error("Error generando PDF del dashboard: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Descargar reporte del dashboard en Excel
     */
    @GetMapping("/dashboard/descargar/excel")
    public ResponseEntity<ByteArrayResource> descargarDashboardExcel() {
        try {
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Dashboard Piñatería");

            // Crear estilos
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);

            // Título
            Row titleRow = sheet.createRow(0);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("Dashboard Kardex - Piñatería");
            titleCell.setCellStyle(headerStyle);

            // Fecha
            Row dateRow = sheet.createRow(1);
            Cell dateCell = dateRow.createCell(0);
            dateCell.setCellValue("Fecha: " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

            // Obtener datos
            List<ProductoDto> productosPinateria = productoService.findAllByRubro(TipoRubro.PIÑATERIA);
            int totalProductos = productosPinateria.size();
            int productosStockBajo = (int) productosPinateria.stream()
                    .filter(p -> p.getStockActual() != null && p.getStockActual() < 10)
                    .count();
            BigDecimal valorTotalInventario = productosPinateria.stream()
                    .filter(p -> p.getStockActual() != null && p.getPrecioVenta() != null)
                    .map(p -> p.getPrecioVenta().multiply(BigDecimal.valueOf(p.getStockActual())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            // Datos del resumen
            Row summaryRow = sheet.createRow(3);
            Cell summaryCell = summaryRow.createCell(0);
            summaryCell.setCellValue("Resumen del Inventario");
            summaryCell.setCellStyle(headerStyle);

            Row totalRow = sheet.createRow(4);
            totalRow.createCell(0).setCellValue("Total de productos");
            totalRow.createCell(1).setCellValue(totalProductos);

            Row stockBajoRow = sheet.createRow(5);
            stockBajoRow.createCell(0).setCellValue("Productos con stock bajo");
            stockBajoRow.createCell(1).setCellValue(productosStockBajo);

            Row valorRow = sheet.createRow(6);
            valorRow.createCell(0).setCellValue("Valor total del inventario");
            valorRow.createCell(1).setCellValue("S/ " + valorTotalInventario);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            workbook.write(baos);
            workbook.close();

            ByteArrayResource resource = new ByteArrayResource(baos.toByteArray());

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=dashboard-pinateria.xlsx")
                    .contentType(MediaType
                            .parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                    .contentLength(baos.size())
                    .body(resource);

        } catch (Exception e) {
            log.error("Error generando Excel del dashboard: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Descargar lista de movimientos en PDF
     */
    @GetMapping("/movimiento/descargar/pdf")
    public ResponseEntity<ByteArrayResource> descargarMovimientosPDF() {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Document document = new Document();
            PdfWriter.getInstance(document, baos);

            document.open();
            document.add(new Paragraph("Movimientos de Inventario - Piñatería"));
            document.add(new Paragraph("Fecha: " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
            document.add(new Paragraph(" "));

            // Obtener movimientos
            List<EntradaDto> entradas = entradaService.findTop10ByTipoRubroOrderByFechaEntradaDesc(TipoRubro.PIÑATERIA);
            List<SalidaDto> salidas = salidaService.findTop10ByTipoRubroOrderByFechaSalidaDesc(TipoRubro.PIÑATERIA);

            // Tabla de entradas
            document.add(new Paragraph("Últimas Entradas:"));
            PdfPTable tablaEntradas = new PdfPTable(4);
            tablaEntradas.addCell("Fecha");
            tablaEntradas.addCell("N° Documento");
            tablaEntradas.addCell("Proveedor");
            tablaEntradas.addCell("Total");

            for (EntradaDto entrada : entradas) {
                tablaEntradas.addCell(entrada.getFechaEntrada().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                tablaEntradas.addCell(entrada.getNumeroFactura());
                tablaEntradas.addCell(entrada.getProveedorNombre());
                tablaEntradas.addCell("S/ " + entrada.getCostoTotal());
            }
            document.add(tablaEntradas);
            document.add(new Paragraph(" "));

            // Tabla de salidas
            document.add(new Paragraph("Últimas Salidas:"));
            PdfPTable tablaSalidas = new PdfPTable(4);
            tablaSalidas.addCell("Fecha");
            tablaSalidas.addCell("N° Documento");
            tablaSalidas.addCell("Cliente");
            tablaSalidas.addCell("Total");

            for (SalidaDto salida : salidas) {
                tablaSalidas.addCell(salida.getFechaSalida().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                tablaSalidas.addCell(salida.getNumeroDocumento());
                tablaSalidas.addCell(salida.getClienteNombre());
                tablaSalidas.addCell("S/ " + salida.getTotalVenta());
            }
            document.add(tablaSalidas);

            document.close();

            ByteArrayResource resource = new ByteArrayResource(baos.toByteArray());

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=movimientos-pinateria.pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .contentLength(baos.size())
                    .body(resource);

        } catch (Exception e) {
            log.error("Error generando PDF de movimientos: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Descargar lista de movimientos en Excel
     */
    @GetMapping("/movimiento/descargar/excel")
    public ResponseEntity<ByteArrayResource> descargarMovimientosExcel() {
        try {
            Workbook workbook = new XSSFWorkbook();

            // Hoja de entradas
            Sheet sheetEntradas = workbook.createSheet("Entradas");
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);

            Row titleRow = sheetEntradas.createRow(0);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("Entradas de Inventario - Piñatería");
            titleCell.setCellStyle(headerStyle);

            Row headerRow = sheetEntradas.createRow(1);
            headerRow.createCell(0).setCellValue("Fecha");
            headerRow.createCell(1).setCellValue("N° Documento");
            headerRow.createCell(2).setCellValue("Proveedor");
            headerRow.createCell(3).setCellValue("Total");

            List<EntradaDto> entradas = entradaService.findTop10ByTipoRubroOrderByFechaEntradaDesc(TipoRubro.PIÑATERIA);
            int rowNum = 2;
            for (EntradaDto entrada : entradas) {
                Row row = sheetEntradas.createRow(rowNum++);
                row.createCell(0)
                        .setCellValue(entrada.getFechaEntrada().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                row.createCell(1).setCellValue(entrada.getNumeroFactura());
                row.createCell(2).setCellValue(entrada.getProveedorNombre());
                row.createCell(3).setCellValue("S/ " + entrada.getCostoTotal());
            }

            // Hoja de salidas
            Sheet sheetSalidas = workbook.createSheet("Salidas");
            Row titleRowSalidas = sheetSalidas.createRow(0);
            Cell titleCellSalidas = titleRowSalidas.createCell(0);
            titleCellSalidas.setCellValue("Salidas de Inventario - Piñatería");
            titleCellSalidas.setCellStyle(headerStyle);

            Row headerRowSalidas = sheetSalidas.createRow(1);
            headerRowSalidas.createCell(0).setCellValue("Fecha");
            headerRowSalidas.createCell(1).setCellValue("N° Documento");
            headerRowSalidas.createCell(2).setCellValue("Cliente");
            headerRowSalidas.createCell(3).setCellValue("Total");

            List<SalidaDto> salidas = salidaService.findTop10ByTipoRubroOrderByFechaSalidaDesc(TipoRubro.PIÑATERIA);
            rowNum = 2;
            for (SalidaDto salida : salidas) {
                Row row = sheetSalidas.createRow(rowNum++);
                row.createCell(0)
                        .setCellValue(salida.getFechaSalida().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                row.createCell(1).setCellValue(salida.getNumeroDocumento());
                row.createCell(2).setCellValue(salida.getClienteNombre());
                row.createCell(3).setCellValue("S/ " + salida.getTotalVenta());
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            workbook.write(baos);
            workbook.close();

            ByteArrayResource resource = new ByteArrayResource(baos.toByteArray());

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=movimientos-pinateria.xlsx")
                    .contentType(MediaType
                            .parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                    .contentLength(baos.size())
                    .body(resource);

        } catch (Exception e) {
            log.error("Error generando Excel de movimientos: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Descargar detalle de entrada en PDF
     */
    @GetMapping("/entrada/{id}/descargar/pdf")
    public ResponseEntity<ByteArrayResource> descargarEntradaPDF(@PathVariable Long id) {
        try {
            EntradaDto entrada = entradaService.findById(id);
            if (entrada == null) {
                return ResponseEntity.notFound().build();
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Document document = new Document();
            PdfWriter.getInstance(document, baos);

            document.open();
            document.add(new Paragraph("Comprobante de Entrada - Piñatería"));
            document.add(new Paragraph(
                    "Fecha: " + entrada.getFechaEntrada().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
            document.add(new Paragraph("N° Documento: " + entrada.getNumeroFactura()));
            document.add(new Paragraph("Proveedor: " + entrada.getProveedorNombre()));
            document.add(new Paragraph(" "));

            // Tabla de productos
            document.add(new Paragraph("Productos:"));
            PdfPTable tabla = new PdfPTable(4);
            tabla.addCell("Producto");
            tabla.addCell("Cantidad");
            tabla.addCell("Precio Unit.");
            tabla.addCell("Subtotal");

            for (DetalleEntradaDto detalle : entrada.getDetalles()) {
                tabla.addCell(detalle.getProductoNombre());
                tabla.addCell(String.valueOf(detalle.getCantidad()));
                tabla.addCell("S/ " + detalle.getPrecioUnitario());
                tabla.addCell("S/ " + detalle.getSubtotal());
            }
            document.add(tabla);
            document.add(new Paragraph("Total: S/ " + entrada.getCostoTotal()));

            document.close();

            ByteArrayResource resource = new ByteArrayResource(baos.toByteArray());

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=entrada-" + id + ".pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .contentLength(baos.size())
                    .body(resource);

        } catch (Exception e) {
            log.error("Error generando PDF de entrada: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Descargar detalle de entrada en Excel
     */
    @GetMapping("/entrada/{id}/descargar/excel")
    public ResponseEntity<ByteArrayResource> descargarEntradaExcel(@PathVariable Long id) {
        try {
            EntradaDto entrada = entradaService.findById(id);
            if (entrada == null) {
                return ResponseEntity.notFound().build();
            }

            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Entrada " + id);

            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);

            // Información de la entrada
            Row titleRow = sheet.createRow(0);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("Comprobante de Entrada - Piñatería");
            titleCell.setCellStyle(headerStyle);

            Row infoRow1 = sheet.createRow(1);
            infoRow1.createCell(0).setCellValue(
                    "Fecha: " + entrada.getFechaEntrada().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            infoRow1.createCell(1).setCellValue("N° Documento: " + entrada.getNumeroFactura());

            Row infoRow2 = sheet.createRow(2);
            infoRow2.createCell(0).setCellValue("Proveedor: " + entrada.getProveedorNombre());

            // Tabla de productos
            Row headerRow = sheet.createRow(4);
            headerRow.createCell(0).setCellValue("Producto");
            headerRow.createCell(1).setCellValue("Cantidad");
            headerRow.createCell(2).setCellValue("Precio Unit.");
            headerRow.createCell(3).setCellValue("Subtotal");

            int rowNum = 5;
            for (DetalleEntradaDto detalle : entrada.getDetalles()) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(detalle.getProductoNombre());
                row.createCell(1).setCellValue(detalle.getCantidad());
                row.createCell(2).setCellValue("S/ " + detalle.getPrecioUnitario());
                row.createCell(3).setCellValue("S/ " + detalle.getSubtotal());
            }

            Row totalRow = sheet.createRow(rowNum);
            totalRow.createCell(0).setCellValue("Total:");
            totalRow.createCell(3).setCellValue("S/ " + entrada.getCostoTotal());

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            workbook.write(baos);
            workbook.close();

            ByteArrayResource resource = new ByteArrayResource(baos.toByteArray());

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=entrada-" + id + ".xlsx")
                    .contentType(MediaType
                            .parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                    .contentLength(baos.size())
                    .body(resource);

        } catch (Exception e) {
            log.error("Error generando Excel de entrada: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Descargar detalle de salida en PDFs
     */
    @GetMapping("/salida/{id}/descargar/pdf")
    public ResponseEntity<ByteArrayResource> descargarSalidaPDF(@PathVariable Long id) {
        try {
            SalidaDto salida = salidaService.findById(id);
            if (salida == null) {
                return ResponseEntity.notFound().build();
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Document document = new Document();
            PdfWriter.getInstance(document, baos);

            document.open();
            document.add(new Paragraph("Comprobante de Salida - Piñatería"));
            document.add(new Paragraph(
                    "Fecha: " + salida.getFechaSalida().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
            document.add(new Paragraph("N° Documento: " + salida.getNumeroDocumento()));
            document.add(new Paragraph("Cliente: " + salida.getClienteNombre()));
            document.add(new Paragraph("Tipo Cliente: " + salida.getClienteTipo().getDescripcion()));
            document.add(new Paragraph(" "));

            // codigo de barras.
            try {
                String data = salida.getNumeroDocumento();
                byte[] barcodeBytes = barcodeService.generarCodigoBarras(data, 400, 100);
                if (barcodeBytes != null && barcodeBytes.length > 0) {
                    Image barcode = Image.getInstance(barcodeBytes);
                    barcode.scalePercent(80);
                    document.add(barcode);
                    document.add(new Paragraph(data));
                    document.add(new Paragraph(" "));
                }
            } catch (Exception ex) {
                log.error("Error generando código de barras para PDF: {}", ex.getMessage());
            }

            // Tabla de productos
            document.add(new Paragraph("Productos:"));
            PdfPTable tabla = new PdfPTable(4);
            tabla.addCell("Producto");
            tabla.addCell("Cantidad");
            tabla.addCell("Precio Unit.");
            tabla.addCell("Subtotal");

            for (DetalleSalidaDto detalle : salida.getDetalles()) {
                tabla.addCell(detalle.getProductoNombre());
                tabla.addCell(String.valueOf(detalle.getCantidad()));
                tabla.addCell("S/ " + detalle.getPrecioUnitario());
                tabla.addCell("S/ " + detalle.getSubtotal());
            }
            document.add(tabla);
            document.add(new Paragraph("Total: S/ " + salida.getTotalVenta()));

            document.close();

            ByteArrayResource resource = new ByteArrayResource(baos.toByteArray());

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=salida-" + id + ".pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .contentLength(baos.size())
                    .body(resource);

        } catch (Exception e) {
            log.error("Error generando PDF de salida: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Descargar detalle de salida en Excel
     */
    @GetMapping("/salida/{id}/descargar/excel")
    public ResponseEntity<ByteArrayResource> descargarSalidaExcel(@PathVariable Long id) {
        try {
            SalidaDto salida = salidaService.findById(id);
            if (salida == null) {
                return ResponseEntity.notFound().build();
            }

            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Salida " + id);

            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);

            // Información de la salida
            Row titleRow = sheet.createRow(0);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("Comprobante de Salida - Piñatería");
            titleCell.setCellStyle(headerStyle);

            Row infoRow1 = sheet.createRow(1);
            infoRow1.createCell(0).setCellValue(
                    "Fecha: " + salida.getFechaSalida().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            infoRow1.createCell(1).setCellValue("N° Documento: " + salida.getNumeroDocumento());

            Row infoRow2 = sheet.createRow(2);
            infoRow2.createCell(0).setCellValue("Cliente: " + salida.getClienteNombre());
            infoRow2.createCell(1).setCellValue("Tipo: " + salida.getClienteTipo().getDescripcion());

            // Tabla de productos
            Row headerRow = sheet.createRow(4);
            headerRow.createCell(0).setCellValue("Producto");
            headerRow.createCell(1).setCellValue("Cantidad");
            headerRow.createCell(2).setCellValue("Precio Unit.");
            headerRow.createCell(3).setCellValue("Subtotal");

            int rowNum = 5;
            for (DetalleSalidaDto detalle : salida.getDetalles()) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(detalle.getProductoNombre());
                row.createCell(1).setCellValue(detalle.getCantidad());
                row.createCell(2).setCellValue("S/ " + detalle.getPrecioUnitario());
                row.createCell(3).setCellValue("S/ " + detalle.getSubtotal());
            }

            Row totalRow = sheet.createRow(rowNum);
            totalRow.createCell(0).setCellValue("Total:");
            totalRow.createCell(3).setCellValue("S/ " + salida.getTotalVenta());

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            workbook.write(baos);
            workbook.close();

            ByteArrayResource resource = new ByteArrayResource(baos.toByteArray());

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=salida-" + id + ".xlsx")
                    .contentType(MediaType
                            .parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                    .contentLength(baos.size())
                    .body(resource);

        } catch (Exception e) {
            log.error("Error generando Excel de salida: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Descargar reporte de inventario en PDF
     */
    @GetMapping("/reporte/descargar/inventario/pdf")
    public ResponseEntity<ByteArrayResource> descargarInventarioPDF() {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Document document = new Document();
            PdfWriter.getInstance(document, baos);

            document.open();
            document.add(new Paragraph("Reporte de Inventario - Piñatería"));
            document.add(new Paragraph("Fecha: " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
            document.add(new Paragraph(" "));

            List<ProductoDto> productos = productoService.findAllByRubro(TipoRubro.PIÑATERIA);
            BigDecimal valorTotal = productos.stream()
                    .filter(p -> p.getStockActual() != null && p.getPrecioVenta() != null)
                    .map(p -> p.getPrecioVenta().multiply(BigDecimal.valueOf(p.getStockActual())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            document.add(new Paragraph("Valor total del inventario: S/ " + valorTotal));
            document.add(new Paragraph(" "));

            // Tabla de productos
            document.add(new Paragraph("Productos en inventario:"));
            PdfPTable tabla = new PdfPTable(5);
            tabla.addCell("Código");
            tabla.addCell("Producto");
            tabla.addCell("Stock Actual");
            tabla.addCell("Precio Venta");
            tabla.addCell("Valor Total");

            for (ProductoDto producto : productos) {
                tabla.addCell(producto.getCodigoProducto());
                tabla.addCell(producto.getNombreProducto());
                tabla.addCell(String.valueOf(producto.getStockActual()));
                tabla.addCell("S/ " + producto.getPrecioVenta());
                BigDecimal valorProducto = producto.getPrecioVenta()
                        .multiply(BigDecimal.valueOf(producto.getStockActual()));
                tabla.addCell("S/ " + valorProducto);
            }
            document.add(tabla);

            document.close();

            ByteArrayResource resource = new ByteArrayResource(baos.toByteArray());

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=inventario-pinateria.pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .contentLength(baos.size())
                    .body(resource);

        } catch (Exception e) {
            log.error("Error generando PDF de inventario: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Descargar reporte de inventario en Excel
     */
    @GetMapping("/reporte/descargar/inventario/excel")
    public ResponseEntity<ByteArrayResource> descargarInventarioExcel() {
        try {
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Inventario Piñatería");

            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);

            Row titleRow = sheet.createRow(0);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("Reporte de Inventario - Piñatería");
            titleCell.setCellStyle(headerStyle);

            List<ProductoDto> productos = productoService.findAllByRubro(TipoRubro.PIÑATERIA);
            BigDecimal valorTotal = productos.stream()
                    .filter(p -> p.getStockActual() != null && p.getPrecioVenta() != null)
                    .map(p -> p.getPrecioVenta().multiply(BigDecimal.valueOf(p.getStockActual())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            Row valorRow = sheet.createRow(1);
            valorRow.createCell(0).setCellValue("Valor total del inventario: S/ " + valorTotal);

            // Tabla de productos
            Row headerRow = sheet.createRow(3);
            headerRow.createCell(0).setCellValue("Código");
            headerRow.createCell(1).setCellValue("Producto");
            headerRow.createCell(2).setCellValue("Stock Actual");
            headerRow.createCell(3).setCellValue("Precio Venta");
            headerRow.createCell(4).setCellValue("Valor Total");

            int rowNum = 4;
            for (ProductoDto producto : productos) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(producto.getCodigoProducto());
                row.createCell(1).setCellValue(producto.getNombreProducto());
                row.createCell(2).setCellValue(producto.getStockActual());
                row.createCell(3).setCellValue("S/ " + producto.getPrecioVenta());
                BigDecimal valorProducto = producto.getPrecioVenta()
                        .multiply(BigDecimal.valueOf(producto.getStockActual()));
                row.createCell(4).setCellValue("S/ " + valorProducto);
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            workbook.write(baos);
            workbook.close();

            ByteArrayResource resource = new ByteArrayResource(baos.toByteArray());

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=inventario-pinateria.xlsx")
                    .contentType(MediaType
                            .parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                    .contentLength(baos.size())
                    .body(resource);

        } catch (Exception e) {
            log.error("Error generando Excel de inventario: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Descargar reporte de stock bajo en PDFs
     */
    @GetMapping("/reporte/descargar/stock-bajo/pdf")
    public ResponseEntity<ByteArrayResource> descargarStockBajoPDF() {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Document document = new Document();
            PdfWriter.getInstance(document, baos);

            document.open();
            document.add(new Paragraph("Productos con Stock Bajo - Piñatería"));
            document.add(new Paragraph("Fecha: " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
            document.add(new Paragraph(" "));

            List<ProductoDto> productos = productoService.findAllByRubro(TipoRubro.PIÑATERIA);
            List<ProductoDto> productosStockBajo = productos.stream()
                    .filter(p -> p.getStockActual() != null && p.getStockActual() < 10)
                    .toList();

            document.add(new Paragraph("Total de productos con stock bajo: " + productosStockBajo.size()));
            document.add(new Paragraph(" "));

            // Tabla de productos
            PdfPTable tabla = new PdfPTable(4);
            tabla.addCell("Código");
            tabla.addCell("Producto");
            tabla.addCell("Stock Actual");
            tabla.addCell("Stock Mínimo");

            for (ProductoDto producto : productosStockBajo) {
                tabla.addCell(producto.getCodigoProducto());
                tabla.addCell(producto.getNombreProducto());
                tabla.addCell(String.valueOf(producto.getStockActual()));
                tabla.addCell("10");
            }
            document.add(tabla);

            document.close();

            ByteArrayResource resource = new ByteArrayResource(baos.toByteArray());

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=stock-bajo-pinateria.pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .contentLength(baos.size())
                    .body(resource);

        } catch (Exception e) {
            log.error("Error generando PDF de stock bajo: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Descargar reporte de stock bajo en Excel
     */
    @GetMapping("/reporte/descargar/stock-bajo/excel")
    public ResponseEntity<ByteArrayResource> descargarStockBajoExcel() {
        try {
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Stock Bajo Piñatería");

            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);

            Row titleRow = sheet.createRow(0);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("Productos con Stock Bajo - Piñatería");
            titleCell.setCellStyle(headerStyle);

            List<ProductoDto> productos = productoService.findAllByRubro(TipoRubro.PIÑATERIA);
            List<ProductoDto> productosStockBajo = productos.stream()
                    .filter(p -> p.getStockActual() != null && p.getStockActual() < 10)
                    .toList();

            Row totalRow = sheet.createRow(1);
            totalRow.createCell(0).setCellValue("Total de productos con stock bajo: " + productosStockBajo.size());

            // Tabla de productos
            Row headerRow = sheet.createRow(3);
            headerRow.createCell(0).setCellValue("Código");
            headerRow.createCell(1).setCellValue("Producto");
            headerRow.createCell(2).setCellValue("Stock Actual");
            headerRow.createCell(3).setCellValue("Stock Mínimo");

            int rowNum = 4;
            for (ProductoDto producto : productosStockBajo) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(producto.getCodigoProducto());
                row.createCell(1).setCellValue(producto.getNombreProducto());
                row.createCell(2).setCellValue(producto.getStockActual());
                row.createCell(3).setCellValue("10");
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            workbook.write(baos);
            workbook.close();

            ByteArrayResource resource = new ByteArrayResource(baos.toByteArray());

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=stock-bajo-pinateria.xlsx")
                    .contentType(MediaType
                            .parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                    .contentLength(baos.size())
                    .body(resource);

        } catch (Exception e) {
            log.error("Error generando Excel de stock bajo: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}