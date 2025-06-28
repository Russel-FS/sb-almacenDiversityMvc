package com.api.diversity.infrastructure.controller.kardex;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.math.BigDecimal;

@Controller
@RequestMapping("/pinateria/kardex")
@RequiredArgsConstructor
@Slf4j
public class PinateriaKardexController {

    /**
     * Dashboard del Kardex para Piñatería
     */
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        log.info("Accediendo al dashboard del Kardex - Piñatería");

        model.addAttribute("titulo", "Dashboard Kardex - Piñatería");
        model.addAttribute("subtitulo", "Resumen del inventario de Piñatería");
        model.addAttribute("rubro", "Piñatería");

        // Variables para las estadísticas (valores por defecto)
        model.addAttribute("totalProductos", 0);
        model.addAttribute("productosStockBajo", 0);
        model.addAttribute("valorTotalInventario", BigDecimal.ZERO);
        model.addAttribute("ultimasEntradas", new ArrayList<>());
        model.addAttribute("ultimasSalidas", new ArrayList<>());

        return "kardex/pinateria/dashboard/index";
    }

    /**
     * Nueva entrada para Piñatería
     */
    @GetMapping("/entrada/nueva")
    public String nuevaEntrada(Model model) {
        log.info("Accediendo al formulario de nueva entrada - Piñatería");

        // TODO: Agregar lógica para cargar:
        // - Lista de productos de piñatería
        // - Lista de proveedores de piñatería
        // - Tipos de entrada específicos

        model.addAttribute("titulo", "Nueva Entrada - Piñatería");
        model.addAttribute("subtitulo", "Registrar entrada de productos de Piñatería");
        model.addAttribute("rubro", "Piñatería");

        return "kardex/pinateria/entrada/nueva";
    }

    /**
     * Nueva salida para Piñatería
     */
    @GetMapping("/salida/nueva")
    public String nuevaSalida(Model model) {
        log.info("Accediendo al formulario de nueva salida - Piñatería");

        // TODO: Agregar lógica para cargar:
        // - Lista de productos de piñatería con stock
        // - Lista de clientes
        // - Tipos de salida específicos

        model.addAttribute("titulo", "Nueva Salida - Piñatería");
        model.addAttribute("subtitulo", "Registrar salida de productos de Piñatería");
        model.addAttribute("rubro", "Piñatería");

        return "kardex/pinateria/salida/nueva";
    }

    /**
     * Movimientos de Piñatería
     */
    @GetMapping("/movimientos")
    public String movimientos(Model model) {
        log.info("Accediendo a la lista de movimientos - Piñatería");

        // TODO: Agregar lógica para cargar:
        // - Lista paginada de movimientos de piñatería
        // - Filtros por fecha, tipo, producto
        // - Estadísticas específicas de piñatería

        model.addAttribute("titulo", "Movimientos - Piñatería");
        model.addAttribute("subtitulo", "Historial de movimientos de Piñatería");
        model.addAttribute("rubro", "Piñatería");

        return "kardex/pinateria/movimiento/lista";
    }

    /**
     * Detalle de movimiento de Piñatería
     */
    @GetMapping("/movimientos/{id}")
    public String detalleMovimiento(Long id, Model model) {
        log.info("Accediendo al detalle del movimiento ID: {} - Piñatería", id);

        // TODO: Agregar lógica para cargar:
        // - Detalles del movimiento de piñatería
        // - Lista de productos involucrados
        // - Información del proveedor/cliente

        model.addAttribute("titulo", "Detalle de Movimiento - Piñatería");
        model.addAttribute("subtitulo", "Información detallada del movimiento");
        model.addAttribute("movimientoId", id);
        model.addAttribute("rubro", "Piñatería");

        return "kardex/pinateria/movimiento/detalle";
    }

    /**
     * Reporte de inventario de Piñatería
     */
    @GetMapping("/reporte")
    public String reporteInventario(Model model) {
        log.info("Accediendo al reporte de inventario - Piñatería");

        // TODO: Agregar lógica para generar reportes de piñatería:
        // - Stock actual por producto de piñatería
        // - Productos con stock bajo
        // - Movimientos por período
        // - Valorización del inventario de piñatería

        model.addAttribute("titulo", "Reporte de Inventario - Piñatería");
        model.addAttribute("subtitulo", "Análisis y estadísticas de Piñatería");
        model.addAttribute("rubro", "Piñatería");

        return "kardex/pinateria/reporte/index";
    }
}