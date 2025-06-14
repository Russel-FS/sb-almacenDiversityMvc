package com.api.diversity.application.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.api.diversity.application.dto.Producto;
import com.api.diversity.application.mappers.ProductMapper;
import com.api.diversity.application.service.interfaces.IProductoService;
import com.api.diversity.domain.model.ProductoEntity;
import com.api.diversity.domain.ports.IProductoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductoServiceImpl implements IProductoService {
    
    private final IProductoRepository productoRepository;
    private final ProductMapper productoMapper;

    @Override
    public List<Producto> findAll() {
        return productoRepository.findAll()
                .stream()
                .map(productoMapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Producto> findById(String id) {
        return productoRepository.findById(id)
                .map(productoMapper::toModel);
    }

    @Override
    public Producto save(Producto producto) {
        ProductoEntity entity = productoMapper.toEntity(producto);
        return productoRepository.save(entity)
                .map(productoMapper::toModel)
                .orElse(null);
    }

    @Override
    public void deleteById(String id) {
        productoRepository.deleteById(id);
    }

    @Override
    public List<Producto> findByCategoria(Long categoriaId) {
        return productoRepository.findByCategoria(categoriaId)
                .stream()
                .map(productoMapper::toModel)
                .collect(Collectors.toList());
    }
}
