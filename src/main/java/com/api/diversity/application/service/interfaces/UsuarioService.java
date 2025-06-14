package com.api.diversity.application.service.interfaces;

import java.util.List;
import com.api.diversity.domain.model.Usuario;

public interface UsuarioService {

    List<Usuario> getAllUsuarios();

    void saveUsuario(Usuario usuario);

    Usuario getUsuarioById(Integer id); 

    void deleteUsuarioById(Integer id); 
}