package com.api.diversity.controller;

import com.api.diversity.model.Salida;
import com.api.diversity.model.DetalleSalida;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/salidas")
public class SalidaController {

    private static List<Salida> salidas = new ArrayList<>();
    private static List<DetalleSalida> detalles = new ArrayList<>();

    // --- Métodos para Salida ---

    @GetMapping("/listar")
    public List<Salida> listarSalidas() {
        return salidas;
    }

    @GetMapping("/buscar/{id}")
    public Salida buscarSalida(@PathVariable Integer id) {
        return salidas.stream()
                .filter(salida -> salida.getIdSalida().equals(id))
                .findFirst()
                .orElse(null);
    }

    @PostMapping("/crear")
    public Salida crearSalida(@RequestBody Salida salida) {
        salida.setIdSalida(salidas.size() + 1);
        salidas.add(salida);
        return salida;
    }

    @PutMapping("/actualizar/{id}")
    public Salida actualizarSalida(@PathVariable Integer id, @RequestBody Salida salida) {
        Salida salidaActual = buscarSalida(id);
        if (salidaActual != null) {
            salidaActual.setFechaSalida(salida.getFechaSalida());
            salidaActual.setMotivoSalida(salida.getMotivoSalida());
        }
        return salidaActual;
    }

    @DeleteMapping("/eliminar/{id}")
    public String eliminarSalida(@PathVariable Integer id) {
        Salida salidaActual = buscarSalida(id);
        if (salidaActual != null) {
            salidas.remove(salidaActual);
            return "Salida eliminada correctamente.";
        }
        return "No se encontró la salida con el ID proporcionado.";
    }

    // --- Métodos para DetalleSalida ---

    @GetMapping("/detalles/listar")
    public List<DetalleSalida> listarDetalles() {
        return detalles;
    }

    @GetMapping("/detalles/buscar/{id}")
    public DetalleSalida buscarDetalle(@PathVariable Integer id) {
        return detalles.stream()
                .filter(detalle -> detalle.getIdDetalleSalida().equals(id))
                .findFirst()
                .orElse(null);
    }

    @PostMapping("/detalles/crear")
    public DetalleSalida crearDetalle(@RequestBody DetalleSalida detalle) {
        detalle.setIdDetalleSalida(detalles.size() + 1);
        detalles.add(detalle);
        return detalle;
    }

    @PutMapping("/detalles/actualizar/{id}")
    public DetalleSalida actualizarDetalle(@PathVariable Integer id, @RequestBody DetalleSalida detalle) {
        DetalleSalida detalleActual = buscarDetalle(id);
        if (detalleActual != null) {
            detalleActual.setSalidaId(detalle.getSalidaId());
            detalleActual.setProductoId(detalle.getProductoId());
            detalleActual.setCantidad(detalle.getCantidad());
            detalleActual.setSubtotal(detalle.getSubtotal());
        }
        return detalleActual;
    }

    @DeleteMapping("/detalles/eliminar/{id}")
    public String eliminarDetalle(@PathVariable Integer id) {
        DetalleSalida detalleActual = buscarDetalle(id);
        if (detalleActual != null) {
            detalles.remove(detalleActual);
            return "Detalle de salida eliminado correctamente.";
        }
        return "No se encontró el detalle de salida con el ID proporcionado.";
    }
}