package com.api.diversity.infrastructure.controller;


import com.api.diversity.domain.model.Cliente;
import com.api.diversity.application.service.interfaces.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping("/")
    public String listarClientes(Model model) {
        model.addAttribute("listaClientes", clienteService.getAllClientes());
        return "index_clientes";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioDeRegistro(Model model) {
        Cliente cliente = new Cliente();
        model.addAttribute("cliente", cliente);
        return "registrar_cliente";
    }

    @PostMapping("/guardar")
    public String guardarCliente(@ModelAttribute("cliente") Cliente cliente) {
        clienteService.saveCliente(cliente);
        return "redirect:/";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioDeEdicion(@PathVariable Long id, Model model) {
        model.addAttribute("cliente", clienteService.getClienteById(id));
        return "actualizar_cliente";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarCliente(@PathVariable Long id) {
        clienteService.deleteClienteById(id);
        return "redirect:/";
    }
}