package com.api.diversity.application.service.interfaces;

import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import com.api.diversity.application.dto.ProductoDto;

public interface IProductoService {
    List<ProductoDto> findAll();

    Optional<ProductoDto> findById(Long id);

    ProductoDto save(ProductoDto producto, MultipartFile imagen);

    void deleteById(Long id);

    List<ProductoDto> findByCategoria(Long categoriaId);
}
