package com.example.avance3.services;

import java.util.List;
import com.example.avance3.model.Entradas;

public interface EntradaService {
    List<Entradas> getAllEntradas();
    void saveEntrada(Entradas entrada);
    Entradas getEntradaById(long id);
    void deleteEntradaById(long id);
}
