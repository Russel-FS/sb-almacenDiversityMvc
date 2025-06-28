package com.api.diversity.infrastructure.controller.kardex;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/admin/kardex")
@RequiredArgsConstructor
@Slf4j
public class AdminKardexController {

    /**
     * Dashboard principal del Kardex para administración
     */
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        log.info("Accediendo al dashboard de administración del Kardex");

        // TODO: Agregar lógica para obtener estadísticas generales del inventario
        // - Total de productos por rubro
        // - Productos con stock bajo
        // - Últimos movimientos de todos los rubros
        // - Valor total del inventario por rubro

        model.addAttribute("titulo", "Dashboard Kardex - Administración");
        model.addAttribute("subtitulo", "Resumen general del inventario");

        return "kardex/admin/dashboard/index";
    }

    /**
     * Reporte general de inventario
     */
    @GetMapping("/reporte")
    public String reporteInventario(Model model) {
        log.info("Accediendo al reporte general de inventario");

        // TODO: Agregar lógica para generar reportes generales:
        // - Stock actual por rubro y producto
        // - Productos con stock bajo por rubro
        // - Movimientos por período y rubro
        // - Valorización del inventario por rubro

        model.addAttribute("titulo", "Reporte General de Inventario");
        model.addAttribute("subtitulo", "Análisis y estadísticas por rubro");

        return "kardex/admin/reporte/index";
    }

    /**
     * Configuración del Kardex
     */
    @GetMapping("/configuracion")
    public String configuracion(Model model) {
        log.info("Accediendo a la configuración del Kardex");

        // TODO: Agregar lógica para configuración:
        // - Parámetros de stock mínimo
        // - Configuración de alertas
        // - Tipos de entrada/salida
        // - Configuración de reportes

        model.addAttribute("titulo", "Configuración Kardex");
        model.addAttribute("subtitulo", "Parámetros y configuración del sistema");

        return "kardex/admin/configuracion/index";
    }

    /**
     * Auditoría de movimientos
     */
    @GetMapping("/auditoria")
    public String auditoria(Model model) {
        log.info("Accediendo a la auditoría de movimientos");

        // TODO: Agregar lógica para auditoría:
        // - Log de todos los movimientos
        // - Usuarios que realizaron movimientos
        // - Filtros por fecha, usuario, tipo
        // - Exportación de logs

        model.addAttribute("titulo", "Auditoría de Movimientos");
        model.addAttribute("subtitulo", "Registro detallado de todas las operaciones");

        return "kardex/admin/auditoria/index";
    }
}