package com.api.diversity.infrastructure.service;

import java.util.List;

import com.api.diversity.domain.model.DetallePedido;

public interface DetallePedidoService {

List <DetallePedido> getAllDetallesPedidos();
DetallePedido findDetallePedidoById(Integer id);
void createDetallePedido(DetallePedido detallePedido);
void deleteDetallePedidoById(Integer id);
        

}
