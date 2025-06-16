/*package com.example.AVANCE.services;

import java.util.List;
import java.util.Optional;

// importamos las anotaciones y servicios
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// importamos el modelo y el repositorio
import com.example.AVANCE.model.DetalleEntrada;
import com.example.AVANCE.repository.DetalleEntradaRepository;

// declaramos la clase como un servicio
@Service
public class DetalleEntradaServiceImpl implements DetalleEntradaService {

     @Autowired
    private DetalleEntradaRepository repository;

    @Override
    public List<DetalleEntrada> getAllDetalles() {
        return repository.findAll();
    }

    @Override
    public void saveDetalle(DetalleEntrada detalleEntrada) {
        repository.save(detalleEntrada);
    }

    @Override
    public DetalleEntrada getDetalleById(Long id) {
        Optional<DetalleEntrada> optional = repository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        } else {
            throw new RuntimeException("DetalleEntrada no encontrado con ID: " + id);
        }
    }

    @Override
    public void deleteDetalleById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public List<DetalleEntrada> getDetallesByEntradaId(Long entradaId) {
        return repository.findByEntradaId(entradaId);
    }
}*/