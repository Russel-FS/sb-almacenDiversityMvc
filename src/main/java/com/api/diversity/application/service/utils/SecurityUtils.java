package com.api.diversity.application.service.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.api.diversity.application.dto.UsuarioDto;
import com.api.diversity.application.service.interfaces.IUsuarioService;
import com.api.diversity.infrastructure.security.CustomUser;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SecurityUtils {
    
    private final IUsuarioService usuarioService;
    
    /**
     * Obtiene la información del usuario actualmente autenticado.
     *
     * @return la información del usuario actual como un UsuarioDto si está autenticado,
     *         o null si el usuario no está autenticado o no tiene un principal CustomUser válido.
     */
    public UsuarioDto getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUser) {
            CustomUser customUser = (CustomUser) authentication.getPrincipal();
            return usuarioService.findById(customUser.getId());
        }
        return null;
    }
}
