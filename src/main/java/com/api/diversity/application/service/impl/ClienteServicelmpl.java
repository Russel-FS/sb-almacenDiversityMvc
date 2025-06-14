package com.api.diversity.application.service.impl;

import com.api.diversity.application.service.interfaces.ClienteService;
import com.api.diversity.domain.model.Cliente;
import com.api.diversity.infrastructure.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
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
        return clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado por id: " + id));
    }

    @Override
    public void deleteClienteById(Integer id) { 
        this.clienteRepository.deleteById(id);
    }
}