package com.api.diversity.application.service.impl;

import org.springframework.stereotype.Service;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Writer;
import com.google.zxing.oned.Code128Writer;
import com.google.zxing.common.BitMatrix;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import javax.imageio.ImageIO;

@Service
public class BarcodeService {
    /**
     * Genera un código de barras Code 128 en formato PNG.
     * 
     * @param data   Texto a codificar
     * @param width  Ancho de la imagen
     * @param height Alto de la imagen
     * @return Array de bytes de la imagen PNG
     */
    public byte[] generarCodigoBarras(String data, int width, int height) {
        try {
            BarcodeFormat format = BarcodeFormat.CODE_128;
            Writer writer = new Code128Writer();
            BitMatrix bitMatrix = writer.encode(data,
                    format,
                    width,
                    height);

            BufferedImage image = new BufferedImage(
                    bitMatrix.getWidth(),
                    bitMatrix.getHeight(),
                    BufferedImage.TYPE_INT_RGB);

            for (int x = 0; x < bitMatrix.getWidth(); x++) {
                for (int y = 0; y < bitMatrix.getHeight(); y++) {
                    image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
                }
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos);
            return baos.toByteArray();
        } catch (Exception e) {
            return new byte[0];
        }
    }
}