package com.api.diversity.infrastructure.service.implementation;

import com.api.diversity.infrastructure.repository.DetallePedidoRepository;
import com.api.diversity.infrastructure.service.DetallePedidoService;
import com.api.diversity.domain.model.DetallePedido;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DetallePedidoServiceImpl implements DetallePedidoService {

    @Autowired
    private DetallePedidoRepository detallePedidoRepository;

    @Override
    public List<DetallePedido> getAllDetallesPedidos() {
        return detallePedidoRepository.findAll();
    }

    @Override
    public DetallePedido findDetallePedidoById(Integer id) {
        if (id == null) {
            System.out.println("No se proporcionó un ID para buscar el detalle de pedido.");
            return null;
        }
        Optional<DetallePedido> detalle = detallePedidoRepository.findById(id);
        return detalle.orElse(null);
    }

    @Override
    public void createDetallePedido(DetallePedido detallePedido) {
        if (detallePedido == null) {
            throw new IllegalArgumentException("El detalle de pedido no puede ser nulo.");
        }
        this.detallePedidoRepository.save(detallePedido);
    }

    @Override
    public void deleteDetallePedidoById(Integer id) {
        if (id == null) {
            System.out.println("No se proporcionó un ID para eliminar el detalle de pedido.");
            return;
        }
        this.detallePedidoRepository.deleteById(id);
    }
}
