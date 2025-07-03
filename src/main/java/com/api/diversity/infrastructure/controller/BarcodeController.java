package com.api.diversity.infrastructure.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

import com.api.diversity.application.service.impl.BarcodeService;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@Slf4j
public class BarcodeController {

    @Autowired
    private BarcodeService barcodeService;

    /**
     * Genera un código de barras Code 128 a partir de un texto recibido por
     * parámetro.
     * Ejemplo de uso: /barcode?data=123456789&width=400&height=100
     */
    @GetMapping("/barcode")
    public ResponseEntity<byte[]> generarCodigoBarras(
            @RequestParam String data,
            @RequestParam(required = false, defaultValue = "400") int width,
            @RequestParam(required = false, defaultValue = "100") int height) {
        try {
            byte[] imagenCodigoBarras = barcodeService.generarCodigoBarras(data, width, height);
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .body(imagenCodigoBarras);
        } catch (Exception e) {
            log.error("Error generando código de barras: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

}