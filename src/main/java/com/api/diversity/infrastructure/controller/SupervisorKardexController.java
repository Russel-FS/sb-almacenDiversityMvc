package com.api.diversity.infrastructure.controller;

import com.api.diversity.application.dto.EntradaDto;
import com.api.diversity.application.dto.SalidaDto;
import com.api.diversity.application.service.interfaces.IEntradaService;
import com.api.diversity.application.service.interfaces.ISalidaService;
import com.api.diversity.application.service.interfaces.IRubroService;
import com.api.diversity.domain.enums.EstadoEntrada;
import com.api.diversity.domain.enums.EstadoSalida;
import com.api.diversity.domain.enums.TipoRubro;
import com.api.diversity.infrastructure.security.CustomUser;
import com.api.diversity.application.dto.RubroDto;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/supervisor/kardex")
@RequiredArgsConstructor
@Slf4j
public class SupervisorKardexController {

    private final IEntradaService entradaService;
    private final ISalidaService salidaService;
    private final IRubroService rubroService;

    @GetMapping("/{rubroCode}/pendientes")
    public String showPendingMovements(@PathVariable String rubroCode,
            Model model,
            @AuthenticationPrincipal CustomUser user) {
        try {
            // Validar que el usuario tenga permisos para este rubro
            validateRubroAccess(rubroCode, user);

            TipoRubro tipoRubro = TipoRubro.fromCode(rubroCode);
            Optional<RubroDto> rubroOptional = rubroService.findByNombreRubro(tipoRubro.getNombre());

            if (rubroOptional.isEmpty()) {
                model.addAttribute("error", "Rubro no encontrado: " + rubroCode);
                return "error";
            }

            Long rubroId = rubroOptional.get().getIdRubro();

            List<EntradaDto> entradasPendientes = entradaService.findByRubroIdAndEstado(rubroId,
                    EstadoEntrada.Pendiente);
            List<SalidaDto> salidasPendientes = salidaService.findByRubroIdAndEstado(rubroId, EstadoSalida.Pendiente);

            model.addAttribute("rubroNombre", tipoRubro.getNombre());
            model.addAttribute("rubroCode", rubroCode);
            model.addAttribute("entradasPendientes", entradasPendientes);
            model.addAttribute("salidasPendientes", salidasPendientes);

            return "supervisor/kardex/pendientes";
        } catch (AccessDeniedException e) {
            log.warn("Usuario {} intentó acceder a rubro {} sin autorización", user.getUsername(), rubroCode);
            model.addAttribute("error", "No tienes permisos para acceder a este rubro.");
            return "error/access-denied";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", "Código de rubro inválido: " + rubroCode);
            return "error";
        } catch (Exception e) {
            log.error("Error al cargar movimientos pendientes para rubro {}: {}", rubroCode, e.getMessage(), e);
            model.addAttribute("error", "Error al cargar movimientos pendientes.");
            return "error";
        }
    }

