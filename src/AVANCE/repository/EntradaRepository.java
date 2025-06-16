package com.example.AVANCE.repository;

import com.example.AVANCE.model.Entrada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EntradaRepository extends JpaRepository<Entrada, Long> {
    
}

