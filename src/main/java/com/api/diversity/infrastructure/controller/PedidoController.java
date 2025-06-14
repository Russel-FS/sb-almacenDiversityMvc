package com.api.diversity.infrastructure.controller;

import org.springframework.web.bind.annotation.*;

import com.api.diversity.domain.model.Pedido;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    @GetMapping("/listar")
    public String listarPedidos() {
        return "Lista de pedidos";
    }

    @GetMapping("/buscar/{id}")
    public String buscarPedido(@PathVariable Integer id) {
        return "" + id;
    }

    @PostMapping("/crear")
    public String crearPedido(@RequestBody Pedido pedido) {
        return "";
    }

    @PutMapping("/actualizar/{id}")
    public String actualizarPedido(@PathVariable Integer id, @RequestBody Pedido pedido) {
        return "" + id;
    }

    @DeleteMapping("/eliminar/{id}")
    public String eliminarPedido(@PathVariable Integer id) {
        return "" + id;
    }

    @GetMapping("/detalles/{id}")
    public String listarDetallesPedido(@PathVariable Integer id) {
        return "" + id;
    }
}