package com.api.diversity.application.service.interfaces;

import com.api.diversity.application.dto.RubroDto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface IRubroService {
    List<RubroDto> findAll();

    RubroDto findById(Long id);

    RubroDto save(RubroDto rubroDto, MultipartFile imagen);

    void deleteById(Long id);

    boolean existsByNombreRubro(String nombreRubro);
}