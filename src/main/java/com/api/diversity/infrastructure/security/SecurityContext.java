package com.api.diversity.infrastructure.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.api.diversity.application.dto.UsuarioDto;
import com.api.diversity.application.service.interfaces.IUsuarioService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SecurityContext {
    
    private final IUsuarioService usuarioService;
    
    /**
     * Obtiene el usuario completo desde la base de datos usando el ID del usuario autenticado.
     *
     * @return UsuarioDto con toda la información de la base de datos del usuario actual,
     *         o null si el usuario no está autenticado.
     */
    public UsuarioDto getCurrentUserDatabase() {
        CustomUser customUser = getCurrentUser();
        if (customUser != null) {
            return usuarioService.findById(customUser.getId());
        }
        return null;
    }

    /**
     * Obtiene el ID del usuario actualmente autenticado.
     *
     * @return Long con el ID del usuario actual,
     *         o null si el usuario no está autenticado.
     */
    public Long getCurrentUserId() {
        CustomUser customUser = getCurrentUser();
        return customUser != null ? customUser.getId() : null;
    }

    /**
     * Obtiene el objeto CustomUser del contexto de seguridad.
     *
     * @return CustomUser con la información básica del usuario autenticado,
     *         o null si el usuario no está autenticado o no es un CustomUser.
     */
    public CustomUser getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUser) {
            return (CustomUser) authentication.getPrincipal();
        }
        return null;
    }
}
