package com.api.diversity.application.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.api.diversity.application.dto.Producto;
import com.api.diversity.application.mappers.ProductMapper;
import com.api.diversity.application.service.interfaces.IProductoService;
import com.api.diversity.domain.model.ProductoEntity;
import com.api.diversity.domain.ports.IProductoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductoServiceImpl implements IProductoService {

    private final IProductoRepository productoRepository;
    private final ProductMapper productoMapper;
    private final CloudinaryService cloudinaryService;

    @Override
    @Transactional(readOnly = true)
    public List<Producto> findAll() {
        return productoRepository.findAll()
                .stream()
                .map(productoMapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Producto> findById(Long id) {
        return productoRepository.findById(id)
                .map(productoMapper::toModel);
    }

    @Override
    @Transactional
    public Producto save(Producto producto, MultipartFile imagen) {
        if (imagen != null && !imagen.isEmpty()) {
            CloudinaryService.CloudinaryResponse response = cloudinaryService.uploadFile(imagen, "productos");
            producto.setUrlImagen(response.getUrl());
            producto.setPublicId(response.getPublicId());
        }
        if (producto.getIdProducto() == null || producto.getIdProducto() == 0) {
            producto.setFechaCreacion(LocalDateTime.now());
        }
        ProductoEntity entity = productoMapper.toEntity(producto);
        ProductoEntity savedEntity = productoRepository.save(entity);
        return productoMapper.toModel(savedEntity);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Optional<ProductoEntity> productoOpt = productoRepository.findById(id);
        if (productoOpt.isPresent()) {
            ProductoEntity producto = productoOpt.get();
            if (producto.getPublicId() != null && !producto.getPublicId().isEmpty()) {
                cloudinaryService.deleteFile(producto.getPublicId());
            }
        }
        productoRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Producto> findByCategoria(Long categoriaId) {
        return productoRepository.findByCategoria(categoriaId)
                .stream()
                .map(productoMapper::toModel)
                .collect(Collectors.toList());
    }
}
