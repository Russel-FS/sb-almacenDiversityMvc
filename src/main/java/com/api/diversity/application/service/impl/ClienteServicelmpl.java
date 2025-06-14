package com.api.diversity.application.service.impl;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.diversity.application.service.interfaces.ClienteService;
import com.api.diversity.domain.model.Cliente;
import com.api.diversity.domain.model.Usuario;

import com.api.diversity.infrastructure.repository.ClienteRepository;

public class ClienteServicelmpl implements ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Override
    public List<Cliente> getAllClientes() {
        return clienteRepository.findAll();
    }

    @Override
    public void saveCliente(Cliente cliente) {
        this.clienteRepository.save(cliente);
    }

    @Override
    public Cliente getClienteById(Integer id) {
        Optional<Cliente> optional = clienteRepository.findById(id);
        Cliente cliente;
        if (optional.isPresent()) {
            cliente = optional.get();
        } else {
            throw new RuntimeException("Cliente no encontrado por id: " + id);
        }
        return cliente;
    }

    @Override
    public void deleteClienteById(Integer id) {
        this.clienteRepository.deleteById(id);
    }
}