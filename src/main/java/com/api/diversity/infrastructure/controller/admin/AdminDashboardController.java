package com.api.diversity.infrastructure.controller.admin;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.api.diversity.application.dto.ProductoDto;
import com.api.diversity.application.dto.UsuarioDto;
import com.api.diversity.application.service.interfaces.IClienteService;
import com.api.diversity.application.service.interfaces.IEntradaService;
import com.api.diversity.application.service.interfaces.IProductoService;
import com.api.diversity.application.service.interfaces.IProveedorService;
import com.api.diversity.application.service.interfaces.IRubroService;
import com.api.diversity.application.service.interfaces.ISalidaService;
import com.api.diversity.application.service.interfaces.IUsuarioService;
import com.api.diversity.domain.enums.EstadoCliente;
import com.api.diversity.domain.enums.EstadoEntrada;
import com.api.diversity.domain.enums.EstadoProducto;
import com.api.diversity.domain.enums.EstadoProveedor;
import com.api.diversity.domain.enums.EstadoSalida;
import com.api.diversity.domain.enums.EstadoUsuario;
import com.api.diversity.domain.enums.TipoRubro;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminDashboardController {

        private final IUsuarioService usuarioService;
        private final IProductoService productoService;
        private final IProveedorService proveedorService;
        private final IClienteService clienteService;
        private final IEntradaService entradaService;
        private final ISalidaService salidaService;
        private final IRubroService rubroService;

        @GetMapping("")
        public String dashboard(Model model) {
                log.info("Accediendo al dashboard de administración");

                try {
                        // Estadísticas de usuarios
                        Long totalUsuarios = (long) usuarioService.findAll().size();
                        Long usuariosActivos = usuarioService.findAll().stream()
                                        .filter(u -> u.getEstado() == EstadoUsuario.Activo).count();
                        Long usuariosInactivos = usuarioService.findAll().stream()
                                        .filter(u -> u.getEstado() == EstadoUsuario.Inactivo).count();
                        Long usuariosBloqueados = usuarioService.findAll().stream()
                                        .filter(u -> u.getEstado() == EstadoUsuario.Bloqueado).count();

                        // Estadísticas de productos
                        Long totalProductos = (long) productoService.findAll().size();
                        Long productosActivos = productoService.findAll().stream()
                                        .filter(p -> p.getEstado() == EstadoProducto.Activo).count();
                        Long productosStockBajo = productoService.findAll().stream()
                                        .filter(p -> p.getStockActual() != null && p.getStockActual() <= 10).count();

                        // Estadísticas de proveedores
                        Long totalProveedores = (long) proveedorService.findAll().size();
                        Long proveedoresActivos = proveedorService.findAll().stream()
                                        .filter(p -> p.getEstado() == EstadoProveedor.Activo).count();

                        // Estadísticas de clientes
                        Long totalClientes = (long) clienteService.findAll().size();
                        Long clientesActivos = clienteService.findAll().stream()
                                        .filter(c -> c.getEstado() == EstadoCliente.Activo).count();

                        // Usuarios recientes
                        List<UsuarioDto> usuariosRecientes = usuarioService.findAll().stream()
                                        .sorted((u1, u2) -> u2.getFechaCreacion().compareTo(u1.getFechaCreacion()))
                                        .limit(5)
                                        .toList();

                        // Productos con stock bajo
                        List<ProductoDto> productosStockBajoList = productoService.findAll()
                                        .stream()
                                        .filter(p -> p.getStockActual() != null && p.getStockActual() <= 10)
                                        .sorted((p1, p2) -> Integer.compare(p1.getStockActual(), p2.getStockActual()))
                                        .limit(5)
                                        .toList();

                        // --- Estadísticas por rubro
                        List<ProductoDto> productosPinateria = productoService.findAllByRubro(TipoRubro.PIÑATERIA);
                        List<ProductoDto> productosLibreria = productoService.findAllByRubro(TipoRubro.LIBRERIA);
                        List<ProductoDto> productosCamaras = productoService.findAllByRubro(TipoRubro.CAMARA_SEGURIDAD);

                        int stockBajoPinateria = (int) productosPinateria.stream()
                                        .filter(p -> p.getStockActual() != null && p.getStockActual() < 10)
                                        .count();
                        int stockBajoLibreria = (int) productosLibreria.stream()
                                        .filter(p -> p.getStockActual() != null && p.getStockActual() < 10)
                                        .count();
                        int stockBajoCamaras = (int) productosCamaras.stream()
                                        .filter(p -> p.getStockActual() != null && p.getStockActual() < 10)
                                        .count();

                        BigDecimal valorPinateria = productosPinateria.stream()
                                        .filter(p -> p.getStockActual() != null && p.getPrecioVenta() != null)
                                        .map(p -> p.getPrecioVenta().multiply(BigDecimal.valueOf(p.getStockActual())))
                                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                        BigDecimal valorLibreria = productosLibreria.stream()
                                        .filter(p -> p.getStockActual() != null && p.getPrecioVenta() != null)
                                        .map(p -> p.getPrecioVenta().multiply(BigDecimal.valueOf(p.getStockActual())))
                                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                        BigDecimal valorCamaras = productosCamaras.stream()
                                        .filter(p -> p.getStockActual() != null && p.getPrecioVenta() != null)
                                        .map(p -> p.getPrecioVenta().multiply(BigDecimal.valueOf(p.getStockActual())))
                                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                        // Pasar datos de rubros al modelo
                        model.addAttribute("productosPinateria", productosPinateria.size());
                        model.addAttribute("productosLibreria", productosLibreria.size());
                        model.addAttribute("productosCamaras", productosCamaras.size());
                        model.addAttribute("stockBajoPinateria", stockBajoPinateria);
                        model.addAttribute("stockBajoLibreria", stockBajoLibreria);
                        model.addAttribute("stockBajoCamaras", stockBajoCamaras);
                        model.addAttribute("valorPinateria", valorPinateria);
                        model.addAttribute("valorLibreria", valorLibreria);
                        model.addAttribute("valorCamaras", valorCamaras);

                        // --- Fin estadísticas por rubro ---

                        model.addAttribute("titulo", "Panel de Administración");
                        model.addAttribute("subtitulo", "Dashboard principal del sistema");

                        // Estadísticas generales
                        model.addAttribute("totalUsuarios", totalUsuarios);
                        model.addAttribute("usuariosActivos", usuariosActivos);
                        model.addAttribute("usuariosInactivos", usuariosInactivos);
                        model.addAttribute("usuariosBloqueados", usuariosBloqueados);

                        model.addAttribute("totalProductos", totalProductos);
                        model.addAttribute("productosActivos", productosActivos);
                        model.addAttribute("productosStockBajo", productosStockBajo);

                        model.addAttribute("totalProveedores", totalProveedores);
                        model.addAttribute("proveedoresActivos", proveedoresActivos);

                        model.addAttribute("totalClientes", totalClientes);
                        model.addAttribute("clientesActivos", clientesActivos);

                        // Datos recientes
                        model.addAttribute("usuariosRecientes", usuariosRecientes);
                        model.addAttribute("productosStockBajoList", productosStockBajoList);

                } catch (Exception e) {
                        log.error("Error al cargar dashboard de administración: {}", e.getMessage(), e);
                        model.addAttribute("error", "Error al cargar el dashboard: " + e.getMessage());
                }

                return "admin/dashboard";
        }

        @GetMapping("/api/dashboard/charts")
        public ResponseEntity<Map<String, Object>> getChartData() {
                log.info("Obteniendo datos para gráficos del dashboard");

                try {
                        Map<String, Object> chartData = new HashMap<>();

                        // datos por rubro
                        List<ProductoDto> productosPinateria = productoService.findAllByRubro(TipoRubro.PIÑATERIA);
                        List<ProductoDto> productosLibreria = productoService.findAllByRubro(TipoRubro.LIBRERIA);
                        List<ProductoDto> productosCamaras = productoService.findAllByRubro(TipoRubro.CAMARA_SEGURIDAD);

                        Map<String, Object> productosPorRubro = new HashMap<>();
                        productosPorRubro.put("labels", List.of("Piñatería", "Librería", "Cámaras"));
                        productosPorRubro.put("data", List.of(productosPinateria.size(), productosLibreria.size(),
                                        productosCamaras.size()));
                        productosPorRubro.put("backgroundColor", List.of("#ec4899", "#3b82f6", "#8b5cf6"));

                        // stok bajo por rubro
                        int stockBajoPinateria = (int) productosPinateria.stream()
                                        .filter(p -> p.getStockActual() != null && p.getStockActual() < 10)
                                        .count();
                        int stockBajoLibreria = (int) productosLibreria.stream()
                                        .filter(p -> p.getStockActual() != null && p.getStockActual() < 10)
                                        .count();
                        int stockBajoCamaras = (int) productosCamaras.stream()
                                        .filter(p -> p.getStockActual() != null && p.getStockActual() < 10)
                                        .count();

                        Map<String, Object> stockBajoPorRubro = new HashMap<>();
                        stockBajoPorRubro.put("labels", List.of("Piñatería", "Librería", "Cámaras"));
                        stockBajoPorRubro.put("data", List.of(stockBajoPinateria, stockBajoLibreria, stockBajoCamaras));
                        stockBajoPorRubro.put("backgroundColor", List.of("#fef3c7", "#fecaca", "#fde68a"));

                        // id de rubros
                        Long idRubroPinateria = rubroService.findByNombreRubro(TipoRubro.PIÑATERIA.getNombre())
                                        .map(r -> r.getIdRubro()).orElse(null);
                        Long idRubroLibreria = rubroService.findByNombreRubro(TipoRubro.LIBRERIA.getNombre())
                                        .map(r -> r.getIdRubro()).orElse(null);
                        Long idRubroCamaras = rubroService.findByNombreRubro(TipoRubro.CAMARA_SEGURIDAD.getNombre())
                                        .map(r -> r.getIdRubro()).orElse(null);

                        // valor de pitañería
                        BigDecimal valorPinateria = BigDecimal.ZERO;
                        if (idRubroPinateria != null) {
                                // costo de entrada aprobadas
                                BigDecimal entradasPinateria = entradaService
                                                .findByRubroIdAndEstado(idRubroPinateria, EstadoEntrada.Completado)
                                                .stream()
                                                .map(e -> e.getCostoTotal())
                                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                                // costo total de salidas aprobadas
                                BigDecimal salidasPinateria = salidaService
                                                .findByRubroIdAndEstado(idRubroPinateria, EstadoSalida.Completado)
                                                .stream()
                                                .map(s -> s.getTotalVenta())
                                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                                valorPinateria = entradasPinateria.subtract(salidasPinateria);
                        }

                        // valor de inventario libreria
                        BigDecimal valorLibreria = BigDecimal.ZERO;
                        if (idRubroLibreria != null) {
                                BigDecimal entradasLibreria = entradaService
                                                .findByRubroIdAndEstado(idRubroLibreria, EstadoEntrada.Completado)
                                                .stream()
                                                .map(e -> e.getCostoTotal())
                                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                                BigDecimal salidasLibreria = salidaService
                                                .findByRubroIdAndEstado(idRubroLibreria, EstadoSalida.Completado)
                                                .stream()
                                                .map(s -> s.getTotalVenta())
                                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                                valorLibreria = entradasLibreria.subtract(salidasLibreria);
                        }

                        // valor de inventario de camaras
                        BigDecimal valorCamaras = BigDecimal.ZERO;
                        if (idRubroCamaras != null) {
                                BigDecimal entradasCamaras = entradaService
                                                .findByRubroIdAndEstado(idRubroCamaras, EstadoEntrada.Completado)
                                                .stream()
                                                .map(e -> e.getCostoTotal())
                                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                                BigDecimal salidasCamaras = salidaService
                                                .findByRubroIdAndEstado(idRubroCamaras, EstadoSalida.Completado)
                                                .stream()
                                                .map(s -> s.getTotalVenta())
                                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                                valorCamaras = entradasCamaras.subtract(salidasCamaras);
                        }

                        Map<String, Object> valorInventarioPorRubro = new HashMap<>();
                        valorInventarioPorRubro.put("labels", List.of("Piñatería", "Librería", "Cámaras"));
                        valorInventarioPorRubro.put("data", List.of(valorPinateria.doubleValue(),
                                        valorLibreria.doubleValue(), valorCamaras.doubleValue()));
                        valorInventarioPorRubro.put("backgroundColor", List.of("#10b981", "#06b6d4", "#6366f1"));

                        // datos de usuariso
                        Long usuariosActivos = usuarioService.findAll().stream()
                                        .filter(u -> u.getEstado() == EstadoUsuario.Activo).count();
                        Long usuariosInactivos = usuarioService.findAll().stream()
                                        .filter(u -> u.getEstado() == EstadoUsuario.Inactivo).count();
                        Long usuariosBloqueados = usuarioService.findAll().stream()
                                        .filter(u -> u.getEstado() == EstadoUsuario.Bloqueado).count();

                        Map<String, Object> estadoUsuarios = new HashMap<>();
                        estadoUsuarios.put("labels", List.of("Activos", "Inactivos", "Bloqueados"));
                        estadoUsuarios.put("data", List.of(usuariosActivos, usuariosInactivos, usuariosBloqueados));
                        estadoUsuarios.put("backgroundColor", List.of("#10b981", "#f59e0b", "#ef4444"));

                        chartData.put("productosPorRubro", productosPorRubro);
                        chartData.put("stockBajoPorRubro", stockBajoPorRubro);
                        chartData.put("valorInventarioPorRubro", valorInventarioPorRubro);
                        chartData.put("estadoUsuarios", estadoUsuarios);

                        return ResponseEntity.ok(chartData);

                } catch (Exception e) {
                        log.error("Error al obtener datos para gráficos: {}", e.getMessage(), e);
                        return ResponseEntity.internalServerError().build();
                }
        }
}