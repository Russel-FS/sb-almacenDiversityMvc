package com.api.diversity.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.diversity.domain.model.Pedidos;

@Repository
public interface PedidosRepository extends JpaRepository<Pedidos, Long> {
    
}
