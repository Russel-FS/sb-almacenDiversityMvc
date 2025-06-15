package com.api.diversity.infrastructure.security;

import java.util.Collections;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.api.diversity.application.dto.UsuarioDto;
import com.api.diversity.application.service.interfaces.IUsuarioService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final IUsuarioService usuarioService;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            UsuarioDto usuario = usuarioService.findByEmail(email);

            if (usuario == null) {
                throw new UsernameNotFoundException("Usuario no encontrado con email: " + email);
            }

            if (!"Activo".equalsIgnoreCase(usuario.getEstado().toString())) {
                throw new UsernameNotFoundException("Usuario no está activo");
            }

            if (usuario.getContraseña() == null || usuario.getContraseña().trim().isEmpty()) {
                throw new UsernameNotFoundException("Error en la contraseña del usuario");
            }

            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(
                    "ROLE_" + usuario.getRol().getNombreRol().toUpperCase());

            return new CustomUser(
                    usuario.getIdUsuario(),
                    usuario.getEmail(),
                    usuario.getContraseña(),
                    usuario.getNombreCompleto(),
                    Collections.singletonList(authority));
        } catch (Exception e) {
            log.error("Error al cargar usuario por email: {}", email, e);
            throw e;
        }
    }
}