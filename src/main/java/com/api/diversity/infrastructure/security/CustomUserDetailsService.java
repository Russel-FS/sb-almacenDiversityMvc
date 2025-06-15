package com.api.diversity.infrastructure.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.api.diversity.application.dto.RolDto;
import com.api.diversity.application.dto.UsuarioDto;
import com.api.diversity.application.service.interfaces.IRolService;
import com.api.diversity.application.service.interfaces.IUsuarioService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final IUsuarioService usuarioService;
    private final IRolService rolService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UsuarioDto usuario = usuarioService.findByEmail(email);
        System.out.println("Buscando usuario por email: " + email);
        if (usuario == null) {
            throw new UsernameNotFoundException("Usuario no encontrado: " + email);
        }

        System.out.println("Usuario encontrado: " + usuario.getEmail());
        // Obtener el rol del usuario
        RolDto rol = rolService.findById(usuario.getRol().getIdRol());

        // crear la lista de autoridades
        if (rol == null) {
            throw new UsernameNotFoundException("Rol no encontrado para el usuario: " + email);
        }
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + rol.getNombreRol().toUpperCase()));

        return new CustomUser(
                usuario.getIdUsuario(),
                usuario.getEmail(),
                usuario.getContraseña(),
                usuario.getNombreCompleto(),
                authorities);
    }
}