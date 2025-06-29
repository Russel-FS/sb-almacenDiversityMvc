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

import com.api.diversity.application.dto.ClienteDto;
import com.api.diversity.application.service.interfaces.IClienteService;
import com.api.diversity.domain.enums.EstadoCliente;
import com.api.diversity.domain.enums.TipoCliente;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.api.diversity.infrastructure.security.CustomUser;

@Controller
@RequestMapping("/clientes")
@RequiredArgsConstructor
@Slf4j
public class ClienteController {

    private final IClienteService clienteService;

    @GetMapping("")
    public String listarClientes(
            @RequestParam(required = false) String busqueda,
            @RequestParam(required = false) EstadoCliente estado,
            @RequestParam(required = false) TipoCliente tipoCliente,
            Model model) {

        log.info("Listando clientes - búsqueda: {}, estado: {}, tipo: {}", busqueda, estado, tipoCliente);

        try {
            List<ClienteDto> clientes;

            if (busqueda != null && !busqueda.trim().isEmpty()) {
                clientes = clienteService.findByNombreCompletoContainingIgnoreCase(busqueda);
            } else if (estado != null) {
                clientes = clienteService.findByEstado(estado);
            } else if (tipoCliente != null) {
                clientes = clienteService.findByTipoCliente(tipoCliente);
            } else {
                clientes = clienteService.findAll()
                        .stream()
                        .filter(c -> c.getEstado() == EstadoCliente.Activo)
                        .toList();
            }

            // Estadísticas
            Long totalClientes = clienteService.countTotal();
            Long clientesActivos = clienteService.countByEstado(EstadoCliente.Activo);
            Long clientesInactivos = clienteService.countByEstado(EstadoCliente.Inactivo);
            Long personasNaturales = clienteService.countByTipoCliente(TipoCliente.Persona_Natural);
            Long empresas = clienteService.countByTipoCliente(TipoCliente.Empresa);

            // datos
            model.addAttribute("titulo", "Gestión de Clientes");
            model.addAttribute("subtitulo", "Administrar clientes del sistema");
            model.addAttribute("clientes", clientes);
            model.addAttribute("totalClientes", totalClientes);
            model.addAttribute("clientesActivos", clientesActivos);
            model.addAttribute("clientesInactivos", clientesInactivos);
            model.addAttribute("personasNaturales", personasNaturales);
            model.addAttribute("empresas", empresas);
            model.addAttribute("estados", EstadoCliente.values());
            model.addAttribute("tiposCliente", TipoCliente.values());
            model.addAttribute("busqueda", busqueda);
            model.addAttribute("estadoFiltro", estado);
            model.addAttribute("tipoClienteFiltro", tipoCliente);

        } catch (Exception e) {
            log.error("Error al listar clientes: {}", e.getMessage(), e);
            model.addAttribute("error", "Error al cargar la lista de clientes: " + e.getMessage());
        }

        return "clientes/lista";
    }

    @GetMapping("/nuevo")
    public String nuevoCliente(Model model) {
        log.info("Mostrando formulario de nuevo cliente");

        try {
            model.addAttribute("titulo", "Nuevo Cliente");
            model.addAttribute("subtitulo", "Registrar nuevo cliente en el sistema");
            model.addAttribute("cliente", new ClienteDto());
            model.addAttribute("tiposCliente", TipoCliente.values());
            model.addAttribute("estados", EstadoCliente.values());

        } catch (Exception e) {
            log.error("Error al cargar formulario de nuevo cliente: {}", e.getMessage(), e);
            model.addAttribute("error", "Error al cargar el formulario: " + e.getMessage());
        }

        return "clientes/form";
    }

    @PostMapping("/guardar")
    public String guardarCliente(
            @ModelAttribute ClienteDto cliente,
            @AuthenticationPrincipal CustomUser usuarioActual,
            RedirectAttributes redirectAttributes) {

        log.info("Guardando cliente: {} por usuario: {}",
                cliente.getNombreCompleto(), usuarioActual != null ? usuarioActual.getUsername() : "Sistema");

        try {
            // Asignar el usuario que crea el cliente
            if (usuarioActual != null) {
                cliente.setCreatedBy(usuarioActual.getId());
            }

            clienteService.save(cliente);
            redirectAttributes.addFlashAttribute("success",
                    "Cliente '" + cliente.getNombreCompleto() + "' registrado exitosamente");

        } catch (Exception e) {
            log.error("Error al guardar cliente: {}", e.getMessage(), e);
            redirectAttributes.addFlashAttribute("error",
                    "Error al guardar el cliente: " + e.getMessage());
        }

        return "redirect:/clientes";
    }

