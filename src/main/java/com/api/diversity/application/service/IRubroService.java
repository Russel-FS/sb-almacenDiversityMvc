package com.api.diversity.application.service;

import com.api.diversity.application.dto.RubroDto;
import com.api.diversity.domain.enums.NombreRubro;

import java.util.List;

public interface IRubroService {
    List<RubroDto> findAll();

    RubroDto findById(Long id);

    RubroDto save(RubroDto rubroDto);

    void deleteById(Long id);

    boolean existsByNombreRubro(NombreRubro nombreRubro);
}