
package com.example.avance3.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.avance3.model.DetalleEntrada;

import com.example.avance3.repository.Detalle_EntradaRepository;

@Service

public class Detalle_EntradaServicelmpl implements Detalle_EntradaService {
    
    @Autowired
    private Detalle_EntradaRepository detalleEntradaRepository;

    @Override
    public List<DetalleEntrada> getAllDetalles() {
        return detalleEntradaRepository.findAll();
    }

    @Override
    public void saveDetalle(DetalleEntrada detalleEntrada) {
        this.detalleEntradaRepository.save(detalleEntrada);
    }

    @Override
    public DetalleEntrada getDetalleById(long id) {
        Optional<DetalleEntrada> optional = detalleEntradaRepository.findById(id);
        DetalleEntrada detalle = null;
        if (optional.isPresent()) {
            detalle = optional.get();
        } else {
            throw new RuntimeException("Detalle_Entrada no encontrado por id: " + id);
        }
        return detalle;
    }

    @Override
    public void deleteDetalleById(long id) {
        this.detalleEntradaRepository.deleteById(id);
    }
}