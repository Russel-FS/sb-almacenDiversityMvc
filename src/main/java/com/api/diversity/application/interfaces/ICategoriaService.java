package com.api.diversity.application.interfaces;

import com.api.diversity.domain.model.Categoria;
import java.util.List;
import java.util.Optional;

public interface ICategoriaService {
    List<Categoria> findAll();    Optional<Categoria> findById(Long id);
    Categoria save(Categoria categoria);
    void deleteById(Long id);
    boolean existsById(Long id);
    boolean existsByNombreCategoria(String nombreCategoria);
}
