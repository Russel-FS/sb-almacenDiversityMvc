package com.api.diversity.infrastructure.repository;

import com.api.diversity.domain.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICategoriaRepository extends JpaRepository<Categoria, Long> {
       boolean existsByNombreCategoria(String nombreCategoria);
}
