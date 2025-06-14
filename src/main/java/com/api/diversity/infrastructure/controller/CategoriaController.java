package com.api.diversity.infrastructure.controller;

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

import com.api.diversity.application.interfaces.ICategoriaService;
import com.api.diversity.domain.model.Categoria;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/categorias")
@RequiredArgsConstructor
public class CategoriaController {

    private final ICategoriaService categoriaService;

    @GetMapping("")
    public String listarCategorias(Model model) {
        List<Categoria> categorias = categoriaService.findAll();
        model.addAttribute("categorias", categorias);
        return "categorias/lista";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("categoria", new Categoria());
        return "categorias/form";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        Categoria categoria = categoriaService.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoría no encontrada"));
        model.addAttribute("categoria", categoria);
        return "categorias/form";
    }    @PostMapping("/guardar")
    public String guardarCategoria(@Valid Categoria categoria, BindingResult result, RedirectAttributes flash) {
        if (result.hasErrors()) {
            return "categorias/form";
        } 
        categoriaService.save(categoria);
        flash.addFlashAttribute("mensaje", "Categoría guardada exitosamente.");
        flash.addFlashAttribute("tipoMensaje", "success");
        return "redirect:/categorias";
    }    @GetMapping("/eliminar/{id}")
    public String eliminarCategoria(@PathVariable Long id, RedirectAttributes flash) {
        categoriaService.deleteById(id);
        flash.addFlashAttribute("mensaje", "Categoría eliminada exitosamente.");
        flash.addFlashAttribute("tipoMensaje", "error");
        return "redirect:/categorias";
    }
}