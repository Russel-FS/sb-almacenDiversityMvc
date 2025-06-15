package com.api.diversity.infrastructure.service;

import java.util.List;

import com.api.diversity.domain.model.Pedidos;

public interface PedidosService {

    List<Pedidos> findAllPedidos();
    Pedidos findByID_Pedido(Long id);
    void savePedidos(Pedidos pedidos);
    void deleteByID_Pedido(Long id);

}