    @GetMapping("/editar/{id}")
    public String editarCliente(@PathVariable Long id, Model model) {
        log.info("Editando cliente ID: {}", id);

        try {
            ClienteDto cliente = clienteService.findById(id);

            model.addAttribute("titulo", "Editar Cliente");
            model.addAttribute("subtitulo", "Modificar información del cliente");
            model.addAttribute("cliente", cliente);
            model.addAttribute("tiposCliente", TipoCliente.values());
            model.addAttribute("estados", EstadoCliente.values());

        } catch (Exception e) {
            log.error("Error al cargar cliente para editar: {}", e.getMessage(), e);
            model.addAttribute("error", "Error al cargar el cliente: " + e.getMessage());
            return "redirect:/clientes";
        }

        return "clientes/form";
    }

    @PostMapping("/actualizar")
    public String actualizarCliente(
            @ModelAttribute ClienteDto cliente,
            @AuthenticationPrincipal CustomUser usuarioActual,
            RedirectAttributes redirectAttributes) {

        log.info("Actualizando cliente ID: {} por usuario: {}",
                cliente.getIdCliente(), usuarioActual != null ? usuarioActual.getUsername() : "Sistema");

        try {
            // Asignar el usuario que actualiza el cliente
            if (usuarioActual != null) {
                cliente.setUpdatedBy(usuarioActual.getId());
            }

            clienteService.update(cliente);
            redirectAttributes.addFlashAttribute("success",
                    "Cliente '" + cliente.getNombreCompleto() + "' actualizado exitosamente");

        } catch (Exception e) {
            log.error("Error al actualizar cliente: {}", e.getMessage(), e);
            redirectAttributes.addFlashAttribute("error",
                    "Error al actualizar el cliente: " + e.getMessage());
        }

        return "redirect:/clientes";
    }

    @GetMapping("/detalle/{id}")
    public String detalleCliente(@PathVariable Long id, Model model) {
        log.info("Mostrando detalle del cliente ID: {}", id);

        try {
            ClienteDto cliente = clienteService.findById(id);

            model.addAttribute("titulo", "Detalle del Cliente");
            model.addAttribute("subtitulo", "Información completa del cliente");
            model.addAttribute("cliente", cliente);

        } catch (Exception e) {
            log.error("Error al cargar detalle del cliente: {}", e.getMessage(), e);
            model.addAttribute("error", "Error al cargar el detalle del cliente: " + e.getMessage());
            return "redirect:/clientes";
        }

        return "clientes/detalle";
    }

    @PostMapping("/cambiar-estado/{id}")
    public String cambiarEstadoCliente(
            @PathVariable Long id,
            @RequestParam EstadoCliente nuevoEstado,
            RedirectAttributes redirectAttributes) {

        log.info("Cambiando estado del cliente ID: {} a {}", id, nuevoEstado);

        try {
            ClienteDto cliente = clienteService.cambiarEstado(id, nuevoEstado);
            redirectAttributes.addFlashAttribute("success",
                    "Estado del cliente '" + cliente.getNombreCompleto() + "' cambiado a " + nuevoEstado);

        } catch (Exception e) {
            log.error("Error al cambiar estado del cliente: {}", e.getMessage(), e);
            redirectAttributes.addFlashAttribute("error",
                    "Error al cambiar el estado del cliente: " + e.getMessage());
        }

        return "redirect:/clientes";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarCliente(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        log.info("Eliminando cliente ID: {}", id);

        try {
            clienteService.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Cliente eliminado exitosamente");

        } catch (Exception e) {
            log.error("Error al eliminar cliente: {}", e.getMessage(), e);
            redirectAttributes.addFlashAttribute("error",
                    "Error al eliminar el cliente: " + e.getMessage());
        }

        return "redirect:/clientes";
    }
}