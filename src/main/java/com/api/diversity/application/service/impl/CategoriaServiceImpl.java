package com.api.diversity.application.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.api.diversity.application.dto.CategoriaDto;
import com.api.diversity.application.mappers.CategoryMapper;
import com.api.diversity.application.service.interfaces.ICategoriaService;
import com.api.diversity.domain.ports.ICategoriaRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional
public class CategoriaServiceImpl implements ICategoriaService {

    private final ICategoriaRepository categoriaRepository;
    private final CategoryMapper categoriaMapper;

    @Override
    @Transactional(readOnly = true)
    public List<CategoriaDto> findAll() {
        return categoriaRepository.findAll()
                .stream()
                .map(categoriaMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CategoriaDto> findById(Long id) {
        return categoriaRepository.findById(id)
                .map(categoriaMapper::toDto);
    }

    @Override
    public CategoriaDto save(CategoriaDto categoria) {
        return categoriaMapper.toDto(categoriaRepository.save(categoriaMapper.toEntity(categoria)));
    }

    @Override
    public void deleteById(Long id) {
        categoriaRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return categoriaRepository.existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByNombreCategoria(String nombreCategoria) {
        return categoriaRepository.existsByNombreCategoria(nombreCategoria);
    }
}
