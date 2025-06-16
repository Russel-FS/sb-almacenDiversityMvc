package com.example.AVANCE.controller;

import com.example.AVANCE.model.DetalleEntrada;
import com.example.AVANCE.repository.DetalleEntradaRepository;

import org.springframework.web.bind.annotation.*;
import java.util.Optional;
import java.util.List;

@RestController
@RequestMapping("/api/detalle-entradas")
@CrossOrigin(origins = "*")
public class DetalleEntradaController {

   private final DetalleEntradaRepository repository;

    public DetalleEntradaController(DetalleEntradaRepository repository) {
        this.repository = repository;
    }

    // GET: Listar todos
    @GetMapping
    public List<DetalleEntrada> listar() {
        return repository.findAll();
    }

    // GET: Buscar por ID
    @GetMapping("/{id}")
    public Optional<DetalleEntrada> buscarPorId(@PathVariable Long id) {
        return repository.findById(id);
    }

    // POST: Registrar nuevo
    @PostMapping
    public DetalleEntrada registrar(@RequestBody DetalleEntrada entrada) {
        // Calculamos subtotal automáticamente
        entrada.setSubtotal(entrada.getCantidad() * entrada.getPrecioUnitario());
        return repository.save(entrada);
    }

    // PUT: Actualizar
    @PutMapping("/{id}")
    public DetalleEntrada actualizar(@PathVariable Long id, @RequestBody DetalleEntrada nuevaEntrada) {
        return repository.findById(id).map(entrada -> {
            entrada.setEntrada(nuevaEntrada.getEntrada());
            entrada.setIdProducto(nuevaEntrada.getIdProducto());
            entrada.setCantidad(nuevaEntrada.getCantidad());
            entrada.setPrecioUnitario(nuevaEntrada.getPrecioUnitario());
            entrada.setSubtotal(nuevaEntrada.getCantidad() * nuevaEntrada.getPrecioUnitario());
            entrada.setIdUsuarioRegistro(nuevaEntrada.getIdUsuarioRegistro());
            entrada.setEstado(nuevaEntrada.getEstado());
            return repository.save(entrada);
        }).orElseGet(() -> {
            nuevaEntrada.setId(id);
            nuevaEntrada.setSubtotal(nuevaEntrada.getCantidad() * nuevaEntrada.getPrecioUnitario());
            return repository.save(nuevaEntrada);
        });
    }

    // DELETE: Eliminar por ID
    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        repository.deleteById(id);
    }
}