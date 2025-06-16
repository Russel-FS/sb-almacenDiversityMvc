package com.api.diversity.application.service.interfaces;

import java.util.List;
import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;

import com.api.diversity.application.dto.CategoriaDto;
import com.api.diversity.domain.enums.TipoRubro;

public interface ICategoriaService {
    /**
     * Obtiene una lista de todas las categorías activas filtradas por rubro.
     *
     * @param rubro el tipo de rubro por el cual filtrar las categorías
     * @return lista de categorías activas del rubro especificado
     */
    @Transactional(readOnly = true)
    List<CategoriaDto> findAllByRubro(TipoRubro rubro);

    /**
     * Obtiene una lista de todas las categorías activas e inactivas (no
     * eliminadas).
     *
     * @return lista de categorías no eliminadas
     */
    @Transactional(readOnly = true)
    List<CategoriaDto> findAllIncludingInactive();

    /**
     * Obtiene una lista de todas las categorías archivadas (eliminadas).
     *
     * @return lista de categorías archivadas
     */
    @Transactional(readOnly = true)
    List<CategoriaDto> findAllArchived();

    /**
     * Desactiva temporalmente una categoría.
     *
     * @param id id de la categoría a desactivar
     */
    @Transactional
    void deactivateCategory(Long id);

    /**
     * Activa una categoría previamente desactivada.
     *
     * @param id id de la categoría a activar
     */
    @Transactional
    void activateCategory(Long id);

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
