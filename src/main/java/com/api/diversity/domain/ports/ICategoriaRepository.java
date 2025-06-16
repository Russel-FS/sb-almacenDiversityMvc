package com.api.diversity.domain.ports;

import java.util.List;
import java.util.Optional;

import com.api.diversity.domain.enums.EstadoCategoria;
import com.api.diversity.domain.enums.TipoRubro;
import com.api.diversity.domain.model.CategoryEntity;

public interface ICategoriaRepository {
    List<CategoryEntity> findAll();

    Optional<CategoryEntity> findById(Long id);

    CategoryEntity save(CategoryEntity category);

    void deleteById(Long id);

    boolean existsById(Long id);

    boolean existsByNombreCategoria(String nombreCategoria);

    /**
     * Busca todas las categorías por rubro y estado.
     *
     * @param rubro  el tipo de rubro
     * @param estado el estado de la categoría
     * @return lista de categorías que coinciden con los criterios
     */
    List<CategoryEntity> findAllByRubroAndEstado(TipoRubro rubro, EstadoCategoria estado);
}
