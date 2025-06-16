package com.example.AVANCE.repository;

import com.example.AVANCE.model.DetalleEntrada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface DetalleEntradaRepository extends JpaRepository<DetalleEntrada, Long> {
    
}
