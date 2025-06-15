package com.api.diversity.application.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.api.diversity.application.dto.CategoriaDto;
import com.api.diversity.application.dto.UsuarioDto;
import com.api.diversity.application.mappers.CategoryMapper;
import com.api.diversity.application.service.interfaces.ICategoriaService;
import com.api.diversity.application.service.utils.SecurityUtils;
import com.api.diversity.domain.ports.ICategoriaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoriaServiceImpl implements ICategoriaService {

    private final ICategoriaRepository categoriaRepository;
    private final CategoryMapper categoriaMapper;
    private final SecurityUtils securityUtils;

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
    }    @Override
    public CategoriaDto save(CategoriaDto categoria) {
        UsuarioDto currentUser = securityUtils.getCurrentUser();
        
        if (currentUser == null) {
            throw new RuntimeException("No se puede guardar la categoría sin un usuario autenticado");
        }

        // Si es una nueva categoría
        if (categoria.getIdCategoria() == null) {
            categoria.setCreatedBy(currentUser);
            categoria.setFechaCreacion(LocalDateTime.now());
        }
        
        // Siempre actualizar estos campos
        categoria.setUpdatedBy(currentUser);
        categoria.setFechaModificacion(LocalDateTime.now());
        
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
