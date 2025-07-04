package com.api.diversity.infrastructure.controller;

import com.api.diversity.application.dto.DevolucionRequestDto;
import com.api.diversity.application.dto.DetalleDevolucionDto;
import com.api.diversity.application.dto.EntradaDto;
import com.api.diversity.application.dto.SalidaDto;
import com.api.diversity.application.service.interfaces.DevolucionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/almacenero/devoluciones")
@RequiredArgsConstructor
public class DevolucionController {

    private final DevolucionService devolucionService;

    @GetMapping("/nueva")
    public String showDevolucionForm(Model model, @RequestParam(value = "idSalida", required = false) Long idSalida,
            @RequestParam(value = "numeroDocumento", required = false) String numeroDocumento) {
        DevolucionRequestDto devolucionRequest = new DevolucionRequestDto();
        model.addAttribute("devolucionRequest", devolucionRequest);

        if (idSalida != null || (numeroDocumento != null && !numeroDocumento.isEmpty())) {
            try {
                SalidaDto salida = devolucionService.buscarSalidaPorIdONumeroDocumento(idSalida, numeroDocumento);
                model.addAttribute("salidaEncontrada", true);
                model.addAttribute("salida", salida);

                // lista de productos devueltos con los productos de la salida
                List<DetalleDevolucionDto> productosDevueltos = new ArrayList<>();
                for (var detalle : salida.getDetalles()) {
                    DetalleDevolucionDto detalleDev = new DetalleDevolucionDto();
                    detalleDev.setIdProducto(detalle.getProductoId());
                    detalleDev.setPrecioUnitario(detalle.getPrecioUnitario());
                    detalleDev.setSeleccionado(false);
                    detalleDev.setCantidadDevuelta(0);
                    productosDevueltos.add(detalleDev);
                }
                devolucionRequest.setProductosDevueltos(productosDevueltos);
                devolucionRequest.setIdSalidaOriginal(salida.getIdSalida());

            } catch (RuntimeException e) {
                model.addAttribute("mensaje", e.getMessage());
                model.addAttribute("tipoMensaje", "error");
            }
        }
        return "almacenero/devolucion/form";
    }

    @PostMapping("/buscar")
    public String buscarSalida(@RequestParam(value = "idSalida", required = false) Long idSalida,
            @RequestParam(value = "numeroDocumento", required = false) String numeroDocumento,
            Model model) {
        return "redirect:/almacenero/devoluciones/nueva?idSalida=" + (idSalida != null ? idSalida : "")
                + "&numeroDocumento=" + (numeroDocumento != null ? numeroDocumento : "");
    }

    @PostMapping("/procesar")
    public String procesarDevolucion(DevolucionRequestDto devolucionRequest, RedirectAttributes redirectAttributes) {

        // Validacionn que productosDevueltos no sea null
        if (devolucionRequest.getProductosDevueltos() == null) {
            redirectAttributes.addFlashAttribute("mensaje",
                    "Error: No se recibieron datos de productos. Intenta nuevamente.");
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
            return "redirect:/almacenero/devoluciones/nueva";
        }

        // se filtra producto seleccionados
        List<DetalleDevolucionDto> productosValidos = devolucionRequest.getProductosDevueltos().stream()
                .filter(p -> Boolean.TRUE.equals(p.getSeleccionado()) && p.getCantidadDevuelta() != null
                        && p.getCantidadDevuelta() > 0)
                .collect(Collectors.toList());

        if (productosValidos.isEmpty()) {
            redirectAttributes.addFlashAttribute("mensaje",
                    "Debes seleccionar al menos un producto y una cantidad válida para devolver.");
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
            return "redirect:/almacenero/devoluciones/nueva";
        }

        // se reeemplza la lista con los datos filtrados
        devolucionRequest.setProductosDevueltos(productosValidos);
        try {
            EntradaDto nuevaEntrada = devolucionService.procesarDevolucion(devolucionRequest);
            redirectAttributes.addFlashAttribute("mensaje",
                    "Devolución procesada exitosamente. ID de Entrada: " + nuevaEntrada.getIdEntrada());
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error al procesar la devolución: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        }
        return "redirect:/almacenero/devoluciones/nueva";
    }
}
