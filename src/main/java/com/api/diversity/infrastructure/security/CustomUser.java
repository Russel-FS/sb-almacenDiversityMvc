package com.api.diversity.infrastructure.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import lombok.Getter;

@Getter
public class CustomUser extends User {
    private final Long idUsuario;
    private final String nombreCompleto;

    public CustomUser(Long idUsuario, String username, String password, String nombreCompleto,
            Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.idUsuario = idUsuario;
        this.nombreCompleto = nombreCompleto;
    }
}