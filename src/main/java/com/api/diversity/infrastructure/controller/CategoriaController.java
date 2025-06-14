package com.api.diversity.infrastructure.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @GetMapping
    public String listarCategorias(Model model) {
        model.addAttribute("categorias", categoriaService.findAll());
        return "categorias/lista";
    }

    @GetMapping("/nueva")
    public String mostrarFormularioNueva(Model model) {
        model.addAttribute("categoria", new Categoria());
        return "categorias/form";
    }

    @PostMapping("/guardar")
    public String guardarCategoria(@Valid Categoria categoria, BindingResult result, RedirectAttributes flash) {
        if (result.hasErrors()) {
            return "categorias/form";
        }

        boolean esNueva = (categoria.getIdCategoria() == null);
        if (esNueva && categoriaService.existsByNombreCategoria(categoria.getNombreCategoria())) {
            result.rejectValue("nombreCategoria", "error.categoria", "Ya existe una categoría con este nombre");
            return "categorias/form";
        }

        categoriaService.save(categoria);
        flash.addFlashAttribute("mensaje", esNueva ? "Categoría creada con éxito" : "Categoría actualizada con éxito");
        return "redirect:/categorias";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model, RedirectAttributes flash) {
        var categoriaOpt = categoriaService.findById(id);
        if (categoriaOpt.isEmpty()) {
            flash.addFlashAttribute("error", "La categoría no existe");
            return "redirect:/categorias";
        }
        model.addAttribute("categoria", categoriaOpt.get());
        return "categorias/form";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarCategoria(@PathVariable Long id, RedirectAttributes flash) {
        if (!categoriaService.existsById(id)) {
            flash.addFlashAttribute("error", "La categoría no existe");
            return "redirect:/categorias";
        }

        try {
            categoriaService.deleteById(id);
            flash.addFlashAttribute("mensaje", "Categoría eliminada con éxito");
        } catch (Exception e) {
            flash.addFlashAttribute("error", "No se puede eliminar la categoría porque tiene productos asociados");
        }
        return "redirect:/categorias";
    }

    @GetMapping("/buscar/{id}")
    public Categoria buscarCategoria(@PathVariable Long id) {
        return categoriaService.findById(id).orElse(null);
    }

    @PostMapping("/crear")
    public Categoria crearCategoria(@RequestBody Categoria categoria) {
        categoriaService.save(categoria);
        return categoria;
    }

    @PutMapping("/actualizar/{id}")
    public Categoria actualizarCategoria(@PathVariable Long id, @RequestBody Categoria categoria) {
        Categoria categoriaActual = buscarCategoria(id);
        if (categoriaActual != null) {
            categoriaActual.setNombreCategoria(categoria.getNombreCategoria());
            categoriaActual.setDescripcion(categoria.getDescripcion());
        }
        return categoriaActual;
    }

    @DeleteMapping("/eliminar/{id}")
    public void eliminarCategoria(@PathVariable Long id) {
        categoriaService.deleteById(id);
    }
}