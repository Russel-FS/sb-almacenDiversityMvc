package com.api.diversity.infrastructure.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Writer;
import com.google.zxing.oned.Code128Writer;
import com.google.zxing.common.BitMatrix;
import java.awt.image.BufferedImage;

@RestController
@Slf4j
public class BarcodeController {

    /**
     * Genera un código de barras Code 128 a partir de un texto recibido por
     * parámetro.
     * Ejemplo de uso: /barcode?data=123456789
     */
    @GetMapping("/barcode")
    public ResponseEntity<byte[]> generarCodigoBarras(@RequestParam String data) {
        try {
            byte[] imagenCodigoBarras = generarImagenCodigoBarras(data);
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .body(imagenCodigoBarras);
        } catch (Exception e) {
            log.error("Error generando código de barras: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Genera una imagen de código de barras en formato PNG a partir del texto
     * proporcionado.
     *
     * @param data El texto a codificar en el código de barras.
     * @return Un arreglo de bytes que representa la imagen del código de barras.
     */
    private byte[] generarImagenCodigoBarras(String data) {
        try {
            BarcodeFormat format = BarcodeFormat.CODE_128;
            Writer writer = new Code128Writer();

            BitMatrix bitMatrix = writer.encode(
                    data,
                    format,
                    400,
                    100);

            BufferedImage image = new BufferedImage(
                    bitMatrix.getWidth(),
                    bitMatrix.getHeight(),
                    java.awt.image.BufferedImage.TYPE_INT_RGB);

            for (int x = 0; x < bitMatrix.getWidth(); x++) {
                for (int y = 0; y < bitMatrix.getHeight(); y++) {
                    image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
                }
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            javax.imageio.ImageIO.write(image, "PNG", baos);
            return baos.toByteArray();
        } catch (Exception e) {
            log.error("Error generando imagen de código de barras: {}", e.getMessage(), e);
            return new byte[0];
        }
    }

}