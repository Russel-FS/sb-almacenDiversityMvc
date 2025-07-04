package com.api.diversity.infrastructure.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class GlobalErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        // Obtener el código de estado del error
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object message = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
        Object exception = request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);

        log.error("Error global capturado - Status: {}, Message: {}, Exception: {}",
                status, message, exception);

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());

            switch (statusCode) {
                case 404:
                    return handleNotFound(model);
                case 403:
                    return handleAccessDenied(model);
                case 500:
                    return handleServerError(model);
                case 401:
                    return handleUnauthorized(model);
                default:
                    return handleGenericError(model, statusCode, message);
            }
        }

        // codigo generico de error en el servidor
        return handleGenericError(model, 500, "Error desconocido");
    }

    private String handleNotFound(Model model) {
        model.addAttribute("titulo", "Página No Encontrada");
        model.addAttribute("subtitulo", "La página que buscas no existe");
        model.addAttribute("codigoError", "404");
        model.addAttribute("mensaje", "La URL que intentaste acceder no existe en nuestro sistema.");
        model.addAttribute("tipoError", "not-found");
        return "error/not-found";
    }

    private String handleAccessDenied(Model model) {
        model.addAttribute("titulo", "Acceso Denegado");
        model.addAttribute("subtitulo", "No tienes permisos para acceder a esta página");
        model.addAttribute("codigoError", "403");
        model.addAttribute("mensaje",
                "Tu cuenta no tiene los permisos necesarios para acceder a esta sección del sistema.");
        model.addAttribute("tipoError", "access-denied");
        return "error/access-denied";
    }

    private String handleServerError(Model model) {
        model.addAttribute("titulo", "Error del Servidor");
        model.addAttribute("subtitulo", "Algo salió mal en nuestro sistema");
        model.addAttribute("codigoError", "500");
        model.addAttribute("mensaje",
                "Ha ocurrido un error interno en nuestro sistema. Por favor, intenta nuevamente más tarde.");
        model.addAttribute("tipoError", "server-error");
        return "error/server-error";
    }

    private String handleUnauthorized(Model model) {
        model.addAttribute("titulo", "No Autorizado");
        model.addAttribute("subtitulo", "Debes iniciar sesión para acceder");
        model.addAttribute("codigoError", "401");
        model.addAttribute("mensaje", "Necesitas iniciar sesión para acceder a esta sección del sistema.");
        model.addAttribute("tipoError", "unauthorized");
        return "error/access-denied";
    }

    private String handleGenericError(Model model, int statusCode, Object message) {
        model.addAttribute("titulo", "Error del Sistema");
        model.addAttribute("subtitulo", "Ha ocurrido un error inesperado");
        model.addAttribute("codigoError", String.valueOf(statusCode));
        model.addAttribute("mensaje", message != null ? message.toString() : "Error desconocido en el sistema.");
        model.addAttribute("tipoError", "generic-error");
        return "error/generic-error";
    }
}