package com.api.diversity.application.service.interfaces;


import com.api.diversity.domain.model.Cliente;
import java.util.List;

public interface ClienteService {

    List<Cliente> getAllClientes();

    void saveCliente(Cliente cliente);

    Cliente getClienteById(Long id);

    void deleteClienteById(Long id);
}