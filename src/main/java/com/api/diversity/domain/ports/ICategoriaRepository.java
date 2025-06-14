package com.api.diversity.domain.ports;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.diversity.application.dto.CategoriaDto;

@Repository
public interface ICategoriaRepository extends JpaRepository<CategoriaDto, Long> {
       boolean existsByNombreCategoria(String nombreCategoria);
}
