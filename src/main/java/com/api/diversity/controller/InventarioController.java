package com.api.diversity.controller;

import com.api.diversity.model.Inventario;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventario")
public class InventarioController {

    @GetMapping("/listar")
    public String listarInventario() {
        return "Lista de inventario";
    }

    @GetMapping("/buscar/{id}")
    public String buscarInventario(@PathVariable Integer id) {
        return "" + id;
    }

    @GetMapping("/buscar/producto/{idProducto}")
    public String buscarInventarioPorProducto(@PathVariable String idProducto) {
        return " " + idProducto;
    }

    @PostMapping("/crear")
    public String crearInventario(@RequestBody Inventario inventario) {
        return "";
    }

    @PutMapping("/actualizar/{id}")
    public String actualizarInventario(@PathVariable Integer id, @RequestBody Inventario inventario) {
        return "" + id;
    }

    @DeleteMapping("/eliminar/{id}")
    public String eliminarInventario(@PathVariable Integer id) {
        return "" + id;
    }
}