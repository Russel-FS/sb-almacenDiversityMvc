package com.example.avance3.controller;

import com.example.avance3.model.Entradas;
import com.example.avance3.services.EntradaService;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class EntradasController {

    @Autowired
    private EntradaService entradaService;

    @GetMapping("/")
    public String viewEntradas(Model model) {
        model.addAttribute("listEntradas", entradaService.getAllEntradas());
        return "index";
    }

    @GetMapping("/formregistrarEntrada")
    public String entradaFormulario(Model model) {
        Entradas entrada = new Entradas();
        model.addAttribute("entrada", entrada);
        return "registrar";
    }

    @PostMapping("/saveEntrada")
public String registrarEntrada(@ModelAttribute("entrada") Entradas entrada) {
    if (entrada.getId() == null) {
        entrada.setFechaEntrada(LocalDateTime.now()); 
    }
    entradaService.saveEntrada(entrada);
    return "redirect:/";
}

    @GetMapping("/formactualizarEntrada/{id}")
    public String entradaFormularioActualizar(@PathVariable("id") long id, Model model) {
        Entradas entrada = entradaService.getEntradaById(id);
        model.addAttribute("entrada", entrada);
        return "actualizar";
    }

    @GetMapping("/deleteEntrada/{id}")
    public String deleteEntrada(@PathVariable("id") long id) {
        entradaService.deleteEntradaById(id);
        return "redirect:/";
    }
}
