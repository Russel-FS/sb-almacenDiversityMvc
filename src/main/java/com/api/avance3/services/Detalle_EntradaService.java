package com.example.avance3.services;

import java.util.List;

import com.example.avance3.model.DetalleEntrada;

public interface Detalle_EntradaService {
    List<DetalleEntrada> getAllDetalles();
    void saveDetalle(DetalleEntrada detalleEntrada);
    DetalleEntrada getDetalleById(long id);
    void deleteDetalleById(long id);
}