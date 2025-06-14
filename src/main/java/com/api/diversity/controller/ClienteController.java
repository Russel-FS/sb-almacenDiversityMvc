package com.api.diversity.controller;

import com.api.diversity.model.Cliente;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private static List<Cliente> clientes = new ArrayList<>();

    @GetMapping("/listar")
    public List<Cliente> listarClientes() {
        return clientes;
    }

    @GetMapping("/buscar/{id}")
    public Cliente buscarCliente(@PathVariable Integer id) {
        return clientes.stream()
                .filter(cliente -> cliente.getIdCliente().equals(id))
                .findFirst()
                .orElse(null);
    }

    @PostMapping("/crear")
    public Cliente crearCliente(@RequestBody Cliente cliente) {
        cliente.setIdCliente(clientes.size() + 1);
        clientes.add(cliente);
        return cliente;
    }

    @PutMapping("/actualizar/{id}")
    public Cliente actualizarCliente(@PathVariable Integer id, @RequestBody Cliente cliente) {
        Cliente clienteActual = buscarCliente(id);
        if (clienteActual != null) {
            clienteActual.setNombre(cliente.getNombre());
            clienteActual.setTelefono(cliente.getTelefono());
            clienteActual.setDireccion(cliente.getDireccion());
        }
        return clienteActual;
    }

    @DeleteMapping("/eliminar/{id}")
    public Cliente eliminarCliente(@PathVariable Integer id) {
        Cliente clienteActual = buscarCliente(id);
        if (clienteActual != null) {
            clientes.remove(clienteActual);
        }
        return clienteActual;
    }
}