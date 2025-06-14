package com.api.diversity.application.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.api.diversity.application.dto.Producto;
import com.api.diversity.application.service.interfaces.IProductoService;
import com.api.diversity.domain.ports.IProductoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductoServiceImpl implements IProductoService {
    
    private final IProductoRepository productoRepository;

    @Override
    public List<Producto> findAll() {
        return productoRepository.findAll();
    }

    @Override
    public Optional<Producto> findById(String id) {
        return productoRepository.findById(id);
    }

    @Override
    public Producto save(Producto producto) {
        return productoRepository.save(producto);
    }

    @Override
    public void deleteById(String id) {
        productoRepository.deleteById(id);
    }

    @Override
    public List<Producto> findByCategoria(Long categoriaId) {
        return productoRepository.findByCategoria(categoriaId);
    }
}
