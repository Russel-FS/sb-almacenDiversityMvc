package com.api.diversity.infrastructure.security;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.api.diversity.application.dto.UsuarioDto;
import com.api.diversity.application.service.interfaces.IUsuarioService;
import com.api.diversity.domain.enums.EstadoUsuario;
import com.api.diversity.domain.enums.EstadoUserRole;
import com.api.diversity.domain.enums.EstadoRol;

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

            if (usuario.getEstado() != EstadoUsuario.Activo) {
                throw new UsernameNotFoundException("Usuario no está activo");
            }

            if (usuario.getContraseña() == null || usuario.getContraseña().trim().isEmpty()) {
                throw new UsernameNotFoundException("Error en la contraseña del usuario");
            }

            // roles de usuario
            List<SimpleGrantedAuthority> authorities = usuario.getRoles().stream()
                    .filter(userRole -> userRole.getEstado() == EstadoUserRole.Activo)
                    .filter(userRole -> userRole.getRol() != null &&
                            userRole.getRol().getEstado() == EstadoRol.Activo)
                    .map(userRole -> new SimpleGrantedAuthority(
                            "ROLE_" + userRole.getRol().getNombreRol().toUpperCase()))
                    .collect(Collectors.toList());

            // Log para debugging
            log.info("Usuario {} - Roles cargados: {}", email, authorities.stream()
                    .map(auth -> auth.getAuthority())
                    .collect(Collectors.joining(", ")));

            // Si no hay roles activos, asignar un rol por defecto
            if (authorities.isEmpty()) {
                authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
                log.warn("Usuario {} no tiene roles activos, asignando ROLE_USER por defectooo", email);
            }

            return new CustomUser(
                    usuario.getIdUsuario(),
                    usuario.getEmail(),
                    usuario.getContraseña(),
                    usuario.getNombreCompleto(),
                    authorities);
        } catch (UsernameNotFoundException e) {
            log.error("Error al cargar usuario por email: {}", email, e);
            throw e;
        }
    }
}