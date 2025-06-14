package com.api.diversity.controller;

import com.api.diversity.model.Producto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private static List<Producto> productos = new ArrayList<Producto>();

    @GetMapping("/listar")
    public List<Producto> listarProductos() {
        return productos;
    }

    @GetMapping("/buscar/{id}")
    public Producto buscarProducto(@PathVariable Integer id) {
        return productos.stream()
                .filter(producto -> producto.getIdProducto().equals(id))
                .findFirst()
                .orElse(null);
    }

    @PostMapping("/crear")
    public Producto crearProducto(@RequestBody Producto producto) {
        producto.setIdProducto(productos.size() + 1);
        productos.add(producto);
        return producto;
    }

    @PutMapping("/actualizar/{id}")
    public Producto actualizarProducto(@PathVariable Integer id, @RequestBody Producto producto) {
        Producto productoActual = buscarProducto(id);
        if (productoActual != null) {
            productoActual.setNombre(producto.getNombre());
            productoActual.setDescripcion(producto.getDescripcion());
            productoActual.setCategoriaId(producto.getCategoriaId());
            productoActual.setStock(producto.getStock());
            productoActual.setPrecioUnitario(producto.getPrecioUnitario());
            productoActual.setFechaRegistro(producto.getFechaRegistro());
        }
        return productoActual;
    }

    @DeleteMapping("/eliminar/{id}")
    public Producto eliminarProducto(@PathVariable Integer id) {
        Producto productoActual = buscarProducto(id);
        if (productoActual != null) {
            productos.remove(productoActual);
        }
        return productoActual;
    }
}