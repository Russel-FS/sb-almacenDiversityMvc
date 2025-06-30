package com.api.diversity.infrastructure.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.api.diversity.application.dto.ClienteDto;
import com.api.diversity.application.dto.EntradaDto;
import com.api.diversity.application.dto.ProductoDto;
import com.api.diversity.application.dto.ProveedorDto;
import com.api.diversity.application.dto.SalidaDto;
import com.api.diversity.application.service.interfaces.IClienteService;
import com.api.diversity.application.service.interfaces.IEntradaService;
import com.api.diversity.application.service.interfaces.IProductoService;
import com.api.diversity.application.service.interfaces.IProveedorService;
import com.api.diversity.application.service.interfaces.ISalidaService;
import com.api.diversity.domain.enums.TipoRubro;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class HomeController {

    private final IProductoService productoService;
    private final IClienteService clienteService;
    private final IProveedorService proveedorService;
    private final IEntradaService entradaService;
    private final ISalidaService salidaService;

    @GetMapping("/")
    public String home(Model model) {
        log.info("Accediendo a la página principal del sistema");

        try {
            // Obtener productos por rubro
            List<ProductoDto> productosPinateria = productoService.findAllByRubro(TipoRubro.PIÑATERIA);
            List<ProductoDto> productosLibreria = productoService.findAllByRubro(TipoRubro.LIBRERIA);
            List<ProductoDto> productosCamaras = productoService.findAllByRubro(TipoRubro.CAMARA_SEGURIDAD);

            // Calcular estadísticas generales
            int totalProductos = productosPinateria.size() + productosLibreria.size() + productosCamaras.size();

            // Productos con stock bajo
            int stockBajoPinateria = (int) productosPinateria.stream()
                    .filter(p -> p.getStockActual() != null && p.getStockActual() < 10)
                    .count();
            int stockBajoLibreria = (int) productosLibreria.stream()
                    .filter(p -> p.getStockActual() != null && p.getStockActual() < 10)
                    .count();
            int stockBajoCamaras = (int) productosCamaras.stream()
                    .filter(p -> p.getStockActual() != null && p.getStockActual() < 10)
                    .count();
            int totalStockBajo = stockBajoPinateria + stockBajoLibreria + stockBajoCamaras;

            // Valor total del inventario por rubro
            BigDecimal valorPinateria = productosPinateria.stream()
                    .filter(p -> p.getStockActual() != null && p.getPrecioVenta() != null)
                    .map(p -> p.getPrecioVenta().multiply(BigDecimal.valueOf(p.getStockActual())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal valorLibreria = productosLibreria.stream()
                    .filter(p -> p.getStockActual() != null && p.getPrecioVenta() != null)
                    .map(p -> p.getPrecioVenta().multiply(BigDecimal.valueOf(p.getStockActual())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal valorCamaras = productosCamaras.stream()
                    .filter(p -> p.getStockActual() != null && p.getPrecioVenta() != null)
                    .map(p -> p.getPrecioVenta().multiply(BigDecimal.valueOf(p.getStockActual())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal valorTotalInventario = valorPinateria.add(valorLibreria).add(valorCamaras);

            // Obtener estadísticas de clientes y proveedores
            List<ClienteDto> clientes = clienteService.findAll();
            List<ProveedorDto> proveedores = proveedorService.findAll();

            // Obtener movimientos recientes
            List<EntradaDto> ultimasEntradas = entradaService.findTop10ByOrderByFechaEntradaDesc()
                    .stream().limit(3).toList();
            List<SalidaDto> ultimasSalidas = salidaService.findTop10ByOrderByFechaSalidaDesc()
                    .stream().limit(3).toList();

            model.addAttribute("totalProductos", totalProductos);
            model.addAttribute("totalStockBajo", totalStockBajo);
            model.addAttribute("valorTotalInventario", valorTotalInventario);
            model.addAttribute("totalClientes", clientes.size());
            model.addAttribute("totalProveedores", proveedores.size());

            // Estadísticas por rubro
            model.addAttribute("productosPinateria", productosPinateria.size());
            model.addAttribute("productosLibreria", productosLibreria.size());
            model.addAttribute("productosCamaras", productosCamaras.size());
            model.addAttribute("stockBajoPinateria", stockBajoPinateria);
            model.addAttribute("stockBajoLibreria", stockBajoLibreria);
            model.addAttribute("stockBajoCamaras", stockBajoCamaras);
            model.addAttribute("valorPinateria", valorPinateria);
            model.addAttribute("valorLibreria", valorLibreria);
            model.addAttribute("valorCamaras", valorCamaras);

            // Movimientos recientes
            model.addAttribute("ultimasEntradas", ultimasEntradas);
            model.addAttribute("ultimasSalidas", ultimasSalidas);

        } catch (Exception e) {
            log.error("Error al cargar estadísticas del dashboard: {}", e.getMessage());

            // Valores por defecto en caso de error
            model.addAttribute("totalProductos", 0);
            model.addAttribute("totalStockBajo", 0);
            model.addAttribute("valorTotalInventario", BigDecimal.ZERO);
            model.addAttribute("totalClientes", 0);
            model.addAttribute("totalProveedores", 0);
            model.addAttribute("productosPinateria", 0);
            model.addAttribute("productosLibreria", 0);
            model.addAttribute("productosCamaras", 0);
            model.addAttribute("stockBajoPinateria", 0);
            model.addAttribute("stockBajoLibreria", 0);
            model.addAttribute("stockBajoCamaras", 0);
            model.addAttribute("valorPinateria", BigDecimal.ZERO);
            model.addAttribute("valorLibreria", BigDecimal.ZERO);
            model.addAttribute("valorCamaras", BigDecimal.ZERO);
            model.addAttribute("ultimasEntradas", List.of());
            model.addAttribute("ultimasSalidas", List.of());
        }

        return "home/home";
    }
}
