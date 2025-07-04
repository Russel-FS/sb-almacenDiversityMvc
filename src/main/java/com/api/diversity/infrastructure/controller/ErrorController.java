package com.api.diversity.infrastructure.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/error")
@Slf4j
public class ErrorController {

    @GetMapping("/access-denied")
    public String accessDenied(Model model, @RequestParam(required = false) String message) {
        log.warn("Acceso denegado - Usuario intentó acceder a una ruta no autorizada");

        model.addAttribute("titulo", "Acceso Denegado");
        model.addAttribute("subtitulo", "No tienes permisos para acceder a esta página");
        model.addAttribute("codigoError", "403");
        model.addAttribute("mensaje", message != null ? message
                : "Tu cuenta no tiene los permisos necesarios para acceder a esta sección del sistema.");
        model.addAttribute("tipoError", "access-denied");

        return "error/access-denied";
    }

    @GetMapping("/not-found")
    public String notFound(Model model) {
        log.warn("Página no encontrada - Usuario intentó acceder a una ruta inexistente");

        model.addAttribute("titulo", "Página No Encontrada");
        model.addAttribute("subtitulo", "La página que buscas no existe");
        model.addAttribute("codigoError", "404");
        model.addAttribute("mensaje", "La URL que intentaste acceder no existe en nuestro sistema.");
        model.addAttribute("tipoError", "not-found");

        return "error/not-found";
    }

    @GetMapping("/server-error")
    public String serverError(Model model) {
        log.error("Error interno del servidor");

        model.addAttribute("titulo", "Error del Servidor");
        model.addAttribute("subtitulo", "Algo salió mal en nuestro sistema");
        model.addAttribute("codigoError", "500");
        model.addAttribute("mensaje",
                "Ha ocurrido un error interno en nuestro sistema. Por favor, intenta nuevamente más tarde.");
        model.addAttribute("tipoError", "server-error");

        return "error/server-error";
    }
}