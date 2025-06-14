package com.api.diversity.application.service;

import com.api.diversity.application.dto.RubroDto;
import com.api.diversity.application.mappers.RubroMapper;
import com.api.diversity.domain.model.RubroEntity;
import com.api.diversity.domain.ports.RubroRepository;
import com.api.diversity.domain.enums.EstadoRubro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RubroService implements IRubroService {

    @Autowired
    private RubroRepository rubroRepository;

    @Autowired
    private RubroMapper rubroMapper;

    @Override
    @Transactional(readOnly = true)
    public List<RubroDto> findAll() {
        return rubroRepository.findAll().stream()
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
    public RubroDto save(RubroDto rubroDto) {
        if (rubroDto.getEstado() == null) {
            rubroDto.setEstado(EstadoRubro.Activo);
        }

        if (rubroDto.getCreatedBy() == null) {
            rubroDto.setCreatedBy(1L);
        }

        RubroEntity rubro = rubroMapper.toEntity(rubroDto);
        return rubroMapper.toDto(rubroRepository.save(rubro));
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        rubroRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByNombreRubro(String nombreRubro) {
        return rubroRepository.existsByNombreRubro(nombreRubro);
    }
}