package com.api.diversity.infrastructure.controller;

import com.api.diversity.infrastructure.service.DetallePedidoService;
import com.api.diversity.domain.model.DetallePedido;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/detalle-pedido")
public class DetallePedidoController {

    @Autowired
    private DetallePedidoService detallePedidoService;

    @GetMapping
    public String listarDetallesPedidos(Model model) {
        List<DetallePedido> detalles = detallePedidoService.getAllDetallesPedidos();
        model.addAttribute("detalles", detalles);
        return "detallePedido/list"; // Ejemplo de nombre de plantilla
    }

    @GetMapping("/{id}")
    public String verDetallePedido(@PathVariable Integer id, Model model) {
        DetallePedido detalle = detallePedidoService.findDetallePedidoById(id);
        model.addAttribute("detalle", detalle);
        return "detallePedido/view"; // Ejemplo de nombre de plantilla
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("detallePedido", new DetallePedido());
        return "detallePedido/form"; // Ejemplo de nombre de plantilla
    }

    @PostMapping
    public String crearDetallePedido(@ModelAttribute DetallePedido detallePedido) {
        detallePedidoService.createDetallePedido(detallePedido);
        return "redirect:/detalle-pedido";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarDetallePedido(@PathVariable Integer id) {
        detallePedidoService.deleteDetallePedidoById(id);
        return "redirect:/detalle-pedido";
    }
}
