package com.api.diversity.application.service.interfaces;

import java.util.List;
import java.util.Optional;

import com.api.diversity.application.dto.Producto;

public interface IProductoService {
    List<Producto> findAll();
    Optional<Producto> findById(String id);
    Producto save(Producto producto);
    void deleteById(String id);
    List<Producto> findByCategoria(Long categoriaId);
}
