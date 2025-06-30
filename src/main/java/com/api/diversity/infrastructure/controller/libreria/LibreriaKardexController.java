package com.api.diversity.infrastructure.controller.libreria;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/libreria/kardex")
@RequiredArgsConstructor
@Slf4j
public class LibreriaKardexController {

    /**
     * Dashboard del Kardex para Librería
     */
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        log.info("Accediendo al dashboard del Kardex - Librería");

        // TODO: Agregar lógica para obtener estadísticas específicas de Librería
        // - Total de productos de librería
        // - Productos con stock bajo
        // - Últimos movimientos de librería
        // - Valor total del inventario de librería

        model.addAttribute("titulo", "Dashboard Kardex - Librería");
        model.addAttribute("subtitulo", "Resumen del inventario de Librería");
        model.addAttribute("rubro", "Librería");

        return "kardex/libreria/dashboard/index";
    }

    /**
     * Nueva entrada para Librería
     */
    @GetMapping("/entrada/nueva")
    public String nuevaEntrada(Model model) {
        log.info("Accediendo al formulario de nueva entrada - Librería");

        // TODO: Agregar lógica para cargar:
        // - Lista de productos de librería
        // - Lista de proveedores de librería
        // - Tipos de entrada específicos

        model.addAttribute("titulo", "Nueva Entrada - Librería");
        model.addAttribute("subtitulo", "Registrar entrada de productos de Librería");
        model.addAttribute("rubro", "Librería");

        return "kardex/libreria/entrada/nueva";
    }

    /**
     * Nueva salida para Librería
     */
    @GetMapping("/salida/nueva")
    public String nuevaSalida(Model model) {
        log.info("Accediendo al formulario de nueva salida - Librería");

        // TODO: Agregar lógica para cargar:
        // - Lista de productos de librería con stock
        // - Lista de clientes
        // - Tipos de salida específicos

        model.addAttribute("titulo", "Nueva Salida - Librería");
        model.addAttribute("subtitulo", "Registrar salida de productos de Librería");
        model.addAttribute("rubro", "Librería");

        return "kardex/libreria/salida/nueva";
    }

    /**
     * Movimientos de Librería
     */
    @GetMapping("/movimientos")
    public String movimientos(Model model) {
        log.info("Accediendo a la lista de movimientos - Librería");

        // TODO: Agregar lógica para cargar:
        // - Lista paginada de movimientos de librería
        // - Filtros por fecha, tipo, producto
        // - Estadísticas específicas de librería

        model.addAttribute("titulo", "Movimientos - Librería");
        model.addAttribute("subtitulo", "Historial de movimientos de Librería");
        model.addAttribute("rubro", "Librería");

        return "kardex/libreria/movimiento/lista";
    }

    /**
     * Detalle de movimiento de Librería
     */
    @GetMapping("/movimientos/{id}")
    public String detalleMovimiento(Long id, Model model) {
        log.info("Accediendo al detalle del movimiento ID: {} - Librería", id);

        // TODO: Agregar lógica para cargar:
        // - Detalles del movimiento de librería
        // - Lista de productos involucrados
        // - Información del proveedor/cliente

        model.addAttribute("titulo", "Detalle de Movimiento - Librería");
        model.addAttribute("subtitulo", "Información detallada del movimiento");
        model.addAttribute("movimientoId", id);
        model.addAttribute("rubro", "Librería");

        return "kardex/libreria/movimiento/detalle";
    }

    /**
     * Reporte de inventario de Librería
     */
    @GetMapping("/reporte")
    public String reporteInventario(Model model) {
        log.info("Accediendo al reporte de inventario - Librería");

        // TODO: Agregar lógica para generar reportes de librería:
        // - Stock actual por producto de librería
        // - Productos con stock bajo
        // - Movimientos por período
        // - Valorización del inventario de librería

        model.addAttribute("titulo", "Reporte de Inventario - Librería");
        model.addAttribute("subtitulo", "Análisis y estadísticas de Librería");
        model.addAttribute("rubro", "Librería");

        return "kardex/libreria/reporte/index";
    }
}