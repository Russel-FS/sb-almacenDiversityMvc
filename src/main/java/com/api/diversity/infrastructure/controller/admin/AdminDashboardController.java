package com.api.diversity.infrastructure.controller.admin;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.api.diversity.application.service.interfaces.IClienteService;
import com.api.diversity.application.service.interfaces.IProductoService;
import com.api.diversity.application.service.interfaces.IProveedorService;
import com.api.diversity.application.service.interfaces.IUsuarioService;
import com.api.diversity.domain.enums.EstadoCliente;
import com.api.diversity.domain.enums.EstadoProducto;
import com.api.diversity.domain.enums.EstadoProveedor;
import com.api.diversity.domain.enums.EstadoUsuario;

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

            // Usuarios recientes (últimos 5)
            List<com.api.diversity.application.dto.UsuarioDto> usuariosRecientes = usuarioService.findAll().stream()
                    .sorted((u1, u2) -> u2.getFechaCreacion().compareTo(u1.getFechaCreacion()))
                    .limit(5)
                    .toList();

            // Productos con stock bajo (últimos 5)
            List<com.api.diversity.application.dto.ProductoDto> productosStockBajoList = productoService.findAll()
                    .stream()
                    .filter(p -> p.getStockActual() != null && p.getStockActual() <= 10)
                    .sorted((p1, p2) -> Integer.compare(p1.getStockActual(), p2.getStockActual()))
                    .limit(5)
                    .toList();

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
}