package com.api.diversity.infrastructure.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.api.diversity.application.dto.ProveedorDto;
import com.api.diversity.application.service.interfaces.IProveedorService;
import com.api.diversity.domain.enums.EstadoProveedor;
import com.api.diversity.infrastructure.security.SecurityContext;
import org.springframework.security.core.GrantedAuthority;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/proveedores")
@RequiredArgsConstructor
@Slf4j
public class ProveedorController {

    private final IProveedorService proveedorService;
    private final SecurityContext securityContext;

    @GetMapping("")
    public String listarProveedores(
            @RequestParam(required = false) String busqueda,
            @RequestParam(required = false) EstadoProveedor estado,
            Model model) {

        log.info("Listando proveedores - búsqueda: {}, estado: {}", busqueda, estado);

        try {
            List<ProveedorDto> proveedores;

            if (busqueda != null && !busqueda.trim().isEmpty()) {
                proveedores = proveedorService.findByRazonSocialContainingIgnoreCase(busqueda);
            } else if (estado != null) {
                proveedores = proveedorService.findByEstado(estado);
            } else {
                proveedores = proveedorService.findAll()
                        .stream()
                        .filter(p -> p.getEstado() != EstadoProveedor.Eliminado)
                        .toList();
            }

            // Estadísticas
            Long totalProveedores = proveedorService.countTotal();
            Long proveedoresActivos = proveedorService.countByEstado(EstadoProveedor.Activo);
            Long proveedoresInactivos = proveedorService.countByEstado(EstadoProveedor.Inactivo);

            model.addAttribute("titulo", "Gestión de Proveedores");
            model.addAttribute("subtitulo", "Administrar proveedores del sistema");
            model.addAttribute("proveedores", proveedores);
            model.addAttribute("totalProveedores", totalProveedores);
            model.addAttribute("proveedoresActivos", proveedoresActivos);
            model.addAttribute("proveedoresInactivos", proveedoresInactivos);
            model.addAttribute("estados", EstadoProveedor.values());
            model.addAttribute("busqueda", busqueda);
            model.addAttribute("estadoFiltro", estado);

        } catch (Exception e) {
            log.error("Error al listar proveedores: {}", e.getMessage(), e);
            model.addAttribute("error", "Error al cargar los proveedores: " + e.getMessage());
            model.addAttribute("proveedores", List.of());
        }

        return "proveedores/lista";
    }

    @GetMapping("/admin")
    public String listarProveedoresAdmin(
            @RequestParam(required = false) String busqueda,
            @RequestParam(required = false) EstadoProveedor estado,
            Model model) {

        log.info("Listando proveedores (ADMIN) - búsqueda: {}, estado: {}", busqueda, estado);

        try {
            var user = securityContext.getCurrentUser();
            boolean isAdmin = false;
            if (user != null) {
                for (GrantedAuthority auth : user.getAuthorities()) {
                    if (auth.getAuthority().equals("ROLE_ADMIN")) {
                        isAdmin = true;
                        break;
                    }
                }
            }

            if (!isAdmin) {
                log.warn("Usuario no autorizado intentando acceder a vista de administrador");
                return "redirect:/proveedores";
            }

            List<ProveedorDto> proveedores;

            if (busqueda != null && !busqueda.trim().isEmpty()) {
                proveedores = proveedorService.findByRazonSocialContainingIgnoreCase(busqueda);
            } else if (estado != null) {
                proveedores = proveedorService.findByEstado(estado);
            } else {
                // Para admin, mostrar TODOS los proveedores (incluidos eliminados)
                proveedores = proveedorService.findAll();
            }

            // Estadísticas completas para admin
            Long totalProveedores = proveedorService.countTotal();
            Long proveedoresActivos = proveedorService.countByEstado(EstadoProveedor.Activo);
            Long proveedoresInactivos = proveedorService.countByEstado(EstadoProveedor.Inactivo);
            Long proveedoresEliminados = proveedorService.countByEstado(EstadoProveedor.Eliminado);

            model.addAttribute("titulo", "Gestión de Proveedores - Administrador");
            model.addAttribute("subtitulo", "Vista completa de todos los proveedores");
            model.addAttribute("proveedores", proveedores);
            model.addAttribute("totalProveedores", totalProveedores);
            model.addAttribute("proveedoresActivos", proveedoresActivos);
            model.addAttribute("proveedoresInactivos", proveedoresInactivos);
            model.addAttribute("proveedoresEliminados", proveedoresEliminados);
            model.addAttribute("estados", EstadoProveedor.values());
            model.addAttribute("busqueda", busqueda);
            model.addAttribute("estadoFiltro", estado);
            model.addAttribute("isAdmin", true);

        } catch (Exception e) {
            log.error("Error al listar proveedores (ADMIN): {}", e.getMessage(), e);
            model.addAttribute("error", "Error al cargar los proveedores: " + e.getMessage());
            model.addAttribute("proveedores", List.of());
        }

        return "proveedores/lista";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        log.info("Mostrando formulario de nuevo proveedor");

        try {
            model.addAttribute("titulo", "Nuevo Proveedor");
            model.addAttribute("subtitulo", "Registrar nuevo proveedor");
            model.addAttribute("proveedor", new ProveedorDto());
            model.addAttribute("estados", EstadoProveedor.values());
            model.addAttribute("esNuevo", true);

        } catch (Exception e) {
            log.error("Error al cargar formulario de nuevo proveedor: {}", e.getMessage(), e);
            model.addAttribute("error", "Error al cargar el formulario: " + e.getMessage());
        }

        return "proveedores/form";
    }

