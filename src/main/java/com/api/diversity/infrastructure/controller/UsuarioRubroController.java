package com.api.diversity.infrastructure.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.api.diversity.application.dto.UsuarioRubroDto;
import com.api.diversity.application.service.interfaces.IUsuarioRubroService;
import com.api.diversity.application.service.interfaces.IUsuarioService;
import com.api.diversity.application.service.interfaces.IRubroService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/admin/usuario-rubros")
@RequiredArgsConstructor
@Slf4j
public class UsuarioRubroController {

    private final IUsuarioRubroService usuarioRubroService;
    private final IUsuarioService usuarioService;
    private final IRubroService rubroService;

    /**
     * Muestra la lista de todas las asignaciones usuario-rubro
     */
    @GetMapping("/lista")
    public String listarAsignaciones(Model model) {
        try {
            List<UsuarioRubroDto> asignaciones = usuarioRubroService.findAll();
            model.addAttribute("asignaciones", asignaciones);
            model.addAttribute("usuarios", usuarioService.findAll());
            model.addAttribute("rubros", rubroService.findAll());
            return "admin/usuario-rubros/lista";
        } catch (Exception e) {
            log.error("Error al listar asignaciones: {}", e.getMessage(), e);
            model.addAttribute("error", "Error al cargar las asignaciones: " + e.getMessage());
            return "admin/usuario-rubros/lista";
        }
    }

    /**
     * Muestra las asignaciones de un usuario específico
     */
    @GetMapping("/usuario/{usuarioId}")
    public String asignacionesPorUsuario(@PathVariable Long usuarioId, Model model) {
        try {
            List<UsuarioRubroDto> asignaciones = usuarioRubroService.findByUsuarioId(usuarioId);
            model.addAttribute("asignaciones", asignaciones);
            model.addAttribute("usuario", usuarioService.findById(usuarioId));
            model.addAttribute("rubros", rubroService.findAll());
            return "admin/usuario-rubros/usuario";
        } catch (Exception e) {
            log.error("Error al obtener asignaciones del usuario {}: {}", usuarioId, e.getMessage(), e);
            model.addAttribute("error", "Error al cargar las asignaciones: " + e.getMessage());
            return "admin/usuario-rubros/usuario";
        }
    }

    /**
     * Muestra las asignaciones de un rubro específico
     */
    @GetMapping("/rubro/{rubroId}")
    public String asignacionesPorRubro(@PathVariable Long rubroId, Model model) {
        try {
            List<UsuarioRubroDto> asignaciones = usuarioRubroService.findByRubroId(rubroId);
            model.addAttribute("asignaciones", asignaciones);
            model.addAttribute("rubro", rubroService.findById(rubroId));
            model.addAttribute("usuarios", usuarioService.findAll());
            return "admin/usuario-rubros/rubro";
        } catch (Exception e) {
            log.error("Error al obtener asignaciones del rubro {}: {}", rubroId, e.getMessage(), e);
            model.addAttribute("error", "Error al cargar las asignaciones: " + e.getMessage());
            return "admin/usuario-rubros/rubro";
        }
    }

    /**
     * Asigna un rubro a un usuario
     */
    @PostMapping("/asignar")
    public String asignarRubro(@RequestParam Long usuarioId,
            @RequestParam Long rubroId,
            RedirectAttributes redirectAttributes) {
        try {
            usuarioRubroService.asignarRubroAUsuario(usuarioId, rubroId);
            redirectAttributes.addFlashAttribute("success", "Rubro asignado exitosamente al usuario");
        } catch (Exception e) {
            log.error("Error al asignar rubro {} al usuario {}: {}", rubroId, usuarioId, e.getMessage(), e);
            redirectAttributes.addFlashAttribute("error", "Error al asignar rubro: " + e.getMessage());
        }
        return "redirect:/admin/usuario-rubros/usuario/" + usuarioId;
    }

