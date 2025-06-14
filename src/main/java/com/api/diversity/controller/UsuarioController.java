package com.api.diversity.controller;



import com.api.diversity.model.Usuario;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/Usuario") 
public class UsuarioController {

   
    private static List<Usuario> usuarios = new ArrayList<>();

    // GET: /api/Usuario/listar
    @GetMapping("/listar")
    public List<Usuario> listarUsuarios() {
        return usuarios;
    }

    // GET: /api/Usuario/buscar/{id}
    @GetMapping("/buscar/{id}")
    public Usuario buscarUsuario(@PathVariable Integer id) {
        // Lógica de búsqueda con Stream, idéntica a tu ejemplo
        return usuarios.stream()
                .filter(usuario -> usuario.getIdUsuario().equals(id))
                .findFirst()
                .orElse(null);
    }

    // POST: /api/Usuario/crear
    @PostMapping("/crear")
    public Usuario crearUsuario(@RequestBody Usuario usuario) {
       
        usuario.setIdUsuario(usuarios.size() + 1);
        usuarios.add(usuario);
        return usuario;
    }

    // PUT: /api/Usuario/actualizar/{id}
    @PutMapping("/actualizar/{id}")
    public Usuario actualizarUsuario(@PathVariable Integer id, @RequestBody Usuario usuario) {
       
        Usuario usuarioActual = buscarUsuario(id);
        if (usuarioActual != null) {
            usuarioActual.setNombreUsuario(usuario.getNombreUsuario());
            usuarioActual.setRol(usuario.getRol());
            usuarioActual.setContraseña(usuario.getContraseña());
        }
        return usuarioActual;
    }

    // DELETE: /api/Usuario/eliminar/{id}
    @DeleteMapping("/eliminar/{id}")
    public Usuario eliminarUsuario(@PathVariable Integer id) {
        // Lógica para eliminar, idéntica a tu ejemplo
        Usuario usuarioAEliminar = buscarUsuario(id);
        if (usuarioAEliminar != null) {
            usuarios.remove(usuarioAEliminar);
        }
        return usuarioAEliminar;
    }
}