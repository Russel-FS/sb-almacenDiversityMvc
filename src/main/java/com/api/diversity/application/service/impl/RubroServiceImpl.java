package com.api.diversity.application.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.api.diversity.application.dto.RubroDto;
import com.api.diversity.application.mappers.RubroMapper;
import com.api.diversity.application.service.impl.CloudinaryService.CloudinaryResponse;
import com.api.diversity.application.service.interfaces.IRubroService;
import com.api.diversity.domain.enums.EstadoRubro;
import com.api.diversity.domain.enums.TipoRubro;
import com.api.diversity.domain.model.RubroEntity;
import com.api.diversity.domain.ports.RubroRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RubroServiceImpl implements IRubroService {

    @Autowired
    private RubroRepository rubroRepository;
    private final CloudinaryService cloudinaryService;

    @Autowired
    private RubroMapper rubroMapper;

    @Override
    @Transactional(readOnly = true)
    public List<RubroDto> findAll() {
        return rubroRepository.findAll().stream()
                .filter(rubro -> rubro.getEstado() == EstadoRubro.Activo)
                .filter(rubro -> rubro.getCode() != null && !rubro.getCode().contains(TipoRubro.SIN_RUBRO.getCode()))
                .map(rubroMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public RubroDto findById(Long id) {
        return rubroRepository.findById(id)
                .map(rubroMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Rubro no encontrado"));
    }

    @Override
    @Transactional
    public RubroDto save(RubroDto rubroDto, MultipartFile imagen) {

        if (imagen != null && !imagen.isEmpty()) {
            CloudinaryResponse response = cloudinaryService.uploadFile(imagen, "rubro");
            rubroDto.setImagenUrl(response.getUrl());
            rubroDto.setPublicId(response.getPublicId());
        }
        if (rubroDto.getEstado() == null) {
            rubroDto.setEstado(EstadoRubro.Activo);
        }

        RubroEntity rubro = rubroMapper.toEntity(rubroDto);
        return rubroMapper.toDto(rubroRepository.save(rubro));
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        rubroRepository.findById(id).ifPresent(rubro -> {
            rubro.setEstado(EstadoRubro.Inactivo);
            rubroRepository.save(rubro);
        });
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByNombreRubro(String nombreRubro) {
        return rubroRepository.existsByNombreRubro(nombreRubro);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RubroDto> findByNombreRubro(String nombreRubro) {
        return rubroRepository.findByNombreRubro(nombreRubro)
                .map(rubroMapper::toDto);
    }
}