package com.api.diversity.application.service.impl;

import org.springframework.stereotype.Service;

import com.api.diversity.application.dto.RolDto;
import com.api.diversity.application.mappers.RolMapper;
import com.api.diversity.application.service.interfaces.IRolService;
import com.api.diversity.domain.ports.IRolRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RolServiceImpl implements IRolService {

    private final IRolRepository rolRepository;
    private final RolMapper rolMapper;

    @Override
    public RolDto findById(Long id) {
        return rolRepository.findById(id)
                .map(rolMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
    }
}