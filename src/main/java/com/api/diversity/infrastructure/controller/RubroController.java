package com.api.diversity.infrastructure.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.api.diversity.application.dto.RubroDto;
import com.api.diversity.application.service.interfaces.IRubroService;
import com.api.diversity.domain.enums.EstadoRubro;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/rubros")
@Slf4j
public class RubroController {

    @Autowired
    private IRubroService rubroService;

    @GetMapping
    public String listarRubros(Model model) {
        model.addAttribute("rubros", rubroService.findAll());
        return "rubros/lista";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevoRubro(Model model) {
        RubroDto rubro = new RubroDto();
        rubro.setEstado(EstadoRubro.Activo);
        rubro.setCreatedBy(1L);
        model.addAttribute("rubro", rubro);
        return "rubros/form";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditarRubro(@PathVariable Long id, Model model) {
        RubroDto rubro = rubroService.findById(id);
        model.addAttribute("rubro", rubro);
        return "rubros/form";
    }

    @PostMapping("/guardar")
    public String guardarRubro(@ModelAttribute @Valid RubroDto rubro, BindingResult result,
            @RequestParam("imagen") MultipartFile imagen,
            RedirectAttributes redirectAttributes) {
        try {
            if (result.hasErrors()) {
                redirectAttributes.addFlashAttribute("mensaje", "Error en los datos del rubro");
                redirectAttributes.addFlashAttribute("tipoMensaje", "error");
                return "rubros/form";
            }
            rubroService.save(rubro, imagen);
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
            log.error("Error al eliminar el rubro", e);
            redirectAttributes.addFlashAttribute("mensaje", "Error al eliminar el rubro: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        }
        return "redirect:/rubros";
    }
}