package com.api.diversity.infrastructure.controller.admin;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.api.diversity.application.dto.RolDto;
import com.api.diversity.application.dto.UserRoleDto;
import com.api.diversity.application.dto.UsuarioDto;
import com.api.diversity.application.mappers.RolMapper;
import com.api.diversity.application.service.interfaces.IRubroService;
import com.api.diversity.application.service.interfaces.IUsuarioService;
import com.api.diversity.domain.enums.EstadoUsuario;
import com.api.diversity.infrastructure.repository.IRolJpaRepository;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/admin/usuarios")
@RequiredArgsConstructor
@Slf4j
public class AdminUsuarioController {

    private final IUsuarioService usuarioService;
    private final IRolJpaRepository rolJpaRepository;
    private final IRubroService rubroService;
    private final RolMapper rolMapper;

    @GetMapping("")
    public String listarUsuarios(
            @RequestParam(required = false) String busqueda,
            @RequestParam(required = false) EstadoUsuario estado,
            Model model) {

        log.info("Listando usuarios (ADMIN) - búsqueda: {}, estado: {}", busqueda, estado);

        try {
            List<UsuarioDto> usuarios = usuarioService.findAll();

            // filtro de busqueda por nombre
            if (busqueda != null && !busqueda.trim().isEmpty()) {
                usuarios = usuarios.stream()
                        .filter(u -> u.getNombreCompleto().toLowerCase().contains(busqueda.toLowerCase()) ||
                                u.getEmail().toLowerCase().contains(busqueda.toLowerCase()) ||
                                u.getNombreUsuario().toLowerCase().contains(busqueda.toLowerCase()))
                        .toList();
            }

            // Filtrar por estado si se proporciona
            if (estado != null) {
                usuarios = usuarios.stream()
                        .filter(u -> u.getEstado() == estado)
                        .toList();
            }

            // estadistica para admin
            Long totalUsuarios = (long) usuarioService.findAll().size();
            Long usuariosActivos = usuarioService.findAll().stream()
                    .filter(u -> u.getEstado() == EstadoUsuario.Activo).count();
            Long usuariosInactivos = usuarioService.findAll().stream()
                    .filter(u -> u.getEstado() == EstadoUsuario.Inactivo).count();
            Long usuariosBloqueados = usuarioService.findAll().stream()
                    .filter(u -> u.getEstado() == EstadoUsuario.Bloqueado).count();

            model.addAttribute("titulo", "Gestión de Usuarios");
            model.addAttribute("subtitulo", "Administración de usuarios del sistema");
            model.addAttribute("usuarios", usuarios);
            model.addAttribute("totalUsuarios", totalUsuarios);
            model.addAttribute("usuariosActivos", usuariosActivos);
            model.addAttribute("usuariosInactivos", usuariosInactivos);
            model.addAttribute("usuariosBloqueados", usuariosBloqueados);
            model.addAttribute("estados", EstadoUsuario.values());
            model.addAttribute("busqueda", busqueda);
            model.addAttribute("estadoFiltro", estado);

        } catch (Exception e) {
            log.error("Error al listar usuarios (ADMIN): {}", e.getMessage(), e);
            model.addAttribute("error", "Error al cargar los usuarios: " + e.getMessage());
            model.addAttribute("usuarios", List.of());
        }

        return "admin/usuarios/lista";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        log.info("Mostrando formulario de nuevo usuario");

        try {
            List<RolDto> roles = rolJpaRepository.findAll().stream()
                    .map(rolMapper::toDto)
                    .collect(Collectors.toList());

            model.addAttribute("titulo", "Nuevo Usuario");
            model.addAttribute("subtitulo", "Registrar nuevo usuario del sistema");
            model.addAttribute("usuario", new UsuarioDto());
            model.addAttribute("roles", roles);
            model.addAttribute("rubros", rubroService.findAll());
            model.addAttribute("estados", EstadoUsuario.values());
            model.addAttribute("esNuevo", true);

        } catch (Exception e) {
            log.error("Error al cargar formulario de nuevo usuario: {}", e.getMessage(), e);
            model.addAttribute("error", "Error al cargar el formulario: " + e.getMessage());
        }

        return "admin/usuarios/form";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        log.info("Mostrando formulario de edición para usuario ID: {}", id);

        try {
            UsuarioDto usuario = usuarioService.findById(id);

            List<RolDto> roles = rolJpaRepository.findAll().stream()
                    .map(rolMapper::toDto)
                    .collect(Collectors.toList());

            model.addAttribute("titulo", "Editar Usuario");
            model.addAttribute("subtitulo", "Modificar datos del usuario");
            model.addAttribute("usuario", usuario);
            model.addAttribute("roles", roles);
            model.addAttribute("rubros", rubroService.findAll());
            model.addAttribute("estados", EstadoUsuario.values());
            model.addAttribute("esNuevo", false);

        } catch (Exception e) {
            log.error("Error al cargar formulario de edición de usuario: {}", e.getMessage(), e);
            model.addAttribute("error", "Error al cargar el formulario: " + e.getMessage());
        }

        return "admin/usuarios/form";
    }

