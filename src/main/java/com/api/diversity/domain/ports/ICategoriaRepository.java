package com.api.diversity.domain.ports;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.diversity.domain.model.Categoria;

@Repository
public interface ICategoriaRepository extends JpaRepository<Categoria, Long> {
       boolean existsByNombreCategoria(String nombreCategoria);
}
