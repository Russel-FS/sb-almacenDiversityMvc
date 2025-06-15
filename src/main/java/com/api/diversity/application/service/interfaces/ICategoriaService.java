package com.api.diversity.application.service.interfaces;

import java.util.List;
import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;

import com.api.diversity.application.dto.CategoriaDto;

public interface ICategoriaService {
 
    /**
     * Obtiene una lista de todas las categorías existentes.
     *
     * @return lista de categorías
     */
    @Transactional(readOnly = true)
    List<CategoriaDto> findAll();

    /** 
     * Busca una categoría por su id.
     *
     * @param id category id
     * @return optional CategoriaDto
     * @throws RuntimeException si no se encuentra la categoría
     */
    @Transactional(readOnly = true)
    Optional<CategoriaDto> findById(Long id);

    /**
     * Guarda una categoría en la base de datos. Si el id es nulo, se
     * considera una creación de una nueva categoría, de lo contrario se
     * actualiza la categoría existente.
     * 
     * @param categoria categoría a guardar
     * @return categoría guardada
     * @throws RuntimeException si no se puede guardar la categoría
     */
    @Transactional
    CategoriaDto save(CategoriaDto categoria);

    /**
     * Elimina una categoría por su id.
     * 
     * @param id id de la categoría a eliminar
     * @throws RuntimeException si no se puede eliminar la categoría
     */
    @Transactional
    void deleteById(Long id);

    /**
     * Verifica si una categoría existe por su id.
     *
     * @param id id de la categoría a verificar
     * @return true si existe, false de lo contrario
     */
    @Transactional
    boolean existsById(Long id);

    /**
     * Verifica si una categoría existe por su nombre.
     *
     * @param nombreCategoria nombre de la categoría a verificar
     * @return true si existe, false de lo contrario
     */
    @Transactional(readOnly = true)
    boolean existsByNombreCategoria(String nombreCategoria);
}
