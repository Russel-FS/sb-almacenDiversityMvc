package com.api.diversity.application.service.impl;

import com.api.diversity.application.dto.SunatResponseDto;
import com.api.diversity.application.service.interfaces.ISunatService;
import com.api.diversity.infrastructure.config.SunatConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
@Slf4j
public class SunatServiceImpl implements ISunatService {

    @Autowired
    private SunatConfig sunatConfig;

    @Override
    public SunatResponseDto generarBoletaElectronica(String numeroDocumento,
            String clienteNombre,
            String clienteDni,
            double totalVenta) {
        try {
            log.info("Generando boleta electrónica: {}", numeroDocumento);

            // Generar XML de la boleta
            String xmlBoleta = generarXMLBoleta(numeroDocumento, clienteNombre, clienteDni, totalVenta);

            // Firmar el documento
            String xmlFirmado = firmarDocumento(xmlBoleta);

            // Enviar a SUNAT (en ambiente de pruebas, simulamos la respuesta)
            SunatResponseDto respuesta = simularEnvioSUNAT(xmlFirmado, numeroDocumento);

            log.info("Boleta electrónica generada exitosamente: {}", numeroDocumento);
            return respuesta;

        } catch (Exception e) {
            log.error("Error generando boleta electrónica: {}", e.getMessage(), e);
            return SunatResponseDto.builder()
                    .autorizado(false)
                    .mensaje("Error generando documento: " + e.getMessage())
                    .codigoError("ERROR_GENERACION")
                    .build();
        }
    }

    private String generarXMLBoleta(String numeroDocumento,
            String clienteNombre,
            String clienteDni,
            double totalVenta) {
        // Generar número de serie y correlativo
        String serie = "B001";
        String correlativo = numeroDocumento.substring(numeroDocumento.lastIndexOf("-") + 1);

        // Fecha actual
        String fechaEmision = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String horaEmision = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));

        // Generar XML básico de boleta
        StringBuilder xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        xml.append("<Invoice xmlns=\"urn:oasis:names:specification:ubl:schema:xsd:Invoice-2\" ");
        xml.append("xmlns:cac=\"urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2\" ");
        xml.append("xmlns:cbc=\"urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2\" ");
        xml.append("xmlns:ccts=\"urn:un:unece:uncefact:documentation:2\" ");
        xml.append("xmlns:ds=\"http://www.w3.org/2000/09/xmldsig#\" ");
        xml.append("xmlns:ext=\"urn:oasis:names:specification:ubl:schema:xsd:CommonExtensionComponents-2\" ");
        xml.append("xmlns:qdt=\"urn:oasis:names:specification:ubl:schema:xsd:QualifiedDatatypes-2\" ");
        xml.append("xmlns:sac=\"urn:sunat:names:specification:ubl:peru:schema:xsd:SunatAggregateComponents-1\" ");
        xml.append("xmlns:udt=\"urn:un:unece:uncefact:data:specification:UnqualifiedDataTypesSchemaModule:2\" ");
        xml.append("xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">");

        // Información básica del documento
        xml.append("<cbc:UBLVersionID>2.1</cbc:UBLVersionID>");
        xml.append("<cbc:CustomizationID>2.0</cbc:CustomizationID>");
        xml.append("<cbc:ID>").append(serie).append("-").append(correlativo).append("</cbc:ID>");
        xml.append("<cbc:IssueDate>").append(fechaEmision).append("</cbc:IssueDate>");
        xml.append("<cbc:IssueTime>").append(horaEmision).append("</cbc:IssueTime>");
        xml.append("<cbc:DocumentCurrencyCode>PEN</cbc:DocumentCurrencyCode>");

        // Emisor (tu información)
        xml.append("<cac:AccountingSupplierParty>");
        xml.append("<cac:Party>");
        xml.append("<cac:PartyIdentification>");
        xml.append("<cbc:ID schemeID=\"6\">").append(sunatConfig.getRuc()).append("</cbc:ID>");
        xml.append("</cac:PartyIdentification>");
        xml.append("<cac:PartyName>");
        xml.append("<cbc:Name>").append(sunatConfig.getRazonSocial()).append("</cbc:Name>");
        xml.append("</cac:PartyName>");
        xml.append("</cac:Party>");
        xml.append("</cac:AccountingSupplierParty>");

        // Cliente
        xml.append("<cac:AccountingCustomerParty>");
        xml.append("<cac:Party>");
        xml.append("<cac:PartyIdentification>");
        xml.append("<cbc:ID schemeID=\"1\">").append(clienteDni).append("</cbc:ID>");
        xml.append("</cac:PartyIdentification>");
        xml.append("<cac:PartyName>");
        xml.append("<cbc:Name>").append(clienteNombre).append("</cbc:Name>");
        xml.append("</cac:PartyName>");
        xml.append("</cac:Party>");
        xml.append("</cac:AccountingCustomerParty>");

        // Totales
        xml.append("<cac:LegalMonetaryTotal>");
        xml.append("<cbc:PayableAmount currencyID=\"PEN\">").append(totalVenta).append("</cbc:PayableAmount>");
        xml.append("</cac:LegalMonetaryTotal>");

        xml.append("</Invoice>");

        return xml.toString();
    }

    private String firmarDocumento(String xml) {
        // En ambiente de pruebas, retornamos el XML sin firmar
        // En producción, aquí iría la lógica de firma digitalllll
        log.info("Simulando firma digital del documento");
        return xml;
    }

    private SunatResponseDto simularEnvioSUNAT(String xmlFirmado, String numeroDocumento) {
        // En ambiente de pruebas, simulamos una respuesta exitosa
        // En producción, aquí iría la coomunicación real con SUNAT

        String codigoAutorizacion = generarCodigoAutorizacion(numeroDocumento);

        return SunatResponseDto.builder()
                .autorizado(true)
                .codigoAutorizacion(codigoAutorizacion)
                .mensaje("Documento autorizado exitosamente")
                .xmlRespuesta(xmlFirmado)
                .hash(generarHash(xmlFirmado))
                .build();
    }

    private String generarCodigoAutorizacion(String numeroDocumento) {
        // Generar código de autorización simulado
        // Formato: RUC + SERIE + CORRELATIVO + HASH + ITEM
        String ruc = sunatConfig.getRuc();
        String serie = "B001";
        String correlativo = numeroDocumento.substring(numeroDocumento.lastIndexOf("-") + 1);
        String hash = UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        return ruc + "-" + serie + "-" + correlativo + "-" + hash + "-00000001";
    }

    private String generarHash(String xml) {
        // Generar hash simulado del XML
        return UUID.randomUUID().toString().substring(0, 16).toUpperCase();
    }

    @Override
    public boolean tieneCredencialesSUNAT() {
        return sunatConfig.getRuc() != null &&
                sunatConfig.getClaveSol() != null &&
                sunatConfig.getRazonSocial() != null &&
                !sunatConfig.getRazonSocial().equals("Tu Nombre Completo"); // Validar que no sea el valor por defecto
    }
}