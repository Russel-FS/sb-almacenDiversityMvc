package com.api.diversity.application.service.interfaces;

import java.util.List;
import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.api.diversity.application.dto.RubroDto;

public interface IRubroService {
    /**
     * Obtiene una lista de todos los rubros activos.
     *
     * @return una lista de objetos RubroDto
     */
    @Transactional(readOnly = true)
    List<RubroDto> findAll();

    /**
     * Busca un rubro por su ID.
     *
     * @param id el ID del rubro
     * @return un objeto RubroDto si se encuentra, de lo contrario lanza una
     *         excepción
     */
    @Transactional(readOnly = true)
    RubroDto findById(Long id);

    /**
     * Guarda un nuevo rubro o actualiza uno existente.
     *
     * @param rubroDto el objeto RubroDto a guardar
     * @param imagen   la imagen asociada al rubro, puede ser nula
     * @return el objeto RubroDto guardado
     */
    @Transactional
    RubroDto save(RubroDto rubroDto, MultipartFile imagen);

    /**
     * desactiva un rubro por su ID.
     *
     * @param id el ID del rubro a desactivar
     */
    @Transactional
    void deleteById(Long id);

    /**
     * Verifica si un rubro con el nombre dado ya existe.
     *
     * @param nombreRubro el nombre del rubro
     * @return true si el rubro existe, false en caso contrario
     */
    @Transactional(readOnly = true)
    boolean existsByNombreRubro(String nombreRubro);

    /**
     * Busca un rubro por su nombre.
     *
     * @param nombreRubro el nombre del rubro
     * @return un objeto RubroDto si se encuentra, de lo contrario devuelve un
     *         Optional vacío
     */
    @Transactional(readOnly = true)
    Optional<RubroDto> findByNombreRubro(String nombreRubro);
}