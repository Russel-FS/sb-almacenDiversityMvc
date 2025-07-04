package com.api.diversity.infrastructure.controller;

import com.api.diversity.application.dto.DevolucionRequestDto;
import com.api.diversity.application.dto.EntradaDto;
import com.api.diversity.application.service.interfaces.DevolucionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/almacenero/devoluciones")
@RequiredArgsConstructor
public class DevolucionController {

    private final DevolucionService devolucionService;

    @GetMapping("/nueva")
    public String showDevolucionForm(Model model) {
        model.addAttribute("devolucionRequest", new DevolucionRequestDto());
        return "almacenero/devolucion/form";
    }

    @PostMapping("/procesar")
    public String procesarDevolucion(DevolucionRequestDto devolucionRequest, RedirectAttributes redirectAttributes) {
        try {
            EntradaDto nuevaEntrada = devolucionService.procesarDevolucion(devolucionRequest);
            redirectAttributes.addFlashAttribute("mensaje", "Devolución procesada exitosamente. ID de Entrada: " + nuevaEntrada.getIdEntrada());
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error al procesar la devolución: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        }
        return "redirect:/almacenero/devoluciones/nueva";
    }
}
