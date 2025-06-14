package com.api.diversity.application.service.interfaces;

import java.util.List;
import java.util.Optional;

import com.api.diversity.application.dto.CategoriaDto;

public interface ICategoriaService {
    List<CategoriaDto> findAll();    Optional<CategoriaDto> findById(Long id);
    CategoriaDto save(CategoriaDto categoria);
    void deleteById(Long id);
    boolean existsById(Long id);
    boolean existsByNombreCategoria(String nombreCategoria);
}
