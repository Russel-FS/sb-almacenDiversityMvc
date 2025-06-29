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
                        .filter(c -> c.getEstado() != EstadoCliente.Eliminado)
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
            model.addAttribute("clientes", List.of());
        }

        return "clientes/lista";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        log.info("Mostrando formulario de nuevo cliente");

        try {
            model.addAttribute("titulo", "Nuevo Cliente");
            model.addAttribute("subtitulo", "Registrar nuevo cliente");
            model.addAttribute("cliente", new ClienteDto());
            model.addAttribute("tiposCliente", TipoCliente.values());
            model.addAttribute("estados", EstadoCliente.values());
            model.addAttribute("esNuevo", true);

        } catch (Exception e) {
            log.error("Error al cargar formulario de nuevo cliente: {}", e.getMessage(), e);
            model.addAttribute("error", "Error al cargar el formulario: " + e.getMessage());
        }

        return "clientes/form";
    }

    @PostMapping("/guardar")
    public String guardarCliente(
            @ModelAttribute ClienteDto cliente,
            RedirectAttributes redirectAttributes) {

        log.info("Guardando cliente: {}", cliente.getNombreCompleto());

        try {
            if (cliente.getIdCliente() == null) {
                // Nuevo cliente
                clienteService.save(cliente);
                redirectAttributes.addFlashAttribute("mensaje", "Cliente registrado exitosamente");
                log.info("Cliente registrado exitosamente: {}", cliente.getNombreCompleto());
            } else {
                // Actualizar cliente existente
                clienteService.update(cliente);
                redirectAttributes.addFlashAttribute("mensaje", "Cliente actualizado exitosamente");
                log.info("Cliente actualizado exitosamente: {}", cliente.getNombreCompleto());
            }

        } catch (Exception e) {
            log.error("Error al guardar cliente: {}", e.getMessage(), e);
            redirectAttributes.addFlashAttribute("error", "Error al guardar cliente: " + e.getMessage());
            redirectAttributes.addFlashAttribute("cliente", cliente);
            return "redirect:/clientes/nuevo";
        }

        return "redirect:/clientes";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        log.info("Mostrando formulario de edición para cliente ID: {}", id);

        try {
            ClienteDto cliente = clienteService.findById(id);

            model.addAttribute("titulo", "Editar Cliente");
            model.addAttribute("subtitulo", "Modificar datos del cliente");
            model.addAttribute("cliente", cliente);
            model.addAttribute("tiposCliente", TipoCliente.values());
            model.addAttribute("estados", EstadoCliente.values());
            model.addAttribute("esNuevo", false);

        } catch (Exception e) {
            log.error("Error al cargar cliente para edición ID {}: {}", id, e.getMessage(), e);
            model.addAttribute("error", "Error al cargar el cliente: " + e.getMessage());
            return "redirect:/clientes";
        }

        return "clientes/form";
    }

    @GetMapping("/ver/{id}")
    public String verCliente(@PathVariable Long id, Model model) {
        log.info("Mostrando detalles del cliente ID: {}", id);

        try {
            ClienteDto cliente = clienteService.findById(id);

            model.addAttribute("titulo", "Detalles del Cliente");
            model.addAttribute("subtitulo", "Información completa del cliente");
            model.addAttribute("cliente", cliente);

        } catch (Exception e) {
            log.error("Error al cargar detalles del cliente ID {}: {}", id, e.getMessage(), e);
            model.addAttribute("error", "Error al cargar los detalles del cliente: " + e.getMessage());
            return "redirect:/clientes";
        }

        return "clientes/detalle";
    }

    @PostMapping("/{id}/cambiar-estado")
    public String cambiarEstado(
            @PathVariable Long id,
            @RequestParam EstadoCliente nuevoEstado,
            RedirectAttributes redirectAttributes) {

        log.info("Cambiando estado del cliente ID: {} a {}", id, nuevoEstado);

        try {
            clienteService.cambiarEstado(id, nuevoEstado);
            redirectAttributes.addFlashAttribute("mensaje",
                    "Estado del cliente cambiado a " + nuevoEstado.name() + " exitosamente");

        } catch (Exception e) {
            log.error("Error al cambiar estado del cliente ID {}: {}", id, e.getMessage(), e);
            redirectAttributes.addFlashAttribute("error", "Error al cambiar estado: " + e.getMessage());
        }

        return "redirect:/clientes";
    }

    @PostMapping("/{id}/eliminar")
    public String eliminarCliente(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        log.info("Eliminando cliente ID: {}", id);

        try {
            clienteService.deleteById(id);
            redirectAttributes.addFlashAttribute("mensaje", "Cliente eliminado exitosamente");

        } catch (Exception e) {
            log.error("Error al eliminar cliente ID {}: {}", id, e.getMessage(), e);
            redirectAttributes.addFlashAttribute("error", "Error al eliminar cliente: " + e.getMessage());
        }

        return "redirect:/clientes";
    }

    @GetMapping("/buscar")
    public String buscarClientes(
            @RequestParam String termino,
            Model model) {

        log.info("Buscando clientes con término: {}", termino);

        try {
            List<ClienteDto> clientes = clienteService.findByNombreCompletoContainingIgnoreCase(termino);

            model.addAttribute("titulo", "Búsqueda de Clientes");
            model.addAttribute("subtitulo", "Resultados para: " + termino);
            model.addAttribute("clientes", clientes);
            model.addAttribute("termino", termino);
            model.addAttribute("totalResultados", clientes.size());

        } catch (Exception e) {
            log.error("Error en búsqueda de clientes: {}", e.getMessage(), e);
            model.addAttribute("error", "Error en la búsqueda: " + e.getMessage());
            model.addAttribute("clientes", List.of());
        }

        return "clientes/lista";
    }
}