package com.api.diversity.infrastructure.controller.kardex;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/camaras/kardex")
@RequiredArgsConstructor
@Slf4j
public class CamarasKardexController {

    /**
     * Dashboard del Kardex para Cámaras
     */
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        log.info("Accediendo al dashboard del Kardex - Cámaras");

        // TODO: Agregar lógica para obtener estadísticas específicas de Cámaras
        // - Total de productos de cámaras
        // - Productos con stock bajo
        // - Últimos movimientos de cámaras
        // - Valor total del inventario de cámaras

        model.addAttribute("titulo", "Dashboard Kardex - Cámaras");
        model.addAttribute("subtitulo", "Resumen del inventario de Cámaras");
        model.addAttribute("rubro", "Cámaras");

        return "kardex/camaras/dashboard/index";
    }

    /**
     * Nueva entrada para Cámaras
     */
    @GetMapping("/entrada/nueva")
    public String nuevaEntrada(Model model) {
        log.info("Accediendo al formulario de nueva entrada - Cámaras");

        // TODO: Agregar lógica para cargar:
        // - Lista de productos de cámaras
        // - Lista de proveedores de cámaras
        // - Tipos de entrada específicos

        model.addAttribute("titulo", "Nueva Entrada - Cámaras");
        model.addAttribute("subtitulo", "Registrar entrada de productos de Cámaras");
        model.addAttribute("rubro", "Cámaras");

        return "kardex/camaras/entrada/nueva";
    }

    /**
     * Nueva salida para Cámaras
     */
    @GetMapping("/salida/nueva")
    public String nuevaSalida(Model model) {
        log.info("Accediendo al formulario de nueva salida - Cámaras");

        // TODO: Agregar lógica para cargar:
        // - Lista de productos de cámaras con stock
        // - Lista de clientes
        // - Tipos de salida específicos

        model.addAttribute("titulo", "Nueva Salida - Cámaras");
        model.addAttribute("subtitulo", "Registrar salida de productos de Cámaras");
        model.addAttribute("rubro", "Cámaras");

        return "kardex/camaras/salida/nueva";
    }

    /**
     * Movimientos de Cámaras
     */
    @GetMapping("/movimientos")
    public String movimientos(Model model) {
        log.info("Accediendo a la lista de movimientos - Cámaras");

        // TODO: Agregar lógica para cargar:
        // - Lista paginada de movimientos de cámaras
        // - Filtros por fecha, tipo, producto
        // - Estadísticas específicas de cámaras

        model.addAttribute("titulo", "Movimientos - Cámaras");
        model.addAttribute("subtitulo", "Historial de movimientos de Cámaras");
        model.addAttribute("rubro", "Cámaras");

        return "kardex/camaras/movimiento/lista";
    }

    /**
     * Detalle de movimiento de Cámaras
     */
    @GetMapping("/movimientos/{id}")
    public String detalleMovimiento(Long id, Model model) {
        log.info("Accediendo al detalle del movimiento ID: {} - Cámaras", id);

        // TODO: Agregar lógica para cargar:
        // - Detalles del movimiento de cámaras
        // - Lista de productos involucrados
        // - Información del proveedor/cliente

        model.addAttribute("titulo", "Detalle de Movimiento - Cámaras");
        model.addAttribute("subtitulo", "Información detallada del movimiento");
        model.addAttribute("movimientoId", id);
        model.addAttribute("rubro", "Cámaras");

        return "kardex/camaras/movimiento/detalle";
    }

    /**
     * Reporte de inventario de Cámaras
     */
    @GetMapping("/reporte")
    public String reporteInventario(Model model) {
        log.info("Accediendo al reporte de inventario - Cámaras");

        // TODO: Agregar lógica para generar reportes de cámaras:
        // - Stock actual por producto de cámaras
        // - Productos con stock bajo
        // - Movimientos por período
        // - Valorización del inventario de cámaras

        model.addAttribute("titulo", "Reporte de Inventario - Cámaras");
        model.addAttribute("subtitulo", "Análisis y estadísticas de Cámaras");
        model.addAttribute("rubro", "Cámaras");

        return "kardex/camaras/reporte/index";
    }
}