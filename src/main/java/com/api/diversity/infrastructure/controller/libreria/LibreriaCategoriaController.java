package com.api.diversity.infrastructure.controller.libreria;

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
import com.api.diversity.application.dto.RubroDto;
import com.api.diversity.application.service.interfaces.ICategoriaService;
import com.api.diversity.application.service.interfaces.IRubroService;
import com.api.diversity.domain.enums.TipoRubro;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/libreria/categorias")
@RequiredArgsConstructor
public class LibreriaCategoriaController {
    private final ICategoriaService categoriaService;
    private final IRubroService rubroService;

    @GetMapping("")
    public String listarCategorias(Model model) {
        List<CategoriaDto> categorias = categoriaService.findByRubro(TipoRubro.LIBRERIA);
        model.addAttribute("categorias", categorias);
        model.addAttribute("rubroActual", TipoRubro.LIBRERIA);
        return "libreria/categorias/lista";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        CategoriaDto categoria = new CategoriaDto();
        RubroDto rubroDto = rubroService.findByNombreRubro(TipoRubro.LIBRERIA.getNombre())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Rubro no encontrado"));

        categoria.setRubro(rubroDto);
        model.addAttribute("categoria", categoria);
        model.addAttribute("rubroActual", TipoRubro.LIBRERIA);
        return "libreria/categorias/form";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        CategoriaDto categoria = categoriaService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoría no encontrada"));

        // Verificar que la categoría pertenece a este rubro
        if (!categoria.getRubro().getCode().equals(TipoRubro.LIBRERIA.getCode())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "La categoría no pertenece a este rubro");
        }

        model.addAttribute("categoria", categoria);
        model.addAttribute("rubroActual", TipoRubro.LIBRERIA);
        return "libreria/categorias/form";
    }

    @PostMapping("/guardar")
    public String guardarCategoria(@Valid CategoriaDto categoria,
            BindingResult result,
            Model model,
            RedirectAttributes flash) {

        if (categoria.getRubro() == null || categoria.getRubro().getCode() == null) {
            RubroDto rubroDto = rubroService.findByNombreRubro(TipoRubro.LIBRERIA.getNombre())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Rubro no encontrado"));
            categoria.setRubro(rubroDto);
        }
        if (result.hasErrors()) {
            model.addAttribute("rubroActual", TipoRubro.LIBRERIA);
            return "libreria/categorias/form";
        }

        // Verificar que el rubro de la categoría es correcto
        if (!categoria.getRubro().getCode().equals(TipoRubro.LIBRERIA.getCode())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "La categoría debe pertenecer al rubro de Librería");
        }

        try {
            categoriaService.save(categoria);
            flash.addFlashAttribute("mensaje", "Categoría guardada exitosamente.");
            flash.addFlashAttribute("tipoMensaje", "success");
            return "redirect:/libreria/categorias";
        } catch (RuntimeException e) {
            model.addAttribute("rubroActual", TipoRubro.LIBRERIA);
            model.addAttribute("categoria", categoria);
            model.addAttribute("mensaje", e.getMessage());
            model.addAttribute("tipoMensaje", "error");
            return "libreria/categorias/form";
        }
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarCategoria(@PathVariable Long id, RedirectAttributes flash) {
        CategoriaDto categoria = categoriaService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoría no encontrada"));

        // Verificar que la categoría pertenece a este rubro
        if (!categoria.getRubro().getCode().equals(TipoRubro.LIBRERIA.getCode())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "La categoría no pertenece a este rubro");
        }

        categoriaService.deleteById(id);
        flash.addFlashAttribute("mensaje", "Categoría eliminada exitosamente.");
        flash.addFlashAttribute("tipoMensaje", "error");
        return "redirect:/libreria/categorias";
    }
}
