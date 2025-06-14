package com.api.diversity.infrastructure.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.api.diversity.application.dto.UsuarioDto;
import com.api.diversity.application.service.interfaces.IUsuarioService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final IUsuarioService usuarioService;

    @GetMapping("/login")
    public String mostrarLogin() {
        return "auth/login";
    }

    @PostMapping("/login")
    public String procesarLogin(UsuarioDto usuarioDto, HttpSession session, RedirectAttributes redirectAttributes) {
        try {
            UsuarioDto usuario = usuarioService.findByEmailAndPassword(usuarioDto.getEmail(),
                    usuarioDto.getContraseña());
            if (usuario != null) {
                session.setAttribute("user", usuario);
                redirectAttributes.addFlashAttribute("mensaje", "Bienvenido " + usuario.getNombreCompleto());
                redirectAttributes.addFlashAttribute("tipoMensaje", "success");
                return "redirect:/";
            } else {
                redirectAttributes.addFlashAttribute("mensaje", "Credenciales inválidas");
                redirectAttributes.addFlashAttribute("tipoMensaje", "error");
                return "redirect:/auth/login";
            }
        } catch (Exception e) {
            log.error("Error en el login", e);
            redirectAttributes.addFlashAttribute("mensaje", "Error al iniciar sesión: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
            return "redirect:/auth/login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        session.invalidate();
        redirectAttributes.addFlashAttribute("mensaje", "Sesión cerrada exitosamente");
        redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        return "redirect:/auth/login";
    }
}