    @PostMapping("/guardar")
    public String guardarProveedor(
            @ModelAttribute ProveedorDto proveedor,
            RedirectAttributes redirectAttributes) {

        log.info("Guardando proveedor: {}", proveedor.getRazonSocial());

        try {
            if (proveedor.getIdProveedor() == null) {
                // Nuevo proveedor
                proveedorService.save(proveedor);
                redirectAttributes.addFlashAttribute("mensaje", "Proveedor registrado exitosamente");
                log.info("Proveedor registrado exitosamente: {}", proveedor.getRazonSocial());
            } else {
                // Actualizar proveedor existente
                proveedorService.update(proveedor);
                redirectAttributes.addFlashAttribute("mensaje", "Proveedor actualizado exitosamente");
                log.info("Proveedor actualizado exitosamente: {}", proveedor.getRazonSocial());
            }

        } catch (Exception e) {
            log.error("Error al guardar proveedor: {}", e.getMessage(), e);
            redirectAttributes.addFlashAttribute("error", "Error al guardar proveedor: " + e.getMessage());
            redirectAttributes.addFlashAttribute("proveedor", proveedor);
            return "redirect:/proveedores/nuevo";
        }

        return "redirect:/proveedores";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        log.info("Mostrando formulario de edición para proveedor ID: {}", id);

        try {
            ProveedorDto proveedor = proveedorService.findById(id);

            model.addAttribute("titulo", "Editar Proveedor");
            model.addAttribute("subtitulo", "Modificar datos del proveedor");
            model.addAttribute("proveedor", proveedor);
            model.addAttribute("estados", EstadoProveedor.values());
            model.addAttribute("esNuevo", false);

        } catch (Exception e) {
            log.error("Error al cargar proveedor para edición ID {}: {}", id, e.getMessage(), e);
            model.addAttribute("error", "Error al cargar el proveedor: " + e.getMessage());
            return "redirect:/proveedores";
        }

        return "proveedores/form";
    }

    @GetMapping("/ver/{id}")
    public String verProveedor(@PathVariable Long id, Model model) {
        log.info("Mostrando detalles del proveedor ID: {}", id);

        try {
            ProveedorDto proveedor = proveedorService.findById(id);

            model.addAttribute("titulo", "Detalles del Proveedor");
            model.addAttribute("subtitulo", "Información completa del proveedor");
            model.addAttribute("proveedor", proveedor);

        } catch (Exception e) {
            log.error("Error al cargar detalles del proveedor ID {}: {}", id, e.getMessage(), e);
            model.addAttribute("error", "Error al cargar los detalles del proveedor: " + e.getMessage());
            return "redirect:/proveedores";
        }

        return "proveedores/detalle";
    }

    @PostMapping("/{id}/cambiar-estado")
    public String cambiarEstado(
            @PathVariable Long id,
            @RequestParam EstadoProveedor nuevoEstado,
            RedirectAttributes redirectAttributes) {

        log.info("Cambiando estado del proveedor ID: {} a {}", id, nuevoEstado);

        try {
            proveedorService.cambiarEstado(id, nuevoEstado);
            redirectAttributes.addFlashAttribute("mensaje",
                    "Estado del proveedor cambiado a " + nuevoEstado.name() + " exitosamente");

        } catch (Exception e) {
            log.error("Error al cambiar estado del proveedor ID {}: {}", id, e.getMessage(), e);
            redirectAttributes.addFlashAttribute("error", "Error al cambiar estado: " + e.getMessage());
        }

        return "redirect:/proveedores";
    }

    @PostMapping("/{id}/eliminar")
    public String eliminarProveedor(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        log.info("Eliminando proveedor ID: {}", id);

        try {
            proveedorService.deleteById(id);
            redirectAttributes.addFlashAttribute("mensaje", "Proveedor eliminado exitosamente");

        } catch (Exception e) {
            log.error("Error al eliminar proveedor ID {}: {}", id, e.getMessage(), e);
            redirectAttributes.addFlashAttribute("error", "Error al eliminar proveedor: " + e.getMessage());
        }

        return "redirect:/proveedores";
    }

    @GetMapping("/buscar")
    public String buscarProveedores(
            @RequestParam String termino,
            Model model) {

        log.info("Buscando proveedores con término: {}", termino);

        try {
            List<ProveedorDto> proveedores = proveedorService.findByRazonSocialContainingIgnoreCase(termino);

            model.addAttribute("titulo", "Búsqueda de Proveedores");
            model.addAttribute("subtitulo", "Resultados para: " + termino);
            model.addAttribute("proveedores", proveedores);
            model.addAttribute("termino", termino);
            model.addAttribute("totalResultados", proveedores.size());

        } catch (Exception e) {
            log.error("Error en búsqueda de proveedores: {}", e.getMessage(), e);
            model.addAttribute("error", "Error en la búsqueda: " + e.getMessage());
            model.addAttribute("proveedores", List.of());
        }

        return "proveedores/lista";
    }
}