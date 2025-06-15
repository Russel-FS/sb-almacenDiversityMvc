package com.api.diversity.infrastructure.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.api.diversity.application.dto.RolDto;
import com.api.diversity.application.dto.UsuarioDto;
import com.api.diversity.application.service.interfaces.IUsuarioService;
import com.api.diversity.domain.enums.EstadoRol;
import com.api.diversity.domain.enums.EstadoUsuario;
import com.api.diversity.domain.enums.TipoRol;
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
    @Transactional
    public void run(String... args) throws Exception {
        try {
            // Crear roles si no existen
            for (TipoRol tipoRol : TipoRol.values()) {
                if (!rolRepository.findByNombreRol(tipoRol.getNombre()).isPresent()) {
                    RolEntity rol = new RolEntity();
                    rol.setNombreRol(tipoRol.getNombre());
                    rol.setDescripcion(tipoRol.getDescripcion());
                    rol.setEstado(EstadoRol.Activo);
                    rolRepository.save(rol);
                    log.info("Rol {} creado exitosamente", tipoRol.getNombre());
                }
            }

            // Buscar el rol ADMINISTRADOR
            RolEntity rolAdmin = rolRepository.findByNombreRol(TipoRol.ADMINISTRADOR.getNombre())
                    .orElseThrow(() -> new RuntimeException("Rol ADMINISTRADOR no encontrado"));

            // Verificar si el usuario admin existe
            UsuarioDto usuarioExistente = usuarioService.findByEmail("admin@gmail.com");
            String password = "admin123";
            String encodedPassword = passwordEncoder.encode(password);

            if (usuarioExistente == null) {
                UsuarioDto admin = new UsuarioDto();
                admin.setNombreUsuario("admin");
                admin.setEmail("admin@gmail.com");
                admin.setNombreCompleto("Administrador del Sistema");
                admin.setContraseña(encodedPassword);
                admin.setRol(RolDto.builder()
                        .idRol(rolAdmin.getIdRol())
                        .nombreRol(rolAdmin.getNombreRol())
                        .build());
                admin.setEstado(EstadoUsuario.Activo);

                usuarioService.save(admin);
                log.info("Usuario admin creado exitosamente");
            }
        } catch (Exception e) {
            log.error("Error durante la inicialización de datos: {}", e.getMessage());

        }
    }
}