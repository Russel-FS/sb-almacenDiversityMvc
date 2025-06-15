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
            log.info("Iniciando búsqueda de usuario por email: {}", email);
            UsuarioDto usuario = usuarioService.findByEmail(email);

            if (usuario == null) {
                log.error("Usuario no encontrado con email: {}", email);
                throw new UsernameNotFoundException("Usuario no encontrado con email: " + email);
            }

            log.info("Usuario encontrado: {}", usuario.getEmail());
            log.info("Rol del usuario: {}", usuario.getRol().getNombreRol());
            log.info("Estado del usuario: {}", usuario.getEstado());
            log.info("Contraseña del usuario: {}", usuario.getContraseña());

            SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + usuario.getRol().getNombreRol());
            log.info("Autoridad creada: {}", authority.getAuthority());

            CustomUser customUser = new CustomUser(
                    usuario.getIdUsuario(),
                    usuario.getEmail(),
                    usuario.getContraseña(),
                    usuario.getNombreCompleto(),
                    Collections.singletonList(authority));

            log.info("CustomUser creado exitosamente para: {}", usuario.getEmail());
            return customUser;
        } catch (Exception e) {
            log.error("Error al cargar usuario por email: {}", email, e);
            throw e;
        }
    }
}