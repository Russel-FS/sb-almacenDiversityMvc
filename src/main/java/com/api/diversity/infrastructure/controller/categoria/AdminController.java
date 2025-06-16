package com.api.diversity.infrastructure.controller.categoria;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.api.diversity.application.dto.CategoriaDto;
import com.api.diversity.application.service.interfaces.ICategoriaService;
import com.api.diversity.application.service.interfaces.IRubroService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/admin/categorias")
@RequiredArgsConstructor
public class AdminController {
    private final ICategoriaService categoriaService;
    private final IRubroService rubroService;

    @GetMapping("")
    public String listarCategorias(Model model) {
        List<CategoriaDto> categorias = categoriaService.findAllIncludingInactive();
        model.addAttribute("categorias", categorias);
        return "categorias/lista"; 
    } 
  
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("categoria", new CategoriaDto());
        model.addAttribute("rubros", rubroService.findAll());
        model.addAttribute("esAdmin", true);
        return "categorias/form";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        CategoriaDto categoria = categoriaService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoría no encontrada"));
        model.addAttribute("categoria", categoria);
        model.addAttribute("rubros", rubroService.findAll());
        model.addAttribute("esAdmin", true);
        return "categorias/form";
    }

    @PostMapping("/guardar")
    public String guardarCategoria(@Valid CategoriaDto categoria, 
            BindingResult result, 
            Model model,
            RedirectAttributes flash) {
        if (result.hasErrors()) {
            model.addAttribute("rubros", rubroService.findAll());
            model.addAttribute("esAdmin", true);
            return "categorias/form";
        }

        categoriaService.save(categoria);
        flash.addFlashAttribute("mensaje", "Categoría guardada exitosamente.");
        flash.addFlashAttribute("tipoMensaje", "success");
        return "redirect:/admin/categorias";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarCategoria(@PathVariable Long id, RedirectAttributes flash) {
        categoriaService.deleteById(id);
        flash.addFlashAttribute("mensaje", "Categoría eliminada exitosamente.");
        flash.addFlashAttribute("tipoMensaje", "error");
        return "redirect:/admin/categorias";
    }
}