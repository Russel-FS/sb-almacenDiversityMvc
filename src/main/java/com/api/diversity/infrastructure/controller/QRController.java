package com.api.diversity.infrastructure.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

import com.api.diversity.application.service.impl.QRService;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@Slf4j
public class QRController {

    @Autowired
    private QRService qrService;

    /**
     * Genera un código QR a partir de un texto recibido por parámetro.
     * Ejemplo de uso: /qr?data=123456789&width=200&height=200
     */
    @GetMapping("/qr")
    public ResponseEntity<byte[]> generarQR(
            @RequestParam String data,
            @RequestParam(required = false, defaultValue = "200") int width,
            @RequestParam(required = false, defaultValue = "200") int height) {
        try {
            byte[] imagenQR = qrService.generarQR(data, width, height);
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .body(imagenQR);
        } catch (Exception e) {
            log.error("Error generando código QR: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Genera un código QR específico para boletas con datos estructurados.
     * Ejemplo de uso:
     * /qr/boleta?numero=BOL-001&fecha=2024-01-01&total=150.00&ruc=12345678&width=200&height=200
     */
    @GetMapping("/qr/boleta")
    public ResponseEntity<byte[]> generarQRBoleta(
            @RequestParam String numero,
            @RequestParam String fecha,
            @RequestParam String total,
            @RequestParam(required = false) String ruc,
            @RequestParam(required = false, defaultValue = "200") int width,
            @RequestParam(required = false, defaultValue = "200") int height) {
        try {
            byte[] imagenQR = qrService.generarQRBoleta(numero, fecha, total, ruc, width, height);
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .body(imagenQR);
        } catch (Exception e) {
            log.error("Error generando código QR para boleta: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
}