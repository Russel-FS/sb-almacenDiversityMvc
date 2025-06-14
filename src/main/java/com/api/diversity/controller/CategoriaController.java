package com.api.diversity.controller;

import com.api.diversity.model.Categoria;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    private static List<Categoria> categorias = new ArrayList<Categoria>();

    @GetMapping("/listar")
    public List<Categoria> listarCategorias() {
        return categorias;
    }

    @GetMapping("/buscar/{id}")
    public Categoria buscarCategoria(@PathVariable Integer id) {
        return categorias.stream()
                .filter(categoria -> categoria.getIdCategoria() == id)
                .findFirst()
                .orElse(null);
    }

    @PostMapping("/crear")
    public Categoria crearCategoria(@RequestBody Categoria categoria) {
        categoria.setIdCategoria(categorias.size() + 1);
        categorias.add(categoria);
        return categoria;
    }

    @PutMapping("/actualizar/{id}")
    public Categoria actualizarCategoria(@PathVariable Integer id, @RequestBody Categoria categoria) {
        Categoria categoriaActual = buscarCategoria(id);
        if (categoriaActual != null) {
            categoriaActual.setNombreCategoria(categoria.getNombreCategoria());
            categoriaActual.setDescripcion(categoria.getDescripcion());
        }
        return categoriaActual;
    }

    @DeleteMapping("/eliminar/{id}")
    public Categoria eliminarCategoria(@PathVariable Integer id) {
        Categoria categoriaActual = buscarCategoria(id);
        if (categoriaActual != null) {
            categorias.remove(categoriaActual);
        }
        return categoriaActual;
    }
}