    /**
     * Quita un rubro de un usuario
     */
    @DeleteMapping("/quitar")
    public String quitarRubro(@RequestParam Long usuarioId,
            @RequestParam Long rubroId,
            RedirectAttributes redirectAttributes) {
        try {
            usuarioRubroService.quitarRubroDeUsuario(usuarioId, rubroId);
            redirectAttributes.addFlashAttribute("success", "Rubro removido exitosamente del usuario");
        } catch (Exception e) {
            log.error("Error al quitar rubro {} del usuario {}: {}", rubroId, usuarioId, e.getMessage(), e);
            redirectAttributes.addFlashAttribute("error", "Error al quitar rubro: " + e.getMessage());
        }
        return "redirect:/admin/usuario-rubros/usuario/" + usuarioId;
    }

    /**
     * Cambia el estado de una asignación
     */
    @PutMapping("/cambiar-estado")
    public String cambiarEstado(@RequestParam Long usuarioId,
            @RequestParam Long rubroId,
            @RequestParam String estado,
            RedirectAttributes redirectAttributes) {
        try {
            usuarioRubroService.cambiarEstadoAsignacion(usuarioId, rubroId, estado);
            redirectAttributes.addFlashAttribute("success", "Estado de asignación cambiado exitosamente");
        } catch (Exception e) {
            log.error("Error al cambiar estado de asignación: {}", e.getMessage(), e);
            redirectAttributes.addFlashAttribute("error", "Error al cambiar estado: " + e.getMessage());
        }
        return "redirect:/admin/usuario-rubros/usuario/" + usuarioId;
    }

    /**
     * API REST para obtener asignaciones por usuario
     */
    @GetMapping("/api/usuario/{usuarioId}")
    public ResponseEntity<List<UsuarioRubroDto>> getAsignacionesPorUsuario(@PathVariable Long usuarioId) {
        try {
            List<UsuarioRubroDto> asignaciones = usuarioRubroService.findByUsuarioId(usuarioId);
            return ResponseEntity.ok(asignaciones);
        } catch (Exception e) {
            log.error("Error al obtener asignaciones del usuario {}: {}", usuarioId, e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * API REST para obtener asignaciones activas por usuario
     */
    @GetMapping("/api/usuario/{usuarioId}/activas")
    public ResponseEntity<List<UsuarioRubroDto>> getAsignacionesActivasPorUsuario(@PathVariable Long usuarioId) {
        try {
            List<UsuarioRubroDto> asignaciones = usuarioRubroService.findByUsuarioIdAndEstado(usuarioId, "ACTIVO");
            return ResponseEntity.ok(asignaciones);
        } catch (Exception e) {
            log.error("Error al obtener asignaciones activas del usuario {}: {}", usuarioId, e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * API REST para asignar rubro a usuario
     */
    @PostMapping("/api/asignar")
    public ResponseEntity<String> asignarRubroAPI(@RequestParam Long usuarioId, @RequestParam Long rubroId) {
        try {
            usuarioRubroService.asignarRubroAUsuario(usuarioId, rubroId);
            return ResponseEntity.ok("Rubro asignado exitosamente");
        } catch (Exception e) {
            log.error("Error al asignar rubro {} al usuario {}: {}", rubroId, usuarioId, e.getMessage(), e);
            return ResponseEntity.badRequest().body("Error al asignar rubro: " + e.getMessage());
        }
    }

    /**
     * API REST para quitar rubro de usuario
     */
    @DeleteMapping("/api/quitar")
    public ResponseEntity<String> quitarRubroAPI(@RequestParam Long usuarioId, @RequestParam Long rubroId) {
        try {
            usuarioRubroService.quitarRubroDeUsuario(usuarioId, rubroId);
            return ResponseEntity.ok("Rubro removido exitosamente");
        } catch (Exception e) {
            log.error("Error al quitar rubro {} del usuario {}: {}", rubroId, usuarioId, e.getMessage(), e);
            return ResponseEntity.badRequest().body("Error al quitar rubro: " + e.getMessage());
        }
    }
}