package com.api.diversity.infrastructure.config;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.api.diversity.application.dto.RubroDto;
import com.api.diversity.application.dto.UsuarioDto;
import com.api.diversity.application.service.interfaces.IRubroService;
import com.api.diversity.application.service.interfaces.IUsuarioService;
import com.api.diversity.domain.enums.EstadoRol;
import com.api.diversity.domain.enums.EstadoRubro;
import com.api.diversity.domain.enums.EstadoUsuario;
import com.api.diversity.domain.enums.EstadoUserRole;
import com.api.diversity.domain.enums.EstadoUsuarioRubro;
import com.api.diversity.domain.enums.TipoRol;
import com.api.diversity.domain.enums.TipoRubro;
import com.api.diversity.domain.model.RolEntity;
import com.api.diversity.domain.model.RubroEntity;
import com.api.diversity.domain.model.UserRoleEntity;
import com.api.diversity.domain.model.UsuarioEntity;
import com.api.diversity.domain.model.UsuarioRubroEntity;
import com.api.diversity.domain.ports.IRolRepository;
import com.api.diversity.domain.ports.RubroRepository;
import com.api.diversity.domain.ports.IUserRoleRepository;
import com.api.diversity.domain.ports.IUsuarioRepository;
import com.api.diversity.domain.ports.IUsuarioRubroRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final IUsuarioService usuarioService;
    private final IRubroService rubroService;
    private final IRolRepository rolRepository;
    private final RubroRepository rubroRepository;
    private final IUserRoleRepository userRoleRepository;
    private final IUsuarioRubroRepository usuarioRubroRepository;
    private final IUsuarioRepository usuarioRepository;
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

            // rol administrador por defecto
            RolEntity rolAdmin = rolRepository.findByNombreRol(TipoRol.ADMINISTRADOR.getNombre())
                    .orElseThrow(() -> new RuntimeException("Rol ADMINISTRADOR no encontrado"));

            // Buscar todos los rubros para asignar al admin
            List<RubroEntity> rubros = rubroRepository.findAll();

            // Verificar si el usuario admin existe
            String email = "admin@gmail.com";
            String password = "admin123";

            if (!usuarioService.existsByEmail(email)) {
                // Crear usuario administrador
                UsuarioDto admin = new UsuarioDto();
                admin.setNombreUsuario("admin");
                admin.setEmail(email);
                admin.setNombreCompleto("Administrador del Sistema");
                admin.setContraseña(passwordEncoder.encode(password));
                admin.setEstado(EstadoUsuario.Activo);
                UsuarioDto savedAdmin = usuarioService.save(admin);

                UsuarioEntity usuarioEntity = usuarioRepository.findById(savedAdmin.getIdUsuario())
                        .orElseThrow(() -> new RuntimeException("Usuario no encontrado después de guardar"));

                // asignacion de roles
                UserRoleEntity userRoleEntity = new UserRoleEntity();
                userRoleEntity.setUsuario(usuarioEntity);
                userRoleEntity.setRol(rolAdmin);
                userRoleEntity.setEstado(EstadoUserRole.Activo);
                userRoleRepository.save(userRoleEntity);

                // asignacion de rubros al admin
                for (RubroEntity rubro : rubros) {
                    UsuarioRubroEntity usuarioRubroEntity = new UsuarioRubroEntity();
                    usuarioRubroEntity.setUsuario(usuarioEntity);
                    usuarioRubroEntity.setRubro(rubro);
                    usuarioRubroEntity.setEstado(EstadoUsuarioRubro.Activo);
                    usuarioRubroRepository.save(usuarioRubroEntity);
                }

                log.info("Usuario admin creado exitosamente con rol ADMINISTRADOR y acceso a todos los rubros");
            }
        } catch (Exception e) {
            log.error("Error durante la inicialización de datos: {}", e.getMessage());
        }
    }
}