package com.api.diversity.application.service.impl;

import org.springframework.stereotype.Service;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Writer;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.common.BitMatrix;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import javax.imageio.ImageIO;

@Service
public class QRService {

    /**
     * Genera un código QR en formato PNG.
     * 
     * @param data   Texto a codificar en el QR
     * @param width  Ancho de la imagen
     * @param height Alto de la imagen
     * @return Array de bytes de la imagen PNG
     */
    public byte[] generarQR(String data, int width, int height) {
        try {
            BarcodeFormat format = BarcodeFormat.QR_CODE;
            Writer writer = new QRCodeWriter();
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

    /**
     * Genera un código QR con datos estructurados para boletas.
     * 
     * @param numeroDocumento Número del documento
     * @param fecha           Fecha del documento
     * @param total           Total de la venta
     * @param ruc             RUC del cliente (si aplica)
     * @param width           Ancho de la imagen
     * @param height          Alto de la imagen
     * @return Array de bytes de la imagen PNG
     */
    public byte[] generarQRBoleta(String numeroDocumento, String fecha, String total, String ruc, int width,
            int height) {
        StringBuilder qrData = new StringBuilder();
        qrData.append("BOLETA|");
        qrData.append(numeroDocumento).append("|");
        qrData.append(fecha).append("|");
        qrData.append(total).append("|");
        if (ruc != null && !ruc.isEmpty()) {
            qrData.append(ruc);
        }

        return generarQR(qrData.toString(), width, height);
    }
}