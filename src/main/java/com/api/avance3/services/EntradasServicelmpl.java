package com.example.avance3.services;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.avance3.model.Entradas;
import com.example.avance3.repository.EntradasRepository;

@Service
public class EntradasServicelmpl implements EntradaService {

    @Autowired
    private EntradasRepository entradaRepository;

    @Override
    public List<Entradas> getAllEntradas() {
        return entradaRepository.findAll();
    }

    @Override
    public void saveEntrada(Entradas entrada) {
        entradaRepository.save(entrada);
    }

    @Override
    public Entradas getEntradaById(long id) {
        Optional<Entradas> optional = entradaRepository.findById(id);
        return optional.orElseThrow(() -> new RuntimeException("Entrada no encontrada por id: " + id));
    }

    @Override
    public void deleteEntradaById(long id) {
        entradaRepository.deleteById(id);
    }
}