    @PostMapping("/guardar")
    public String guardarUsuario(@Valid UsuarioDto usuario,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        try {
            List<RolDto> roles = rolJpaRepository.findAll().stream()
                    .map(rolMapper::toDto)
                    .collect(Collectors.toList());

            model.addAttribute("roles", roles);
            model.addAttribute("rubros", rubroService.findAll());
            model.addAttribute("estados", EstadoUsuario.values());
            model.addAttribute("usuario", usuario);

            // roles
            if (usuario.getRolesIds() != null && !usuario.getRolesIds().isEmpty()) {
                List<UserRoleDto> userRoles = usuario.getRolesIds().stream()
                        .map(rolId -> {
                            UserRoleDto ur = new com.api.diversity.application.dto.UserRoleDto();
                            RolDto rol = rolJpaRepository.findById(rolId)
                                    .map(rolMapper::toDto)
                                    .orElseThrow(() -> new RuntimeException("Rol no encontrado: " + rolId));
                            ur.setRol(rol);
                            ur.setEstado(com.api.diversity.domain.enums.EstadoUserRole.Activo);
                            return ur;
                        }).toList();
                usuario.setRoles(userRoles);
            } else {
                usuario.setRoles(null);
            }

            // rubros
            if (usuario.getRubrosIds() != null && !usuario.getRubrosIds().isEmpty()) {
                List<com.api.diversity.application.dto.RubroDto> rubrosSeleccionados = rubroService.findAll().stream()
                        .filter(r -> usuario.getRubrosIds().contains(r.getIdRubro()))
                        .toList();
                usuario.setRubros(rubrosSeleccionados);
            } else {
                usuario.setRubros(null);
            }

            if (result.hasErrors()) {
                model.addAttribute("titulo", usuario.getIdUsuario() == null ? "Nuevo Usuario" : "Editar Usuario");
                model.addAttribute("subtitulo",
                        usuario.getIdUsuario() == null ? "Registrar nuevo usuario" : "Modificar datos del usuario");
                model.addAttribute("esNuevo", usuario.getIdUsuario() == null);
                model.addAttribute("error", "Error en los datos del usuario");
                return "admin/usuarios/form";
            }

            // Validar que el email no esté duplicadoo
            if (usuario.getIdUsuario() == null) {
                if (usuarioService.existsByEmail(usuario.getEmail())) {
                    model.addAttribute("titulo", "Nuevo Usuario");
                    model.addAttribute("subtitulo", "Registrar nuevo usuario");
                    model.addAttribute("esNuevo", true);
                    model.addAttribute("error", "El email ya está registrado en el sistema");
                    return "admin/usuarios/form";
                }

                // Validar que el nombre de usuario no esté duplicado
                if (usuarioService.existsByNombreUsuario(usuario.getNombreUsuario())) {
                    model.addAttribute("titulo", "Nuevo Usuario");
                    model.addAttribute("subtitulo", "Registrar nuevo usuario");
                    model.addAttribute("esNuevo", true);
                    model.addAttribute("error", "El nombre de usuario ya está registrado en el sistema");
                    return "admin/usuarios/form";
                }
            } else {
                try {
                    UsuarioDto usuarioExistente = usuarioService.findByEmail(usuario.getEmail());
                    if (usuarioExistente != null && !usuarioExistente.getIdUsuario().equals(usuario.getIdUsuario())) {
                        model.addAttribute("titulo", "Editar Usuario");
                        model.addAttribute("subtitulo", "Modificar datos del usuario");
                        model.addAttribute("esNuevo", false);
                        model.addAttribute("error", "El email ya está registrado por otro usuario");
                        return "admin/usuarios/form";
                    }
                } catch (Exception e) {

                }
                try {
                    UsuarioDto usuarioExistente = usuarioService.findByNombreUsuario(usuario.getNombreUsuario());
                    if (usuarioExistente != null && !usuarioExistente.getIdUsuario().equals(usuario.getIdUsuario())) {
                        model.addAttribute("titulo", "Editar Usuario");
                        model.addAttribute("subtitulo", "Modificar datos del usuario");
                        model.addAttribute("esNuevo", false);
                        model.addAttribute("error", "El nombre de usuario ya está registrado por otro usuario");
                        return "admin/usuarios/form";
                    }
                } catch (Exception e) {

                }
            }

            usuarioService.save(usuario);
            redirectAttributes.addFlashAttribute("success", "Usuario guardado exitosamente");
            return "redirect:/admin/usuarios";

        } catch (Exception e) {
            log.error("Error al guardar usuario: {}", e.getMessage(), e);
            model.addAttribute("titulo", usuario.getIdUsuario() == null ? "Nuevo Usuario" : "Editar Usuario");
            model.addAttribute("subtitulo",
                    usuario.getIdUsuario() == null ? "Registrar nuevo usuario" : "Modificar datos del usuario");
            model.addAttribute("esNuevo", usuario.getIdUsuario() == null);
            model.addAttribute("error", "Error al guardar el usuario: " + e.getMessage());
            return "admin/usuarios/form";
        }
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarUsuario(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        log.info("Eliminando usuario ID: {}", id);

        try {
            UsuarioDto usuario = usuarioService.findById(id);

            if ("admin@gmail.com".equals(usuario.getEmail())) {
                redirectAttributes.addFlashAttribute("error",
                        "No se puede eliminar al administrador principal del sistema");
                return "redirect:/admin/usuarios";
            }

            usuarioService.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Usuario eliminado exitosamente");

        } catch (Exception e) {
            log.error("Error al eliminar usuario: {}", e.getMessage(), e);
            redirectAttributes.addFlashAttribute("error", "Error al eliminar el usuario: " + e.getMessage());
        }

        return "redirect:/admin/usuarios";
    }

    @GetMapping("/cambiar-estado/{id}")
    public String cambiarEstadoUsuario(@PathVariable Long id,
            @RequestParam EstadoUsuario nuevoEstado,
            RedirectAttributes redirectAttributes) {
        log.info("Cambiando estado del usuario ID: {} a: {}", id, nuevoEstado);

        try {
            UsuarioDto usuario = usuarioService.findById(id);

            if ("admin@gmail.com".equals(usuario.getEmail()) && nuevoEstado == EstadoUsuario.Bloqueado) {
                redirectAttributes.addFlashAttribute("error",
                        "No se puede bloquear al administrador principal del sistema");
                return "redirect:/admin/usuarios";
            }

            usuario.setEstado(nuevoEstado);
            usuarioService.save(usuario);
            redirectAttributes.addFlashAttribute("success", "Estado del usuario actualizado exitosamente");

        } catch (Exception e) {
            log.error("Error al cambiar estado del usuario: {}", e.getMessage(), e);
            redirectAttributes.addFlashAttribute("error", "Error al cambiar el estado: " + e.getMessage());
        }

        return "redirect:/admin/usuarios";
    }
}