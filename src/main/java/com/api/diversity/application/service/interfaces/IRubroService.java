package com.api.diversity.application.service.interfaces;

import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import com.api.diversity.application.dto.RubroDto;

public interface IRubroService {
    List<RubroDto> findAll();

    RubroDto findById(Long id);

    RubroDto save(RubroDto rubroDto, MultipartFile imagen);

    void deleteById(Long id);

    boolean existsByNombreRubro(String nombreRubro);

    Optional<RubroDto> findByNombreRubro(String nombreRubro);
}