package com.example.avance3.controller;

import com.example.avance3.model.DetalleEntrada;
import com.example.avance3.services.Detalle_EntradaService;
import com.example.avance3.services.EntradaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class Detalle_EntradaController {

    @Autowired
    private Detalle_EntradaService detalleEntradaService;

    @Autowired
    private EntradaService entradaService;

    // Mostrar lista
    @GetMapping("/detalles")
    public String listarDetalles(Model model) {
        model.addAttribute("listDetalles", detalleEntradaService.getAllDetalles());
        return "detalle/index";
    }

    // Mostrar formulario de registro
    @GetMapping("/formregistrarDetalle")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("detalleEntrada", new DetalleEntrada());
        model.addAttribute("entradas", entradaService.getAllEntradas());
        return "detalle/registrar";
    }

    // Guardar detalle (ruta corregida para que coincida con el formulario)
    @PostMapping("/detalleEntrada/guardar")
public String guardarDetalle(@ModelAttribute("detalleEntrada") DetalleEntrada detalleEntrada,
                             @RequestParam("entrada.id") Long entradaId) {

    detalleEntrada.setEntrada(entradaService.getEntradaById(entradaId));
    detalleEntradaService.saveDetalle(detalleEntrada);
    return "redirect:/detalles";
}

    // Formulario para actualizar
    @GetMapping("/formactualizarDetalle/{id}")
    public String mostrarFormularioActualizar(@PathVariable("id") Long id, Model model) {
        DetalleEntrada detalle = detalleEntradaService.getDetalleById(id);
        model.addAttribute("detalleEntrada", detalle);
        model.addAttribute("entradas", entradaService.getAllEntradas());
        return "detalle/actualizar";
    }

    // Eliminar
    @GetMapping("/deleteDetalle/{id}")
    public String eliminarDetalle(@PathVariable("id") Long id) {
        detalleEntradaService.deleteDetalleById(id);
        return "redirect:/detalles";
    }
}
