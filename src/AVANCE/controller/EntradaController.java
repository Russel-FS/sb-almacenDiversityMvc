package com.example.AVANCE.controller;

import com.example.AVANCE.model.Entrada;
import com.example.AVANCE.repository.EntradaRepository;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/entradas")
@CrossOrigin(origins = "*")
public class EntradaController {

   private final EntradaRepository entradaRepository;

    public EntradaController(EntradaRepository entradaRepository) {
        this.entradaRepository = entradaRepository;
    }

    @GetMapping
    public List<Entrada> listarEntradas() {
        return entradaRepository.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Entrada> obtenerEntrada(@PathVariable Long id) {
        return entradaRepository.findById(id);
    }

    @PostMapping
    public Entrada registrarEntrada(@RequestBody Entrada entrada) {
        return entradaRepository.save(entrada);
    }

    @PutMapping("/{id}")
    public Entrada actualizarEntrada(@PathVariable Long id, @RequestBody Entrada nuevaEntrada) {
        return entradaRepository.findById(id).map(e -> {
            e.setNumeroFactura(nuevaEntrada.getNumeroFactura());
            e.setIdProveedor(nuevaEntrada.getIdProveedor());
            e.setFechaEntrada(nuevaEntrada.getFechaEntrada());
            e.setCostoTotal(nuevaEntrada.getCostoTotal());
            e.setEstado(nuevaEntrada.getEstado());
            e.setIdUsuarioRegistro(nuevaEntrada.getIdUsuarioRegistro());
            e.setIdUsuarioAprobacion(nuevaEntrada.getIdUsuarioAprobacion());
            e.setFechaAprobacion(nuevaEntrada.getFechaAprobacion());
            e.setObservaciones(nuevaEntrada.getObservaciones());
            return entradaRepository.save(e);
        }).orElseGet(() -> {
            nuevaEntrada.setId(id);
            return entradaRepository.save(nuevaEntrada);
        });
    }

    @DeleteMapping("/{id}")
    public void eliminarEntrada(@PathVariable Long id) {
        entradaRepository.deleteById(id);
    }
}