package com.api.diversity.infrastructure.service.implementation;

import com.api.diversity.infrastructure.repository.PedidosRepository;
import com.api.diversity.infrastructure.service.PedidosService;
import com.api.diversity.domain.model.Pedidos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PedidosServiceImpl implements PedidosService {

    @Autowired
    private PedidosRepository pedidosRepository;

    @Override
    public List<Pedidos> findAllPedidos() {
        return pedidosRepository.findAll();
    }

    @Override
    public Pedidos findByID_Pedido(Long id) {
        if (id == null) {
            System.out.println("No se proporcionó un ID para buscar el pedido.");
            return null;
        }
        Optional<Pedidos> pedido = pedidosRepository.findById(id);
        return pedido.orElse(null);
    }

    @Override
    public void savePedidos(Pedidos pedidos) {
        if (pedidos == null) {
            throw new IllegalArgumentException("El pedido no puede ser nulo.");
        }
        this.pedidosRepository.save(pedidos);
    }

    @Override
    public void deleteByID_Pedido(Long id) {
        if (id == null) {
            System.out.println("No se proporcionó un ID para eliminar el pedido.");
            return;
        }
        this.pedidosRepository.deleteById(id);
    }
}
