package com.api.diversity.infrastructure.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.api.diversity.domain.model.Usuario;
import com.api.diversity.application.service.interfaces.UsuarioService;

@Controller
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/")
    public String verPaginaDeInicio(Model model) {
        model.addAttribute("listaUsuarios", usuarioService.getAllUsuarios());
        return "index";
    }

    @GetMapping("/mostrarFormularioDeRegistro")
    public String mostrarFormularioDeRegistro(Model model) {
        Usuario usuario = new Usuario();
        model.addAttribute("usuario", usuario);
        return "registrar";
    }

    @PostMapping("/guardarUsuario")
    public String guardarUsuario(@ModelAttribute("usuario") Usuario usuario) {
        usuarioService.saveUsuario(usuario);
        return "redirect:/";
    }

    @GetMapping("/mostrarFormularioDeActualizacion/{id}")
    public String mostrarFormularioDeActualizacion(@PathVariable(value = "id") Integer id, Model model) {
        Usuario usuario = usuarioService.getUsuarioById(id);
        model.addAttribute("usuario", usuario);
        return "actualizar";
    }

    @GetMapping("/eliminarUsuario/{id}")
    public String eliminarUsuario(@PathVariable(value = "id") Integer id) { 
        this.usuarioService.deleteUsuarioById(id);
        return "redirect:/";
    }
}