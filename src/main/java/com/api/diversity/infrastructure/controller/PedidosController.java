package com.api.diversity.infrastructure.controller;

import com.api.diversity.infrastructure.service.PedidosService;
import com.api.diversity.domain.model.Pedidos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/pedidos")
public class PedidosController {

    @Autowired
    private PedidosService pedidosService;

    @GetMapping
    public String listarPedidos(Model model) {
        List<Pedidos> pedidos = pedidosService.findAllPedidos();
        model.addAttribute("pedidos", pedidos);
        return "pedidos/lista"; // minúsculas
    }

    @GetMapping("/detalle/{id}")
    public String verPedido(@PathVariable Long id, Model model) {
        Pedidos pedido = pedidosService.findByID_Pedido(id);
        model.addAttribute("pedido", pedido);
        return "pedidos/detalle"; // minúsculas
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("pedido", new Pedidos());
        return "pedidos/formulario"; // minúsculas
    }

    @PostMapping("/guardar")
    public String guardarPedido(@ModelAttribute Pedidos pedido) {
        pedidosService.savePedidos(pedido);
        return "redirect:/pedidos";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarPedido(@PathVariable Long id) {
        pedidosService.deleteByID_Pedido(id);
        return "redirect:/pedidos";
    }
}