    @PostMapping("/aprobar/entrada/{id}")
    public String approveEntrada(@PathVariable Long id,
            @RequestParam String rubroCode,
            HttpSession session,
            RedirectAttributes redirectAttributes,
            @AuthenticationPrincipal CustomUser user) {
        try {
            // Validar que el usuario tenga permisos para este rubro
            validateRubroAccess(rubroCode, user);

            Long usuarioAprobacionId = user.getId();
            entradaService.aprobarEntrada(id, usuarioAprobacionId);

            redirectAttributes.addFlashAttribute("mensaje", "Entrada aprobada exitosamente.");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (AccessDeniedException e) {
            log.warn("Usuario {} intentó aprobar entrada {} en rubro {} sin autorización",
                    user.getUsername(), id, rubroCode);
            redirectAttributes.addFlashAttribute("mensaje",
                    "No tienes permisos para aprobar movimientos en este rubro.");
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        } catch (Exception e) {
            log.error("Error al aprobar entrada {}: {}", id, e.getMessage(), e);
            redirectAttributes.addFlashAttribute("mensaje", "Error al aprobar entrada: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        }
        redirectAttributes.addAttribute("rubroCode", rubroCode);
        return "redirect:/supervisor/kardex/{rubroCode}/pendientes";
    }

    @PostMapping("/anular/entrada/{id}")
    public String cancelEntrada(@PathVariable Long id,
            @RequestParam(required = false) String motivo,
            @RequestParam String rubroCode,
            RedirectAttributes redirectAttributes,
            @AuthenticationPrincipal CustomUser user) {
        try {
            // Validar que el usuario tenga permisos para este rubro
            validateRubroAccess(rubroCode, user);

            entradaService.anularEntrada(id, motivo);
            redirectAttributes.addFlashAttribute("mensaje", "Entrada anulada exitosamente.");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (AccessDeniedException e) {
            log.warn("Usuario {} intentó anular entrada {} en rubro {} sin autorización",
                    user.getUsername(), id, rubroCode);
            redirectAttributes.addFlashAttribute("mensaje",
                    "No tienes permisos para anular movimientos en este rubro.");
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        } catch (Exception e) {
            log.error("Error al anular entrada {}: {}", id, e.getMessage(), e);
            redirectAttributes.addFlashAttribute("mensaje", "Error al anular entrada: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        }
        redirectAttributes.addAttribute("rubroCode", rubroCode);
        return "redirect:/supervisor/kardex/{rubroCode}/pendientes";
    }

    @PostMapping("/aprobar/salida/{id}")
    public String approveSalida(@PathVariable Long id,
            @RequestParam String rubroCode,
            HttpSession session,
            RedirectAttributes redirectAttributes,
            @AuthenticationPrincipal CustomUser user) {
        try {
            // Validar que el usuario tenga permisos para este rubro
            validateRubroAccess(rubroCode, user);

            Long usuarioAprobacionId = user.getId();
            salidaService.aprobarSalida(id, usuarioAprobacionId);

            redirectAttributes.addFlashAttribute("mensaje", "Salida aprobada exitosamente.");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (AccessDeniedException e) {
            log.warn("Usuario {} intentó aprobar salida {} en rubro {} sin autorización",
                    user.getUsername(), id, rubroCode);
            redirectAttributes.addFlashAttribute("mensaje",
                    "No tienes permisos para aprobar movimientos en este rubro.");
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        } catch (Exception e) {
            log.error("Error al aprobar salida {}: {}", id, e.getMessage(), e);
            redirectAttributes.addFlashAttribute("mensaje", "Error al aprobar salida: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        }
        redirectAttributes.addAttribute("rubroCode", rubroCode);
        return "redirect:/supervisor/kardex/{rubroCode}/pendientes";
    }

    @PostMapping("/anular/salida/{id}")
    public String cancelSalida(@PathVariable Long id,
            @RequestParam(required = false) String motivo,
            @RequestParam String rubroCode,
            RedirectAttributes redirectAttributes,
            @AuthenticationPrincipal CustomUser user) {
        try {
            // Validar que el usuario tenga permisos para este rubro
            validateRubroAccess(rubroCode, user);

            salidaService.anularSalida(id, motivo);
            redirectAttributes.addFlashAttribute("mensaje", "Salida anulada exitosamente.");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (AccessDeniedException e) {
            log.warn("Usuario {} intentó anular salida {} en rubro {} sin autorización",
                    user.getUsername(), id, rubroCode);
            redirectAttributes.addFlashAttribute("mensaje",
                    "No tienes permisos para anular movimientos en este rubro.");
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        } catch (Exception e) {
            log.error("Error al anular salida {}: {}", id, e.getMessage(), e);
            redirectAttributes.addFlashAttribute("mensaje", "Error al anular salida: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        }
        redirectAttributes.addAttribute("rubroCode", rubroCode);
        return "redirect:/supervisor/kardex/{rubroCode}/pendientes";
    }

    /**
     * Valida que el usuario tenga permisos para acceder al rubro especificado
     */
    private void validateRubroAccess(String rubroCode, CustomUser user) {
        // Administradores y supervisores generales tienen acceso a todos los rubros
        if (user.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMINISTRADOR") ||
                auth.getAuthority().equals("ROLE_SUPERVISOR_GENERAL"))) {
            return;
        }

        // validacion especifico por rubro
        String requiredRole = getRequiredRoleForRubro(rubroCode);
        if (requiredRole != null && user.getAuthorities().stream()
                .noneMatch(auth -> auth.getAuthority().equals("ROLE_" + requiredRole))) {
            throw new AccessDeniedException("Usuario no tiene permisos para acceder al rubro: " + rubroCode);
        }
    }

    /**
     * Obtiene el rol requerido para un rubro específico
     */
    private String getRequiredRoleForRubro(String rubroCode) {
        return switch (rubroCode) {
            case "PIÑAT" -> "SUPERVISOR_PINATERIA";
            case "LIBR" -> "SUPERVISOR_LIBRERIA";
            case "CSEG" -> "SUPERVISOR_CAMARAS";
            default -> null;
        };
    }
}
