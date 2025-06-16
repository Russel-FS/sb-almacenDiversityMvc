package com.api.diversity.application.service.interfaces;

import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import com.api.diversity.application.dto.ProductoDto;
import com.api.diversity.domain.enums.TipoRubro;

public interface IProductoService {
    /**
     * obtiene todos los productos activos.
     *
     * @return una lista de productos activos.
     */
    List<ProductoDto> findAll();

    /**
     * Obtiene todos los productos filtrados por rubro.
     *
     * @param rubro el tipo de rubro por el cual filtrar los productos
     * @return lista de productos del rubro especificado
     */
    List<ProductoDto> findAllByRubro(TipoRubro rubro);

    /**
     * Busca un producto por su id.
     *
     * @param id el id del producto a buscar
     * @return un Optional que contiene el ProductoDto si se encuentra, o vacío si
     *         no
     */
    Optional<ProductoDto> findById(Long id);

    /**
     * Guarda un producto nuevo o actualizado.
     *
     * @param producto el ProductoDto a guardar
     * @param imagen   la imagen del producto a subir
     * @return el ProductoDto guardado
     */
    ProductoDto save(ProductoDto producto, MultipartFile imagen);

    /**
     * Elimina un producto por su id.
     * 
     * @param id el id del producto a eliminar
     * @throws RuntimeException si el producto no se encuentra o no se puede
     */
    void deleteById(Long id);

    /**
     * Busca productos por categoría.
     *
     * @param categoriaId el id de la categoría por la cual filtrar los productos
     * @return lista de productos de la categoría especificada
     */
    List<ProductoDto> findByCategoria(Long categoriaId);
}
