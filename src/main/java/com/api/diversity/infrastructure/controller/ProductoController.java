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

import com.api.diversity.application.dto.ProductoDto;
import com.api.diversity.application.service.interfaces.ICategoriaService;
import com.api.diversity.application.service.interfaces.IProductoService;
import com.api.diversity.domain.enums.TipoRubro;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/productos")
@RequiredArgsConstructor
public class ProductoController {

    private final IProductoService productoService;
    private final ICategoriaService categoriaService;

    @GetMapping("")
    public String listarProductos(@RequestParam(required = false) TipoRubro rubro, Model model) {
        List<ProductoDto> productos = productoService.findAllByRubro(rubro);
        model.addAttribute("productos", productos); 
        return "productos/lista";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(@RequestParam TipoRubro rubro, Model model) {
        model.addAttribute("producto", new ProductoDto());
        model.addAttribute("categorias", categoriaService.findByRubro(rubro));
        return "productos/form";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id,
            @RequestParam TipoRubro rubro,
            Model model) {
        ProductoDto producto = productoService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado"));
        model.addAttribute("producto", producto);
        model.addAttribute("categorias", categoriaService.findByRubro(rubro));
        return "productos/form";
    }

    @PostMapping("/guardar")
    public String guardarProducto(@Valid ProductoDto producto,
            BindingResult result,
            @RequestParam(value = "imagen", required = false) MultipartFile imagen,
            @RequestParam TipoRubro rubro,
            Model model,
            RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute("categorias", categoriaService.findByRubro(rubro));
            if (result.hasErrors()) {
                model.addAttribute("mensaje", "Error en los datos del producto");
                model.addAttribute("tipoMensaje", "error");
                return "productos/form";
            }

            productoService.save(producto, imagen);
            redirectAttributes.addFlashAttribute("mensaje", "Producto guardado exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
            return "redirect:/productos?rubro=" + rubro;

        } catch (Exception e) {
            model.addAttribute("mensaje", "Error al guardar el producto: " + e.getMessage());
            model.addAttribute("tipoMensaje", "error");
            model.addAttribute("producto", producto);
            return "productos/form";
        }
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarProducto(@PathVariable Long id,
            @RequestParam TipoRubro rubro,
            RedirectAttributes flash) {
        productoService.deleteById(id);
        flash.addFlashAttribute("mensaje", "Producto eliminado exitosamente.");
        flash.addFlashAttribute("tipoMensaje", "error");
        return "redirect:/productos?rubro=" + rubro;
    }
}
