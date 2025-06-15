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
import com.api.diversity.domain.enums.EstadoCategoria;
import com.api.diversity.domain.ports.ICategoriaRepository;
import com.api.diversity.infrastructure.security.SecurityContext;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoriaServiceImpl implements ICategoriaService {

    private final ICategoriaRepository categoriaRepository;
    private final CategoryMapper categoriaMapper;
    private final SecurityContext securityContext;

    @Override
    @Transactional(readOnly = true)
    public List<CategoriaDto> findAll() {
        return categoriaRepository.findAll()
                .stream()
                .map(categoriaMapper::toDto)
                .filter(cat -> cat.getEstado() == EstadoCategoria.Activo)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoriaDto> findAllIncludingInactive() {
        return categoriaRepository.findAll()
                .stream()
                .map(categoriaMapper::toDto)
                .filter(cat -> cat.getEstado() != EstadoCategoria.Eliminado)
                .collect(Collectors.toList());
    }

    @Override

    @Transactional(readOnly = true)
    public List<CategoriaDto> findAllArchived() {
        return categoriaRepository.findAll()
                .stream()
                .map(categoriaMapper::toDto)
                .filter(cat -> cat.getEstado() == EstadoCategoria.Eliminado)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deactivateCategory(Long id) {
        CategoriaDto categoria = findById(id)
                .orElseThrow(() -> new RuntimeException("No se encontró la categoría"));

        UsuarioDto currentUser = securityContext.getCurrentUserDatabase();
        if (currentUser == null) {
            throw new RuntimeException("No se puede desactivar la categoría sin un usuario autenticado");
        }

        categoria.setEstado(EstadoCategoria.Inactivo);
        categoria.setUpdatedBy(currentUser);
        categoria.setFechaModificacion(LocalDateTime.now());

        categoriaMapper.toDto(categoriaRepository.save(categoriaMapper.toEntity(categoria)));
    }

    @Override
    @Transactional
    public void activateCategory(Long id) {
        CategoriaDto categoria = findById(id)
                .orElseThrow(() -> new RuntimeException("No se encontró la categoría"));

        UsuarioDto currentUser = securityContext.getCurrentUserDatabase();
        if (currentUser == null) {
            throw new RuntimeException("No se puede activar la categoría sin un usuario autenticado");
        }

        categoria.setEstado(EstadoCategoria.Activo);
        categoria.setUpdatedBy(currentUser);
        categoria.setFechaModificacion(LocalDateTime.now());

        categoriaMapper.toDto(categoriaRepository.save(categoriaMapper.toEntity(categoria)));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CategoriaDto> findById(Long id) {
        return categoriaRepository.findById(id)
                .map(categoriaMapper::toDto);
    }

    @Override
    public CategoriaDto save(CategoriaDto categoria) {
        UsuarioDto currentUser = securityContext.getCurrentUserDatabase();

        if (currentUser == null) {
            throw new RuntimeException("No se puede guardar la categoría sin un usuario autenticado");
        }

        if (categoria.getIdCategoria() == null) {
            categoria.setCreatedBy(currentUser);
            categoria.setUpdatedBy(currentUser);
            categoria.setFechaCreacion(LocalDateTime.now());
            categoria.setFechaModificacion(LocalDateTime.now());
        } else {
            CategoriaDto categoriaExistente = findById(categoria.getIdCategoria())
                    .orElseThrow(() -> new RuntimeException("No se encontró la categoría a editar"));

            categoria.setCreatedBy(categoriaExistente.getCreatedBy());
            categoria.setFechaCreacion(categoriaExistente.getFechaCreacion());
            categoria.setUpdatedBy(currentUser);
            categoria.setFechaModificacion(LocalDateTime.now());
        }

        return categoriaMapper.toDto(categoriaRepository.save(categoriaMapper.toEntity(categoria)));
    }

    @Override
    public void deleteById(Long id) {
        CategoriaDto categoria = findById(id)
                .orElseThrow(() -> new RuntimeException("No se encontró la categoría a eliminar"));
        // usuario del contexto
        UsuarioDto currentUser = securityContext.getCurrentUserDatabase();
        if (currentUser == null) {
            throw new RuntimeException("No se puede eliminar la categoría sin un usuario autenticado");
        } 
        // Actualizar estado y usuario que modificoo
        categoria.setEstado(EstadoCategoria.Eliminado);
        categoria.setUpdatedBy(currentUser);
        categoria.setFechaModificacion(LocalDateTime.now());

        // Guardar los cambios
        categoriaMapper.toDto(categoriaRepository.save(categoriaMapper.toEntity(categoria)));
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
