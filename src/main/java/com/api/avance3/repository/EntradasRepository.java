package com.example.avance3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.avance3.model.Entradas;

@Repository
public interface EntradasRepository extends JpaRepository<Entradas, Long> {
}
