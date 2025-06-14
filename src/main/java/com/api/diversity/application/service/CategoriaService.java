package com.api.diversity.application.service;

import com.api.diversity.application.interfaces.ICategoriaService;
import com.api.diversity.domain.model.Categoria;
import com.api.diversity.infrastructure.repository.ICategoriaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
public class CategoriaService implements ICategoriaService {

    private final ICategoriaRepository categoriaRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Categoria> findAll() {
        return categoriaRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)    public Optional<Categoria> findById(Long id) {
        return categoriaRepository.findById(id);
    }

    @Override
    public Categoria save(Categoria categoria) {
        return categoriaRepository.save(categoria);
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
