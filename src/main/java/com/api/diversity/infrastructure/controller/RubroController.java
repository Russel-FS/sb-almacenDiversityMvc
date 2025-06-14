package com.api.diversity.infrastructure.controller;

import com.api.diversity.application.dto.RubroDto;
import com.api.diversity.application.service.IRubroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/rubros")
public class RubroController {

    @Autowired
    private IRubroService rubroService;

    @GetMapping
    public String listarRubros(Model model) {
        model.addAttribute("rubros", rubroService.findAll());
        return "rubros/lista";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("rubro", new RubroDto());
        return "rubros/form";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        model.addAttribute("rubro", rubroService.findById(id));
        return "rubros/form";
    }

    @PostMapping("/guardar")
    public String guardarRubro(@ModelAttribute RubroDto rubro, RedirectAttributes redirectAttributes) {
        try {
            rubroService.save(rubro);
            redirectAttributes.addFlashAttribute("mensaje", "Rubro guardado exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error al guardar el rubro: " + e.getMessage());
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