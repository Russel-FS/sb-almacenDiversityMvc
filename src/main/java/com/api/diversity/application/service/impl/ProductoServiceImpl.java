package com.api.diversity.application.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.api.diversity.application.dto.ProductoDto;
import com.api.diversity.application.mappers.ProductMapper;
import com.api.diversity.application.service.impl.CloudinaryService.CloudinaryResponse;
import com.api.diversity.application.service.interfaces.IProductoService;
import com.api.diversity.domain.enums.EstadoProducto;
import com.api.diversity.domain.model.ProductoEntity;
import com.api.diversity.domain.ports.IProductoRepository;
import com.api.diversity.infrastructure.security.SecurityContext;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ProductoServiceImpl implements IProductoService {

    private final IProductoRepository productoRepository;
    private final ProductMapper productoMapper;
    private final CloudinaryService cloudinaryService;
    private final SecurityContext securityContext;

    @Override
    @Transactional(readOnly = true)
    public List<ProductoDto> findAll() {
        return productoRepository.findAll()
                .stream()
                .filter(producto -> producto.getEstado() != EstadoProducto.Eliminado && producto.getEstado() != null)
                .map(productoMapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductoDto> findById(Long id) {
        return productoRepository.findById(id)
                .map(productoMapper::toModel);
    }

    @Override
    @Transactional
    public ProductoDto save(ProductoDto producto, MultipartFile imagen) {

        log.info("Guardando producto: {}", producto.getNombreProducto());
        // validar campos de producto
        if (imagen != null && !imagen.isEmpty())  {
            CloudinaryResponse response = cloudinaryService.uploadFile(imagen, "productos");
            producto.setUrlImagen(response.getUrl());
            producto.setPublicId(response.getPublicId());
        }  
        // establecer el usuario que crea o actualiza el producto de la sesión actual
        if (producto.getIdProducto() == null) {
            producto.setCreatedBy(securityContext.getCurrentUserDatabase());
            producto.setFechaCreacion(LocalDateTime.now());
        } else {
            producto.setUpdatedBy(securityContext.getCurrentUserDatabase());
            producto.setFechaModificacion(LocalDateTime.now());
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
            producto.setEstado(EstadoProducto.Inactivo);
            if (producto.getPublicId() != null && !producto.getPublicId().isEmpty()) {
                cloudinaryService.deleteFile(producto.getPublicId());
            }
            productoRepository.save(producto);
        } 
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoDto> findByCategoria(Long categoriaId) {
        return productoRepository.findByCategoria(categoriaId)
                .stream()
                .map(productoMapper::toModel)
                .collect(Collectors.toList());
    }
}
