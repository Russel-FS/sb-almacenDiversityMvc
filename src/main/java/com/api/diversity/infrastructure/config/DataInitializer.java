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
        log.info("Iniciando la carga de datos iniciales...");

        // Crear roles si no existen
        for (TipoRol tipoRol : TipoRol.values()) {
            if (!rolRepository.findByNombreRol(tipoRol.getNombre()).isPresent()) {
                RolEntity rol = new RolEntity();
                rol.setNombreRol(tipoRol.getNombre());
                rol.setDescripcion(tipoRol.getDescripcion());
                rol.setEstado(EstadoRol.Activo);
                rolRepository.save(rol);
                log.info("Rol {} creado", tipoRol.getNombre());
            }
        }

        // Buscar el rol ADMINISTRADOR
        RolEntity rolAdmin = rolRepository.findByNombreRol(TipoRol.ADMINISTRADOR.getNombre())
                .orElseThrow(() -> new RuntimeException("Rol ADMINISTRADOR no encontrado"));
        log.info("Rol ADMINISTRADOR encontrado con ID: {}", rolAdmin.getIdRol());

        // Verificar si el usuario admin existe
        UsuarioDto usuarioExistente = usuarioService.findByEmail("admin@gmail.com");
        String password = "admin123";
        String encodedPassword = passwordEncoder.encode(password);

        if (usuarioExistente == null) {
            log.info("Creando usuario admin...");
            log.info("Contraseña original: {}", password);
            log.info("Contraseña encriptada: {}", encodedPassword);

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
            log.info("Usuario admin creado con éxito");
        } else {
            log.info("Usuario admin ya existe, actualizando contraseña...");
            usuarioExistente.setContraseña(encodedPassword);
            usuarioService.save(usuarioExistente);
            log.info("Contraseña actualizada");
        }

        // Verificar que la contraseña se guardó correctamente
        UsuarioDto usuarioVerificado = usuarioService.findByEmail("admin@gmail.com");
        if (usuarioVerificado != null) {
            log.info("Usuario verificado - Email: {}", usuarioVerificado.getEmail());
            log.info("Usuario verificado - Contraseña: {}", usuarioVerificado.getContraseña());

            boolean matches = passwordEncoder.matches(password, usuarioVerificado.getContraseña());
            log.info("Verificación de contraseña: {}", matches);

            if (!matches) {
                log.error("Error: La contraseña no coincide después de la actualización");
                throw new RuntimeException("No se pudo establecer la contraseña correctamente");
            }
        }
    }
}