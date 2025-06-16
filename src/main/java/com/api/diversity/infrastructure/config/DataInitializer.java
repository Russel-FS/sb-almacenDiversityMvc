package com.api.diversity.infrastructure.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.api.diversity.application.dto.RolDto;
import com.api.diversity.application.dto.RubroDto;
import com.api.diversity.application.dto.UsuarioDto;
import com.api.diversity.application.service.interfaces.IRubroService;
import com.api.diversity.application.service.interfaces.IUsuarioService;
import com.api.diversity.domain.enums.EstadoRol;
import com.api.diversity.domain.enums.EstadoRubro;
import com.api.diversity.domain.enums.EstadoUsuario;
import com.api.diversity.domain.enums.TipoRol;
import com.api.diversity.domain.enums.TipoRubro;
import com.api.diversity.domain.model.RolEntity;
import com.api.diversity.domain.ports.IRolRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final IUsuarioService usuarioService;
    private final IRubroService rubroService;
    private final IRolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Metodo que se encarga de inicializar los datos por defecto, tales como roles
     * y rubros.
     * Ademas, crea un usuario administrador si es que no existe.
     * 
     * @param args argumentos que se reciben desde la linea de comandos
     * @throws Exception
     */
    @Override
    @Transactional
    public void run(String... args) throws Exception {
        try {
            // creacion de roles por defecto
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

            // creacion de rubros por defecto
            for (TipoRubro tipoRubro : TipoRubro.values()) {
                if (!rubroService.existsByNombreRubro(tipoRubro.getNombre())) {
                    rubroService.save(RubroDto.builder()
                            .nombreRubro(tipoRubro.getNombre())
                            .code(tipoRubro.getCode())
                            .descripcion(tipoRubro.getDescripcion())
                            .estado(EstadoRubro.Activo)
                            .build(), null);
                    log.info("Rubro {} creado exitosamente", tipoRubro.getNombre());
                }
            }

            // Buscar el rol ADMINISTRADOR
            RolEntity rolAdmin = rolRepository.findByNombreRol(TipoRol.ADMINISTRADOR.getNombre())
                    .orElseThrow(() -> new RuntimeException("Rol ADMINISTRADOR no encontrado"));

            // Buscar un rubro por defecto
            RubroDto rubroDefault = rubroService.findByNombreRubro(TipoRubro.SIN_RUBRO.getNombre())
                    .orElseThrow(() -> new RuntimeException("Rubro SIN_RUBRO no encontrado"));

            // Verificar si el usuario admin existe
            String email = "admin@gmail.com";
            String password = "admin123";

            if (!usuarioService.existsByEmail(email)) {
                UsuarioDto admin = new UsuarioDto();
                admin.setNombreUsuario("admin");
                admin.setEmail(email);
                admin.setNombreCompleto("Administrador del Sistema");
                admin.setContraseña(passwordEncoder.encode(password));
                admin.setRol(RolDto.builder()
                        .idRol(rolAdmin.getIdRol())
                        .nombreRol(rolAdmin.getNombreRol())
                        .build());
                admin.setEstado(EstadoUsuario.Activo);
                admin.setRubro(rubroDefault);
                usuarioService.save(admin);
                log.info("Usuario admin creado exitosamente");
            }
        } catch (Exception e) {
            log.error("Error durante la inicialización de datos: {}", e.getMessage());

        }
    }
}