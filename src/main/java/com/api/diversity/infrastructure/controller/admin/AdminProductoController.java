package com.api.diversity.infrastructure.controller.admin;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.api.diversity.application.dto.ProductoDto;
import com.api.diversity.application.service.interfaces.ICategoriaService;
import com.api.diversity.application.service.interfaces.IProductoService;
import com.api.diversity.domain.enums.EstadoProducto;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/admin/productos")
@RequiredArgsConstructor
@Slf4j
public class AdminProductoController {

    private final IProductoService productoService;
    private final ICategoriaService categoriaService;

    @GetMapping("")
    public String listarProductos(@RequestParam(required = false) String busqueda,
            @RequestParam(required = false) EstadoProducto estado,
            Model model) {
        log.info("Listando productos desde administración");

        try {
            List<ProductoDto> productos = productoService.findAll();

            // productoss generalmente ordenados por nombre
            if (busqueda != null && !busqueda.trim().isEmpty()) {
                productos = productos.stream()
                        .filter(p -> p.getNombreProducto().toLowerCase().contains(busqueda.toLowerCase()) ||
                                p.getCodigoProducto().toLowerCase().contains(busqueda.toLowerCase()))
                        .toList();
            }

            // Filtro por estado
            if (estado != null) {
                productos = productos.stream()
                        .filter(p -> p.getEstado() == estado)
                        .toList();
            }

            // Estadísticas
            Long totalProductos = (long) productoService.findAll().size();
            Long productosActivos = productoService.findAll().stream()
                    .filter(p -> p.getEstado() == EstadoProducto.Activo).count();
            Long productosInactivos = productoService.findAll().stream()
                    .filter(p -> p.getEstado() == EstadoProducto.Inactivo).count();
            Long productosStockBajo = productoService.findAll().stream()
                    .filter(p -> p.getStockActual() != null && p.getStockActual() <= 10).count();

            model.addAttribute("titulo", "Gestión de Productos");
            model.addAttribute("subtitulo", "Administración de productos del sistema");
            model.addAttribute("productos", productos);
            model.addAttribute("busqueda", busqueda);
            model.addAttribute("estadoFiltro", estado);
            model.addAttribute("estados", EstadoProducto.values());

            // Estadísticas
            model.addAttribute("totalProductos", totalProductos);
            model.addAttribute("productosActivos", productosActivos);
            model.addAttribute("productosInactivos", productosInactivos);
            model.addAttribute("productosStockBajo", productosStockBajo);

        } catch (Exception e) {
            log.error("Error al listar productos: {}", e.getMessage(), e);
            model.addAttribute("error", "Error al cargar los productos: " + e.getMessage());
        }

        return "admin/productos/lista";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        log.info("Mostrando formulario de nuevo producto");

        try {
            model.addAttribute("titulo", "Nuevo Producto");
            model.addAttribute("subtitulo", "Registrar nuevo producto en el sistema");
            model.addAttribute("producto", new ProductoDto());
            model.addAttribute("categorias", categoriaService.findAllIncludingInactive());
            model.addAttribute("estados", EstadoProducto.values());
            model.addAttribute("esNuevo", true);

        } catch (Exception e) {
            log.error("Error al cargar formulario de nuevo producto: {}", e.getMessage(), e);
            model.addAttribute("error", "Error al cargar el formulario: " + e.getMessage());
        }

        return "admin/productos/form";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        log.info("Mostrando formulario de edición para producto ID: {}", id);

        try {
            ProductoDto producto = productoService.findById(id)
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

            model.addAttribute("titulo", "Editar Producto");
            model.addAttribute("subtitulo", "Modificar datos del producto");
            model.addAttribute("producto", producto);
            model.addAttribute("categorias", categoriaService.findAllIncludingInactive());
            model.addAttribute("estados", EstadoProducto.values());
            model.addAttribute("esNuevo", false);

        } catch (Exception e) {
            log.error("Error al cargar formulario de edición: {}", e.getMessage(), e);
            model.addAttribute("error", "Error al cargar el formulario: " + e.getMessage());
        }

        return "admin/productos/form";
    }

    @PostMapping("/guardar")
    public String guardarProducto(@Valid ProductoDto producto,
            BindingResult result,
            @RequestParam(required = false) MultipartFile imagen,
            Model model,
            RedirectAttributes redirectAttributes) {

        log.info("Guardando producto: {}", producto.getCodigoProducto());

        try {
            if (result.hasErrors()) {
                model.addAttribute("titulo", producto.getIdProducto() == null ? "Nuevo Producto" : "Editar Producto");
                model.addAttribute("subtitulo",
                        producto.getIdProducto() == null ? "Registrar nuevo producto" : "Modificar datos del producto");
                model.addAttribute("esNuevo", producto.getIdProducto() == null);
                model.addAttribute("categorias", categoriaService.findAllIncludingInactive());
                model.addAttribute("estados", EstadoProducto.values());
                model.addAttribute("error", "Error en los datos del producto");
                return "admin/productos/form";
            }

            productoService.save(producto, imagen);

            String mensaje = producto.getIdProducto() == null ? "Producto creado exitosamente"
                    : "Producto actualizado exitosamente";
            redirectAttributes.addFlashAttribute("success", mensaje);

        } catch (Exception e) {
            log.error("Error al guardar producto: {}", e.getMessage(), e);
            redirectAttributes.addFlashAttribute("error", "Error al guardar el producto: " + e.getMessage());
        }

        return "redirect:/admin/productos";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarProducto(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        log.info("Desactivando producto ID: {}", id);

        try {
            productoService.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Producto desactivado exitosamente");

        } catch (Exception e) {
            log.error("Error al desactivar producto: {}", e.getMessage(), e);
            redirectAttributes.addFlashAttribute("error", "Error al desactivar el producto: " + e.getMessage());
        }

        return "redirect:/admin/productos";
    }

    @GetMapping("/cambiar-estado/{id}")
    public String cambiarEstadoProducto(@PathVariable Long id,
            @RequestParam EstadoProducto nuevoEstado,
            RedirectAttributes redirectAttributes) {
        log.info("Cambiando estado del producto ID: {} a {}", id, nuevoEstado);

        try {
            ProductoDto producto = productoService.findById(id)
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

            producto.setEstado(nuevoEstado);
            productoService.save(producto, null);

            redirectAttributes.addFlashAttribute("success",
                    "Estado del producto cambiado a " + nuevoEstado + " exitosamente");

        } catch (Exception e) {
            log.error("Error al cambiar estado del producto: {}", e.getMessage(), e);
            redirectAttributes.addFlashAttribute("error", "Error al cambiar el estado: " + e.getMessage());
        }

        return "redirect:/admin/productos";
    }
}
