package com.api.diversity.infrastructure.controller;

import org.springframework.web.bind.annotation.*;

import com.api.diversity.domain.model.Entrada;

@RestController
@RequestMapping("/api/entradas")
public class EntradaController {

    @GetMapping("/listar")
    public String listarEntradas() {
        return "Lista de entradas";
    }

    @GetMapping("/buscar/{id}")
    public String buscarEntrada(@PathVariable Integer id) {
        return "" + id;
    }

    @PostMapping("/crear")
    public String crearEntrada(@RequestBody Entrada entrada) {
        return "";
    }

    @PutMapping("/actualizar/{id}")
    public String actualizarEntrada(@PathVariable Integer id, @RequestBody Entrada entrada) {
        return "" + id;
    }

    @DeleteMapping("/eliminar/{id}")
    public String eliminarEntrada(@PathVariable Integer id) {
        return "" + id;
    }

    @GetMapping("/detalles/{id}")
    public String listarDetallesEntrada(@PathVariable Integer id) {
        return "" + id;
    }
}