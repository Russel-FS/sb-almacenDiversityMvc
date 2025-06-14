package com.api.diversity.infrastructure.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.api.diversity.application.dto.UsuarioDto;
import com.api.diversity.application.service.interfaces.IUsuarioService;
import com.api.diversity.domain.enums.EstadoRol;
import com.api.diversity.domain.model.RolEntity;
import com.api.diversity.domain.ports.IRolRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final IUsuarioService usuarioService;
    private final IRolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (!rolRepository.findById(1L).isPresent()) {
            RolEntity rolAdmin = new RolEntity();
            rolAdmin.setIdRol(1L);
            rolAdmin.setNombreRol("Administrador");
            rolAdmin.setDescripcion("Acceso total al sistema");
            rolAdmin.setEstado(EstadoRol.Activo);
            rolRepository.save(rolAdmin);
            log.info("Rol ADMINISTRADOR creado");
        }

        if (!usuarioService.existsByEmail("admin@gmail.com")) {
            UsuarioDto admin = new UsuarioDto();
            admin.setNombreUsuario("admin");
            admin.setEmail("admin@gmail.com");
            admin.setNombreCompleto("Administrador del Sistema");
            admin.setContraseña("admin123");
            admin.setIdRol(1L);
            admin.setActivo(true);

            usuarioService.save(admin);
            log.info("Usuario admin creado con éxito");
        }
    }
}