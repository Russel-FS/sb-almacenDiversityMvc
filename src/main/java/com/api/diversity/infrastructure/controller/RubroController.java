package com.api.diversity.infrastructure.controller;

import com.api.diversity.application.dto.RubroDto;
import com.api.diversity.application.service.IRubroService;

import lombok.extern.log4j.Log4j2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/rubros")
@Log4j2
public class RubroController {

    @Autowired
    private IRubroService rubroService;

    @GetMapping
    public String listarRubros(Model model) {
        model.addAttribute("rubros", rubroService.findAll());
        return "rubros/lista";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        model.addAttribute("rubro", rubroService.findById(id));
        return "rubros/form";
    }

    @PostMapping("/guardar")
    public String guardarRubro(@ModelAttribute RubroDto rubro, RedirectAttributes redirectAttributes) {
        try {
            if (rubro.getIdRubro() == null) {
                redirectAttributes.addFlashAttribute("mensaje",
                        "No se puede crear un nuevo rubro. Los rubros son predefinidos.");
                redirectAttributes.addFlashAttribute("tipoMensaje", "error");
                return "redirect:/rubros";
            }

            if (rubro.getNombreRubro() == null || rubro.getNombreRubro().trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("mensaje", "El nombre del rubro no puede estar vacío.");
                redirectAttributes.addFlashAttribute("tipoMensaje", "error");
                return "redirect:/rubros/editar/" + rubro.getIdRubro();
            }

            RubroDto rubroOriginal = rubroService.findById(rubro.getIdRubro());
            rubro.setCode(rubroOriginal.getCode());

            log.info("Guardando rubro: {}", rubro);
            rubroService.save(rubro);
            redirectAttributes.addFlashAttribute("mensaje", "Rubro actualizado exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error al actualizar el rubro: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        }
        return "redirect:/rubros";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarRubro(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            rubroService.deleteById(id);
            redirectAttributes.addFlashAttribute("mensaje", "Rubro eliminado exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error al eliminar el rubro: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        }
        return "redirect:/rubros";
    }
}