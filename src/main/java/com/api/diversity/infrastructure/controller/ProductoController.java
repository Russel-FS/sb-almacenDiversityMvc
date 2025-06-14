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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.api.diversity.application.dto.Producto;
import com.api.diversity.application.service.interfaces.ICategoriaService;
import com.api.diversity.application.service.interfaces.IProductoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/productos")
@RequiredArgsConstructor
public class ProductoController {

    private final IProductoService productoService;
    private final ICategoriaService categoriaService;

    @GetMapping("")
    public String listarProductos(Model model) {
        List<Producto> productos = productoService.findAll();
        model.addAttribute("productos", productos);
        return "productos/lista";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("producto", new Producto());
        model.addAttribute("categorias", categoriaService.findAll());
        return "productos/form";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        Producto producto = productoService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado"));
        model.addAttribute("producto", producto);
        model.addAttribute("categorias", categoriaService.findAll());
        return "productos/form";
    }

    @PostMapping("/guardar")
    public String guardarProducto(@Valid Producto producto,
            BindingResult result,
            @RequestParam("imagen") MultipartFile imagen,
            RedirectAttributes flash,
            Model model) {
        if (result.hasErrors()) {
            model.addAttribute("categorias", categoriaService.findAll());
            return "productos/form";
        }

        try {
            productoService.save(producto, imagen);
            flash.addFlashAttribute("mensaje", "Producto guardado exitosamente.");
            flash.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            flash.addFlashAttribute("mensaje", "Error al guardar el producto: " + e.getMessage());
            flash.addFlashAttribute("tipoMensaje", "error");
            model.addAttribute("categorias", categoriaService.findAll());
            return "productos/form";
        }

        return "redirect:/productos";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarProducto(@PathVariable Long id, RedirectAttributes flash) {
        productoService.deleteById(id);
        flash.addFlashAttribute("mensaje", "Producto eliminado exitosamente.");
        flash.addFlashAttribute("tipoMensaje", "error");
        return "redirect:/productos";
    }
}
